package com.example.pancharm.service.company;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.company.CompanyRequest;
import com.example.pancharm.dto.response.company.CompanyResponse;
import com.example.pancharm.entity.Company;
import com.example.pancharm.entity.CompanyInfos;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CompanyMapper;
import com.example.pancharm.repository.CompanyInfoRepository;
import com.example.pancharm.repository.CompanyRepository;
import com.example.pancharm.repository.UserRepository;
import com.example.pancharm.util.ImageUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyService {
    CompanyRepository companyRepository;
    CompanyMapper companyMapper;

    UserRepository userRepository;
    CompanyInfoRepository companyInfoRepository;

    ImageUtil imageUtil;

    @NonFinal
    @Value("${spring.application.name}")
    static String COMPANY_NAME;

    @NonFinal
    @Value("${appInfo.masterUsername}")
    static String SUPER_ADMIN_USERNAME;

    /**
     * @desc Update existing company
     * @param companyRequest
     * @return CompanyResponse
     */
    @PreAuthorize("hasRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name())")
    public CompanyResponse updateCompany(CompanyRequest companyRequest) {
        Company company = companyRepository.findAll().getFirst();
        if (company == null) {
            throw new AppException(ErrorCode.COMPANY_NOT_FOUND);
        }

        companyMapper.updateCompany(companyRequest, company);

        if (companyRequest.getAvatarFile() != null
                && !companyRequest.getAvatarFile().isEmpty()) {
            imageUtil.upsertSingleFile(
                    companyRequest.getAvatarFile(),
                    company,
                    c -> "companies/" + c.getId() + "/avatar",
                    company::getAvatar,
                    company::setAvatar,
                    false);
        }

        if (companyRequest.getBankAttachmentFile() != null
                && !companyRequest.getBankAttachmentFile().isEmpty()) {
            imageUtil.upsertSingleFile(
                    companyRequest.getBankAttachmentFile(),
                    company,
                    c -> "companies/" + c.getId() + "/bankAttachment",
                    company::getBankAttachment,
                    company::setBankAttachment,
                    false);
        }

        try {
            company = companyRepository.save(company);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UPDATE_ERROR);
        }

        return companyMapper.toCompanyResponse(company);
    }

    /**
     * @desc Get Default Company
     * @return CompanyResponse
     */
    public CompanyResponse getCompany() {
        var company = companyRepository.findAll().getFirst();
        if (company != null) {
            return companyMapper.toCompanyResponse(company);
        }

        var user = userRepository
                .findByUsername(SUPER_ADMIN_USERNAME)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        return companyMapper.toCompanyResponse(initCompany(user));
    }

    public Company initCompany(Users user) {
        Company company = Company.builder().name(COMPANY_NAME).build();

        try {
            company = companyRepository.save(company);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        CompanyInfos companyInfos =
                CompanyInfos.builder().company(company).personInCharge(user).build();

        companyInfoRepository.save(companyInfos);

        return company;
    }
}
