package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.pancharm.entity.InvalidatedToken;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {}
