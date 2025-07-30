package com.example.pancharm.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Permissions;

@Repository
public interface PermissionRepository
        extends JpaRepository<Permissions, String>, JpaSpecificationExecutor<Permissions> {
    boolean existsByName(String name);

    Set<Permissions> findAllByNameIn(Set<String> names);
}
