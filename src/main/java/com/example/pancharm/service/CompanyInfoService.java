package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.company.CompanyInfoRequest;
import com.example.pancharm.dto.response.CompanyInfoResponse;
import com.example.pancharm.entity.Company;
import com.example.pancharm.entity.CompanyInfos;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CompanyInfoMapper;
import com.example.pancharm.repository.CompanyInfoRepository;
import com.example.pancharm.repository.CompanyRepository;
import com.example.pancharm.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.mapstruct.MappingTarget;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyInfoService {
	CompanyInfoRepository companyInfoRepository;
	CompanyInfoMapper companyInfoMapper;
	CompanyRepository companyRepository;
	UserRepository userRepository;

	/**
	 * @desc Create new company info
	 * @param request
	 * @return CompanyInfoResponse
	 */
	@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
	public CompanyInfoResponse createCompanyInfo(CompanyInfoRequest request) {
		var companyInfo = companyInfoMapper.toCompanyInfos(request);
		var company = companyRepository.findById(String.valueOf(1)).orElseThrow(
				() -> new AppException(ErrorCode.COMPANY_NOT_FOUND)
		);
		companyInfo.setCompany(company);

		var user = userRepository.findById(String.valueOf(request.getUserId())).orElseThrow(
				() -> new AppException(ErrorCode.USER_NOT_FOUND)
		);
		companyInfo.setPersonInCharge(user);

		try {
			companyInfo = companyInfoRepository.save(companyInfo);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return companyInfoMapper.toCompanyInfoResponse(companyInfo);
	}

	/**
	 * @desc Update existing company info
	 * @param request
	 * @param companyInfoId
	 * @return CompanyInfoResponse
	 */
	@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
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

	/**
	 * @desc Delete existing Company Info
	 * @param companyInfoId
	 */
	@PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
	public void deleteCompanyInfo(int companyInfoId){
		if (!companyInfoRepository.existsById(String.valueOf(companyInfoId))) {
			return;
		}
		companyInfoRepository.deleteById(String.valueOf(companyInfoId));
	}

	/**
	 * @desc Get company info by ID
	 * @param companyInfoId
	 * @return CompanyInfoResponse
	 */
	public CompanyInfoResponse getCompanyInfo(int companyInfoId){
		var companyInfo = companyInfoRepository.findById(String.valueOf(companyInfoId)).orElseThrow(
				() -> new AppException(ErrorCode.COMPANY_INFO_NOT_FOUND)
		);

		return companyInfoMapper.toCompanyInfoResponse(companyInfo);
	}

	/**
	 * @desc Get all company info
	 * @return List<CompanyInfoResponse>
	 */
	public List<CompanyInfoResponse> getCompanyInfos(){
		return companyInfoRepository.findAll().stream().map(companyInfoMapper::toCompanyInfoResponse).toList();
	}
}
