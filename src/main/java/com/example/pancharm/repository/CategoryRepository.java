package com.example.pancharm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Categories;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, String> {
    boolean existsBySlug(String slug);

    List<Categories> findAllByParentId(int id);
}
