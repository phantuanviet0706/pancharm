package com.example.pancharm.repository;

import com.example.pancharm.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, String> {
	boolean existsBySlug(String slug);

	List<Categories> findAllByParentId(int id);
}
