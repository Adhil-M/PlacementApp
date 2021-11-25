package com.dbms.placementapp.repositories;

import java.util.List;

import com.dbms.placementapp.models.UserCreds;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCredsRepository extends JpaRepository<UserCreds, Integer> {
    List<UserCreds> findByUsername(String username);

    List<UserCreds> findByUserId(Integer userId);
}
