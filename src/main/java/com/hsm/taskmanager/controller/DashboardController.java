package com.hsm.taskmanager.controller;

import com.hsm.taskmanager.entity.Project;
import com.hsm.taskmanager.entity.TestClass;
import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;   // ← Import ajouté
import com.hsm.taskmanager.service.ProjectService;
import com.hsm.taskmanager.service.TestClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
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
        // Semaine actuelle par défaut
        LocalDate today = LocalDate.now();
        WeekFields weekFields = WeekFields.ISO;
        int currentWeek = today.get(weekFields.weekOfWeekBasedYear());
        int currentYear = today.get(weekFields.weekBasedYear());

        int selectedWeek = (week != null) ? week : currentWeek;
        int selectedYear = (year != null) ? year : currentYear;

        // Récupérer tous les tests
        List<TestClass> allTests = testClassService.findAll();
        Map<Status, Long> statusStats = testClassService.countByStatus();
        Map<TestType, Long> typeStats = testClassService.countByType();  // Maintenant reconnu

        // Organiser les tests par semaine
        Map<String, List<TestClass>> testsByWeek = new LinkedHashMap<>();

        // Déterminer la plage de semaines (les 4 dernières + la semaine sélectionnée)
        Set<String> weeksToShow = new LinkedHashSet<>();
        for (int i = 3; i >= 0; i--) {
            LocalDate date = today.minusWeeks(i);
            int w = date.get(weekFields.weekOfWeekBasedYear());
            int y = date.get(weekFields.weekBasedYear());
            weeksToShow.add(y + "-W" + String.format("%02d", w));
        }
        weeksToShow.add(selectedYear + "-W" + String.format("%02d", selectedWeek));
        // Trier par ordre chronologique
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
                    .filter(t -> {
                        LocalDate start = t.getStartDate();
                        return !start.isBefore(startOfWeek) && !start.isAfter(endOfWeek);
                    })
                    .sorted(Comparator.comparing(TestClass::getStatus)
                            .thenComparing(TestClass::getStartDate))
                    .collect(Collectors.toList());

            // Trier : en cours d'abord, puis suspendu, puis terminé, puis à faire
            List<TestClass> sortedTests = testsInWeek.stream()
                    .sorted(Comparator.comparing((TestClass t) -> {
                        switch (t.getStatus()) {
                            case IN_PROGRESS: return 0;
                            case SUSPENDED: return 1;
                            case COMPLETED: return 2;
                            default: return 3;
                        }
                    }).thenComparing(TestClass::getStartDate))
                    .collect(Collectors.toList());

            testsByWeek.put(weekKey, sortedTests);
        }

        // Projets pour le dropdown
        List<Project> projects = projectService.findAll();

        model.addAttribute("testsByWeek", testsByWeek);
        model.addAttribute("selectedWeek", selectedWeek);
        model.addAttribute("selectedYear", selectedYear);
        model.addAttribute("projects", projects);
        model.addAttribute("totalTests", allTests.size());
        model.addAttribute("statusStats", statusStats);
        model.addAttribute("typeStats", typeStats);

        return "dashboard";
    }
}