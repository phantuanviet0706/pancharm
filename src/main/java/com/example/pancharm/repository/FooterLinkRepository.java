package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pancharm.entity.FooterLinks;

public interface FooterLinkRepository
        extends JpaRepository<FooterLinks, String>, JpaSpecificationExecutor<FooterLinks> {}
