package com.dbms.placementapp.repositories;

import java.util.List;

import com.dbms.placementapp.models.Recruiter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter, Integer> {
    List<Recruiter> findByNameAndRole(String name, String role);

    List<Recruiter> findByRecruiterId(Integer recruiterId);

    List<Recruiter> findByRole(String role);

    List<Recruiter> findByName(String name);
}
