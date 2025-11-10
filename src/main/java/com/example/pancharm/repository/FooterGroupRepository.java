package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.FooterGroups;

@Repository
public interface FooterGroupRepository
        extends JpaRepository<FooterGroups, Integer>, JpaSpecificationExecutor<FooterGroups> {}
