package com.example.pancharm.service.base;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.response.base.GlobalConfigResponse;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CompanyMapper;
import com.example.pancharm.repository.CompanyRepository;
import com.example.pancharm.repository.UserRepository;
import com.example.pancharm.service.company.CompanyService;
import com.example.pancharm.service.configuration.ConfigurationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigService {
    CompanyRepository companyRepository;
    UserRepository userRepository;
    CompanyService companyService;
    CompanyMapper companyMapper;
    ConfigurationService configurationService;

    @NonFinal
    @Value("${appInfo.masterUsername}")
    static String SUPER_ADMIN_USERNAME;

    public GlobalConfigResponse getGlobalConfig() {
        var company = companyRepository.findAll().getFirst();
        if (company == null) {
            var user = userRepository
                    .findByUsername(SUPER_ADMIN_USERNAME)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
            company = companyService.initCompany(user);
        }

        var configuration = configurationService.getCompanyConfiguration();

        return GlobalConfigResponse.builder()
                .company(companyMapper.toCompanyResponse(company))
                .configuration(configuration)
                .build();
    }
}
