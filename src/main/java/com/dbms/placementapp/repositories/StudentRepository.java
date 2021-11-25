package com.dbms.placementapp.repositories;

import java.util.List;

import com.dbms.placementapp.models.Student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {
    List<Student> findByRegNo(String regNo);

    List<Student> findByStudentId(Integer studentId);
}
