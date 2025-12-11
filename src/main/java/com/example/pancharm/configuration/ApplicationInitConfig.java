package com.example.pancharm.configuration;

import java.util.List;
import java.util.Set;

import org.json.JSONObject;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.constant.UserStatus;
import com.example.pancharm.entity.*;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.*;
import com.example.pancharm.util.CompanyUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApplicationInitConfig {
    PasswordEncoder passwordEncoder;

    CompanyUtil companyUtil;

    @NonFinal
    static String superAdminUsername = "admin";

    @NonFinal
    static String superAdminPassword = "admin";

    @NonFinal
    static String companyName = "Pancharm";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            CompanyInfoRepository companyInfoRepository,
            ConfigurationRepository configurationRepository) {
        return args -> {
            initRolesAndSuperAdmin(userRepository, roleRepository);
            initCompany(userRepository, companyRepository, companyInfoRepository);
            initConfigurations(configurationRepository);
            log.warn("Application initialization completed .......");
        };
    }

    @Transactional
    void initRolesAndSuperAdmin(UserRepository userRepository, RoleRepository roleRepository) {
        if (userRepository.findByUsername(superAdminUsername).isEmpty()) {
            Roles superAdminRole = roleRepository.save(Roles.builder()
                    .name(PredefineRole.SUPER_ADMIN.getName())
                    .description(PredefineRole.SUPER_ADMIN.getDescription())
                    .build());

            roleRepository.saveAll(List.of(
                    Roles.builder()
                            .name(PredefineRole.ADMIN.getName())
                            .description(PredefineRole.ADMIN.getDescription())
                            .build(),
                    Roles.builder()
                            .name(PredefineRole.USER.getName())
                            .description(PredefineRole.USER.getDescription())
                            .build()));

            Users user = Users.builder()
                    .username(superAdminUsername)
                    .password(passwordEncoder.encode(superAdminPassword))
                    .status(UserStatus.ACTIVE)
                    .roles(Set.of(superAdminRole))
                    .build();
            userRepository.save(user);

            log.warn("Super admin created with default password. Please change it immediately!");
        }
    }

    @Transactional
    void initCompany(
            UserRepository userRepository,
            CompanyRepository companyRepository,
            CompanyInfoRepository companyInfoRepository) {
        if (companyRepository.findAll().isEmpty()) {
            Users admin = userRepository
                    .findByUsername(superAdminUsername)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

            var defaultData = companyUtil.getCompanyDefaultInfo();
            var companyData = new Company();
            if (defaultData != null) {
                JSONObject companyDefaultData = defaultData.getJSONObject("company");

                companyData = Company.builder()
                        .name(companyDefaultData.getString("name"))
                        .address(companyDefaultData.getString("address"))
                        .email(companyDefaultData.getString("email"))
                        .phone(companyDefaultData.getString("phone"))
                        .taxcode(companyDefaultData.getString("tax_code"))
                        .build();
            } else {
                companyData = Company.builder().name(companyName).build();
            }

            Company company = companyRepository.save(companyData);
            companyInfoRepository.save(CompanyInfos.builder()
                    .company(company)
                    .personInCharge(admin)
                    .build());
            log.warn("Default company created. Update details as needed.");
        }
    }

    @Transactional
    void initConfigurations(ConfigurationRepository configurationRepository) {
        if (!configurationRepository.existsByName(ConfigurationName.COMPANY_CONFIG)) {
            configurationRepository.save(Configurations.builder().build());
        }
    }
}
