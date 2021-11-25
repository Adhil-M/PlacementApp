package com.dbms.placementapp.repositories;

import java.util.List;

import com.dbms.placementapp.models.IsPlaced;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IsPlacedRepository extends JpaRepository<IsPlaced, Integer> {
    List<IsPlaced> findByStudentId(Integer studentId);

    List<IsPlaced> findByRecruiterId(Integer recruiterId);

    List<IsPlaced> deleteByRecruiterId(Integer recruiterId);

}
