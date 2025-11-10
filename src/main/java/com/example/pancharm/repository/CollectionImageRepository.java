package com.example.pancharm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.example.pancharm.entity.CollectionImages;

@Repository
public interface CollectionImageRepository
        extends JpaRepository<CollectionImages, Integer>, JpaSpecificationExecutor<CollectionImages> {}
