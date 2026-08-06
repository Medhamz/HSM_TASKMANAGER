package com.hsm.taskmanager.controller;

import com.hsm.taskmanager.entity.Project;
import com.hsm.taskmanager.entity.TestClass;
import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;
import com.hsm.taskmanager.service.ProjectService;
import com.hsm.taskmanager.service.TestClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TestClassService testClassService;

    // --- GESTION DES PROJETS ---

    @GetMapping("/projects")
    public String listProjects(Model model) {
        model.addAttribute("projects", projectService.findAll());
        return "admin/projects";
    }

    @GetMapping("/projects/new")
    public String showProjectForm(Model model) {
        model.addAttribute("project", new Project());
        return "admin/project-form";
    }

    @PostMapping("/projects/save")
    public String saveProject(@Valid @ModelAttribute("project") Project project, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/project-form";
        }
        projectService.save(project);
        return "redirect:/admin/projects";
    }

    @GetMapping("/projects/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findById(id));
        return "admin/project-form";
    }

    @GetMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return "redirect:/admin/projects";
    }

    // --- GESTION DES TESTS ---

    @GetMapping("/tests")
    public String listTests(Model model) {
        model.addAttribute("tests", testClassService.findAll());
        model.addAttribute("projects", projectService.findAll());
        return "admin/tests";
    }

    @GetMapping("/tests/new")
    public String showTestForm(Model model) {
        model.addAttribute("testClass", new TestClass());
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("types", TestType.values());
        return "admin/test-form";
    }

    @PostMapping("/tests/save")
    public String saveTest(@Valid @ModelAttribute("testClass") TestClass testClass,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("types", TestType.values());
            return "admin/test-form";
        }
        testClassService.save(testClass);
        return "redirect:/admin/tests";
    }

    @GetMapping("/tests/edit/{id}")
    public String editTest(@PathVariable Long id, Model model) {
        model.addAttribute("testClass", testClassService.findById(id));
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("types", TestType.values());
        return "admin/test-form";
    }

    @GetMapping("/tests/delete/{id}")
    public String deleteTest(@PathVariable Long id) {
        testClassService.delete(id);
        return "redirect:/admin/tests";
    }
}