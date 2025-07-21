package com.example.pancharm.repository;

import com.example.pancharm.entity.CompanyInfos;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfos, String> {
}
