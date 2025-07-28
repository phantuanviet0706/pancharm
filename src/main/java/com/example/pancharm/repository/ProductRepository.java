package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Products;

@Repository
public interface ProductRepository extends JpaRepository<Products, Integer> {
	boolean existsBySlug(String slug);
}
