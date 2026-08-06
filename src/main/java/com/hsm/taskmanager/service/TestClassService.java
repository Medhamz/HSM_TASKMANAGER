package com.hsm.taskmanager.service;

import com.hsm.taskmanager.entity.TestClass;
import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;
import com.hsm.taskmanager.repository.TestClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.Arrays;

@Service
public class TestClassService {

    @Autowired
    private TestClassRepository testClassRepository;

    public List<TestClass> findAll() {
        return testClassRepository.findAll();
    }

    public List<TestClass> findByProject(Long projectId) {
        return testClassRepository.findByProjectId(projectId);
    }

    public TestClass findById(Long id) {
        return testClassRepository.findById(id).orElseThrow(() -> new RuntimeException("Test class not found"));
    }

    public TestClass save(TestClass testClass) {
        return testClassRepository.save(testClass);
    }

    public void delete(Long id) {
        testClassRepository.deleteById(id);
    }

    // Statistiques pour le dashboard
    public Map<Status, Long> countByStatus() {
        return Arrays.stream(Status.values())
                .collect(Collectors.toMap(
                        s -> s,
                        s -> testClassRepository.countByStatus(s)
                ));
    }

    public Map<TestType, Long> countByType() {
        return Arrays.stream(TestType.values())
                .collect(Collectors.toMap(
                        t -> t,
                        t -> testClassRepository.countByType(t)
                ));
    }

    public long countTotal() {
        return testClassRepository.count();
    }
}