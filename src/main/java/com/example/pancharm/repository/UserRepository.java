package com.example.pancharm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, String> {
    boolean existsByUsername(String username);

    Optional<Users> findByUsername(String username);

    boolean existsByEmail(String email);

    Optional<Users> findByEmail(String email);
}
