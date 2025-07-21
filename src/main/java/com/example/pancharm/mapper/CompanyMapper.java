package com.example.pancharm.mapper;

import com.example.pancharm.dto.request.company.CompanyRequest;
import com.example.pancharm.dto.response.CompanyResponse;
import com.example.pancharm.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", uses = CompanyInfoMapper.class)
public interface CompanyMapper {
	CompanyResponse toCompanyResponse(Company company);

	@Mapping(target = "companyInfos", ignore = true)
	void updateCompany(CompanyRequest companyRequest, @MappingTarget Company company);
}
