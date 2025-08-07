package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pancharm.entity.InvalidatedToken;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository
        extends JpaRepository<InvalidatedToken, String>, JpaSpecificationExecutor<InvalidatedToken> {}
