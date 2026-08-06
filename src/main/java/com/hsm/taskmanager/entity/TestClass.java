package com.hsm.taskmanager.entity;

import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
public class TestClass {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Test class name is required")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    private TestType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Status status;

    @NotNull
    private LocalDate startDate;

    @Min(value = 1, message = "Estimated hours must be at least 1")
    private Integer estimatedHours;

    private LocalDate completionDate;

    @Min(value = 0, message = "Actual hours cannot be negative")
    private Integer actualHours; // Heures réellement passées

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    // Constructeurs, Getters & Setters
    public TestClass() {}

    public TestClass(String name, TestType type, Status status, LocalDate startDate,
                     Integer estimatedHours, LocalDate completionDate, Integer actualHours, Project project) {
        this.name = name;
        this.type = type;
        this.status = status;
        this.startDate = startDate;
        this.estimatedHours = estimatedHours;
        this.completionDate = completionDate;
        this.actualHours = actualHours;
        this.project = project;
    }

    // --- Getters et Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public TestType getType() { return type; }
    public void setType(TestType type) { this.type = type; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public Integer getEstimatedHours() { return estimatedHours; }
    public void setEstimatedHours(Integer estimatedHours) { this.estimatedHours = estimatedHours; }
    public LocalDate getCompletionDate() { return completionDate; }
    public void setCompletionDate(LocalDate completionDate) { this.completionDate = completionDate; }
    public Integer getActualHours() { return actualHours; }
    public void setActualHours(Integer actualHours) { this.actualHours = actualHours; }
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
}