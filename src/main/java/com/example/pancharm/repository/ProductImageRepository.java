package com.example.pancharm.repository;

import com.example.pancharm.entity.ProductImages;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImages, Integer> {
}
