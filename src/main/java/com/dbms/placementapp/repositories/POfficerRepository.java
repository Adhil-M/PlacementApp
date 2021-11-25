package com.dbms.placementapp.repositories;

import com.dbms.placementapp.models.POfficer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface POfficerRepository extends JpaRepository<POfficer, Integer> {

}
