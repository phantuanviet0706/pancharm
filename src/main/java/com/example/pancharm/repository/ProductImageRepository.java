package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.example.pancharm.entity.ProductImages;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductImageRepository
        extends JpaRepository<ProductImages, Integer>, JpaSpecificationExecutor<ProductImages> {}
