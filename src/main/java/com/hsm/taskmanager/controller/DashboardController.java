package com.hsm.taskmanager.controller;

import com.hsm.taskmanager.service.ProjectService;
import com.hsm.taskmanager.service.TestClassService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TestClassService testClassService;

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("totalTests", testClassService.countTotal());
        model.addAttribute("statusStats", testClassService.countByStatus());
        model.addAttribute("typeStats", testClassService.countByType());
        return "dashboard";
    }
}