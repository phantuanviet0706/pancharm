package com.example.pancharm.repository;

import com.example.pancharm.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface ProductImageRepository
		extends JpaRepository<ProductImages, Integer>, JpaSpecificationExecutor<ProductImages> {
}
