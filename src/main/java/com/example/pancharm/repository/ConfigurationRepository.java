package com.example.pancharm.repository;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.entity.Configurations;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigurationRepository extends JpaRepository<Configurations, Integer> {
	boolean existsByName(ConfigurationName name);
}
