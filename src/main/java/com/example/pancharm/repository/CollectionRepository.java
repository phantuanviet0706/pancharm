package com.example.pancharm.repository;

import com.example.pancharm.entity.Collections;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface CollectionRepository extends JpaRepository<Collections, Integer>, JpaSpecificationExecutor<Collections> {

}
