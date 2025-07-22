package com.example.pancharm.repository;

import com.example.pancharm.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface RoleRepository extends JpaRepository<Roles, String> {
	boolean existsByName(String name);

	Set<Roles> findAllByNameIn(Set<String> names);
}
