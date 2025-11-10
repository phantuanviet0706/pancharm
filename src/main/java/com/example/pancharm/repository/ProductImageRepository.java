package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.ProductImages;

@Repository
public interface ProductImageRepository
        extends JpaRepository<ProductImages, Integer>, JpaSpecificationExecutor<ProductImages> {}
