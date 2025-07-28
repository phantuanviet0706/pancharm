package com.example.pancharm.repository;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.entity.Configurations;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ConfigurationRepository extends JpaRepository<Configurations, Integer> {
	boolean existsByName(ConfigurationName name);

	Optional<Configurations> findByName(ConfigurationName name);
}
