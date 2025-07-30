package com.example.pancharm.repository;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.entity.Configurations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ConfigurationRepository
		extends JpaRepository<Configurations, Integer>, JpaSpecificationExecutor<Configurations> {
	boolean existsByName(ConfigurationName name);

	Optional<Configurations> findByName(ConfigurationName name);
}
