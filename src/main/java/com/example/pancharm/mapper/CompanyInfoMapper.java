package com.example.pancharm.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import com.example.pancharm.dto.request.company.CompanyInfoRequest;
import com.example.pancharm.dto.response.company.CompanyInfoResponse;
import com.example.pancharm.entity.CompanyInfos;

@Mapper(componentModel = "spring")
public interface CompanyInfoMapper {
    @Mapping(target = "user", source = "personInCharge")
    CompanyInfoResponse toCompanyInfoResponse(CompanyInfos companyInfos);

    CompanyInfos toCompanyInfos(CompanyInfoRequest request);

    void updateCompanyInfo(@MappingTarget CompanyInfos companyInfos, CompanyInfoRequest request);
}
