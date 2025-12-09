package com.example.pancharm.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.Collections;

@Repository
public interface CollectionRepository
        extends JpaRepository<Collections, Integer>, JpaSpecificationExecutor<Collections> {
    boolean existsBySlug(String slug);

    Collections findByIsDefault(short isDefault);

    List<Collections> findAllByIsDefault(short isDefault);

    List<Collections> findTop10ByOrderByIdDesc();

}
