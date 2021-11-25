package com.dbms.placementapp.repositories;

import java.util.List;
import java.util.Optional;

import com.dbms.placementapp.models.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    public List<User> findByEmail(String email);

    public Optional<User> findById(Integer id);

}
