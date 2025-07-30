package com.example.pancharm.repository;

import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Roles;

@Repository
public interface RoleRepository
        extends JpaRepository<Roles, String>, JpaSpecificationExecutor<Roles> {
    boolean existsByName(String name);

    Set<Roles> findAllByNameIn(Set<String> names);
}
