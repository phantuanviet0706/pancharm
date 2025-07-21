package com.example.pancharm.mapper;

import com.example.pancharm.dto.response.CompanyInfoResponse;
import com.example.pancharm.entity.CompanyInfos;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CompanyInfoMapper {
	CompanyInfoResponse toCompanyInfoResponse(CompanyInfos companyInfos);
}
