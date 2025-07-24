package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.company.CompanyInfoRequest;
import com.example.pancharm.dto.response.CompanyInfoResponse;
import com.example.pancharm.entity.CompanyInfos;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CompanyInfoMapper {
	@Mapping(target = "company", ignore = true)
	@Mapping(target = "user", ignore = true)
	CompanyInfoResponse toCompanyInfoResponse(CompanyInfos companyInfos);

	CompanyInfos toCompanyInfos(CompanyInfoRequest request);

	void updateCompanyInfo(@MappingTarget CompanyInfos companyInfos, CompanyInfoRequest request);
}
