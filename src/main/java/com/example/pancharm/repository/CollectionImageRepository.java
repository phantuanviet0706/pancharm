package com.example.pancharm.repository;

import com.example.pancharm.entity.CollectionImages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CollectionImageRepository
        extends JpaRepository<CollectionImages, Integer>, JpaSpecificationExecutor<CollectionImages> {}
