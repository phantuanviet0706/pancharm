package com.example.pancharm.repository;

import com.example.pancharm.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, String> {
	Optional<Company> findByName(String name);
}
