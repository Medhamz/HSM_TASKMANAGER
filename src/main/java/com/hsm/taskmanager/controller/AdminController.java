package com.hsm.taskmanager.controller;

import com.hsm.taskmanager.entity.Project;
import com.hsm.taskmanager.entity.TestClass;
import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;
import com.hsm.taskmanager.service.ExportService;
import com.hsm.taskmanager.service.ProjectService;
import com.hsm.taskmanager.service.TestClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private ProjectService projectService;

    @Autowired
    private TestClassService testClassService;

    @Autowired
    private ExportService exportService;

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
    public String saveProject(@Valid @ModelAttribute("project") Project project, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "admin/project-form";
        }
        projectService.save(project);
        redirectAttributes.addFlashAttribute("success", "Project saved!");
        return "redirect:/admin/projects";
    }

    @GetMapping("/projects/edit/{id}")
    public String editProject(@PathVariable Long id, Model model) {
        model.addAttribute("project", projectService.findById(id));
        return "admin/project-form";
    }

    @GetMapping("/projects/delete/{id}")
    public String deleteProject(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        projectService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Project deleted!");
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
                           BindingResult result,
                           @RequestParam("projectId") Long projectId,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("projects", projectService.findAll());
            model.addAttribute("statuses", Status.values());
            model.addAttribute("types", TestType.values());
            return "admin/test-form";
        }
        // Récupérer le projet et l'associer
        Project project = projectService.findById(projectId);
        testClass.setProject(project);
        testClassService.save(testClass);
        redirectAttributes.addFlashAttribute("success", "Test class saved!");
        return "redirect:/admin/tests";
    }

    @GetMapping("/tests/edit/{id}")
    public String editTest(@PathVariable Long id, Model model) {
        TestClass testClass = testClassService.findById(id);
        model.addAttribute("testClass", testClass);
        model.addAttribute("projects", projectService.findAll());
        model.addAttribute("statuses", Status.values());
        model.addAttribute("types", TestType.values());
        return "admin/test-form";
    }

    @GetMapping("/tests/delete/{id}")
    public String deleteTest(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        testClassService.delete(id);
        redirectAttributes.addFlashAttribute("success", "Test class deleted!");
        return "redirect:/admin/tests";
    }

    // --- EXPORT ---

    @GetMapping("/tests/export/csv")
    public ResponseEntity<byte[]> exportCsv() throws IOException {
        List<TestClass> tests = testClassService.findAll();
        byte[] csvData = exportService.exportCsv(tests);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType("text/csv"));
        headers.setContentDispositionFormData("attachment", "test_classes_export.csv");
        headers.setContentLength(csvData.length);
        return ResponseEntity.ok().headers(headers).body(csvData);
    }

    @GetMapping("/tests/export/pdf")
    public ResponseEntity<byte[]> exportPdf() throws IOException {
        List<TestClass> tests = testClassService.findAll();
        byte[] pdfData = exportService.exportPdf(tests);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "test_classes_export.pdf");
        headers.setContentLength(pdfData.length);
        return ResponseEntity.ok().headers(headers).body(pdfData);
    }
}