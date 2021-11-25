package com.dbms.placementapp.repositories;

import java.util.List;

import com.dbms.placementapp.models.AppliedFor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppliedForRepository extends JpaRepository<AppliedFor, Integer> {
    List<AppliedFor> findByRecruiterIdAndStudentId(Integer recruiterId, Integer studentID);

    List<AppliedFor> findByRecruiterId(Integer recruiterId);

    List<AppliedFor> findByStudentId(Integer studentId);

    List<AppliedFor> deleteByStudentId(Integer studentId);

}
