package com.example.pancharm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.entity.Configurations;

@Repository
public interface ConfigurationRepository
        extends JpaRepository<Configurations, Integer>, JpaSpecificationExecutor<Configurations> {
    boolean existsByName(ConfigurationName name);

    Optional<Configurations> findByName(ConfigurationName name);
}
