package com.hsm.taskmanager.controller;

import com.hsm.taskmanager.entity.TestClass;
import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;
import com.hsm.taskmanager.service.ProjectService;
import com.hsm.taskmanager.service.TestClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.WeekFields;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TestClassService testClassService;

    @GetMapping
    public String dashboard(Model model,
                            @RequestParam(required = false) Integer week,
                            @RequestParam(required = false) Integer year) {
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.ISO;
        int currentWeek = today.get(weekFields.weekOfWeekBasedYear());
        int currentYear = today.get(weekFields.weekBasedYear());

        int selectedWeek = (week != null) ? week : currentWeek;
        int selectedYear = (year != null) ? year : currentYear;

        List<TestClass> allTests = testClassService.findAll();

        // Récupération et conversion des clés Enum -> String pour éviter les erreurs de sérialisation JSON/Thymeleaf
        Map<Status, Long> rawStatusStats = testClassService.countByStatus();
        Map<TestType, Long> rawTypeStats = testClassService.countByType();

        Map<String, Long> statusStats = new HashMap<>();
        if (rawStatusStats != null) {
            rawStatusStats.forEach((k, v) -> statusStats.put(k.name(), v));
        }

        Map<String, Long> typeStats = new HashMap<>();
        if (rawTypeStats != null) {
            rawTypeStats.forEach((k, v) -> typeStats.put(k.name(), v));
        }

        // Organisation par semaines
        Map<String, List<TestClass>> testsByWeek = new LinkedHashMap<>();
        Set<String> weeksToShow = new LinkedHashSet<>();
        for (int i = 3; i >= 0; i--) {
            LocalDate date = today.minusWeeks(i);
            int w = date.get(weekFields.weekOfWeekBasedYear());
            int y = date.get(weekFields.weekBasedYear());
            weeksToShow.add(y + "-W" + String.format("%02d", w));
        }
        weeksToShow.add(selectedYear + "-W" + String.format("%02d", selectedWeek));

        List<String> sortedWeeks = weeksToShow.stream().sorted().collect(Collectors.toList());

        for (String weekKey : sortedWeeks) {
            String[] parts = weekKey.split("-W");
            int y = Integer.parseInt(parts[0]);
            int w = Integer.parseInt(parts[1]);
            LocalDate startOfWeek = LocalDate.of(y, 1, 1)
                    .with(weekFields.weekOfWeekBasedYear(), w)
                    .with(weekFields.dayOfWeek(), 1);
            LocalDate endOfWeek = startOfWeek.plusDays(6);

            List<TestClass> testsInWeek = allTests.stream()
                    .filter(t -> t.getStartDate() != null &&
                            !t.getStartDate().isBefore(startOfWeek) &&
                            !t.getStartDate().isAfter(endOfWeek))
                    .collect(Collectors.toList());

            List<TestClass> sortedTests = testsInWeek.stream()
                    .sorted(Comparator.comparing((TestClass t) -> {
                        if (t.getStatus() == null) return 3;
                        switch (t.getStatus()) {
                            case IN_PROGRESS: return 0;
                            case SUSPENDED: return 1;
                            case COMPLETED: return 2;
                            default: return 3;
                        }
                    }).thenComparing(TestClass::getStartDate, Comparator.nullsLast(Comparator.naturalOrder())))
                    .collect(Collectors.toList());

            testsByWeek.put(weekKey, sortedTests);
        }

        model.addAttribute("testsByWeek", testsByWeek);
        model.addAttribute("selectedWeek", selectedWeek);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("totalTests", allTests.size());
        model.addAttribute("statusStats", statusStats);
        model.addAttribute("typeStats", typeStats);
        model.addAttribute("today", today);

        return "dashboard";
    }
}