package com.example.pancharm.repository;

import com.example.pancharm.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, String> {
	boolean existsByUsername(String username);

	Optional<Users> findByUsername(String username);
}
