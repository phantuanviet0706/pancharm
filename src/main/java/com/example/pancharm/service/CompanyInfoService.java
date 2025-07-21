package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.company.CompanyInfoRequest;
import com.example.pancharm.dto.response.CompanyInfoResponse;
import com.example.pancharm.entity.CompanyInfos;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CompanyInfoMapper;
import com.example.pancharm.repository.CompanyInfoRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.MappingTarget;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyInfoService {
	CompanyInfoRepository companyInfoRepository;
	CompanyInfoMapper companyInfoMapper;

	public CompanyInfoResponse createCompanyInfo(CompanyInfoRequest request) {
		var companyInfo = companyInfoMapper.toCompanyInfos(request);

		try {
			companyInfo = companyInfoRepository.save(companyInfo);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return companyInfoMapper.toCompanyInfoResponse(companyInfo);
	}

	public CompanyInfoResponse updateCompanyInfo(CompanyInfoRequest request, int companyInfoId){
		var companyInfo = companyInfoRepository.findById(String.valueOf(companyInfoId)).orElseThrow(
				() -> new AppException(ErrorCode.COMPANY_INFO_NOT_FOUND)
		);

		companyInfoMapper.updateCompanyInfo(companyInfo, request);

		try {
			companyInfo = companyInfoRepository.save(companyInfo);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return companyInfoMapper.toCompanyInfoResponse(companyInfo);
	}

	public void deleteCompanyInfo(int companyInfoId){
		companyInfoRepository.deleteById(String.valueOf(companyInfoId));
	}

	public CompanyInfoResponse getCompanyInfo(int companyInfoId){
		var companyInfo = companyInfoRepository.findById(String.valueOf(companyInfoId)).orElseThrow(
				() -> new AppException(ErrorCode.COMPANY_INFO_NOT_FOUND)
		);

		return companyInfoMapper.toCompanyInfoResponse(companyInfo);
	}

	public List<CompanyInfoResponse> getCompanyInfos(){
		return companyInfoRepository.findAll().stream().map(companyInfoMapper::toCompanyInfoResponse).toList();
	}
}
