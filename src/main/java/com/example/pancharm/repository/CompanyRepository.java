package com.example.pancharm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Company;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {
    Optional<Company> findByName(String name);
}
