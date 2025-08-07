package com.example.pancharm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.entity.Configurations;
import org.springframework.stereotype.Repository;

@Repository
public interface ConfigurationRepository
        extends JpaRepository<Configurations, Integer>, JpaSpecificationExecutor<Configurations> {
    boolean existsByName(ConfigurationName name);

    Optional<Configurations> findByName(ConfigurationName name);
}
