package com.example.pancharm.repository;

import com.example.pancharm.entity.Permissions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface PermissionRepository extends JpaRepository<Permissions, String> {
	boolean existsByName(String name);

	Set<Permissions> findAllByNameIn(Set<String> names);

}
