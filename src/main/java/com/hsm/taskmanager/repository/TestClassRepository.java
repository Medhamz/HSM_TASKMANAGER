package com.hsm.taskmanager.repository;

import com.hsm.taskmanager.entity.TestClass;
import com.hsm.taskmanager.entity.enums.Status;
import com.hsm.taskmanager.entity.enums.TestType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TestClassRepository extends JpaRepository<TestClass, Long> {
    long countByStatus(Status status);
    long countByType(TestType type);
    List<TestClass> findByProjectId(Long projectId);
}