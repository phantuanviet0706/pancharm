package com.example.pancharm.configuration;

import java.util.HashSet;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.constant.UserStatus;
import com.example.pancharm.entity.Company;
import com.example.pancharm.entity.CompanyInfos;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.CompanyInfoRepository;
import com.example.pancharm.repository.CompanyRepository;
import com.example.pancharm.repository.RoleRepository;
import com.example.pancharm.repository.UserRepository;

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

    @NonFinal
    @Value("${appInfo.masterUsername}")
    static String SUPER_ADMIN_USERNAME = "admin";

    @NonFinal
    static String SUPER_ADMIN_PASSWORD = "admin";

    @NonFinal
    static String COMPANY_NAME = "Pancharm";

    @Bean
    @ConditionalOnProperty(
            prefix = "spring",
            value = "datasource.driver-class-name",
            havingValue = "com.mysql.cj.jdbc.Driver")
    ApplicationRunner applicationRunner(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CompanyRepository companyRepository,
            CompanyInfoRepository companyInfoRepository) {
        return args -> {
            Users user = null;
            boolean isEmptyUser =
                    userRepository.findByUsername(SUPER_ADMIN_USERNAME).isEmpty();
            if (isEmptyUser) {
                Roles superAdminRole = roleRepository.save(Roles.builder()
                        .name(PredefineRole.SUPER_ADMIN.getName())
                        .description(PredefineRole.SUPER_ADMIN.getDescription())
                        .build());

                roleRepository.save(Roles.builder()
                        .name(PredefineRole.ADMIN.getName())
                        .description(PredefineRole.ADMIN.getDescription())
                        .build());

                roleRepository.save(Roles.builder()
                        .name(PredefineRole.USER.getName())
                        .description(PredefineRole.USER.getDescription())
                        .build());

                var roles = new HashSet<Roles>();
                roles.add(superAdminRole);

                user = Users.builder()
                        .username(SUPER_ADMIN_USERNAME)
                        .password(passwordEncoder.encode(SUPER_ADMIN_PASSWORD))
                        .status(UserStatus.ACTIVE)
                        .roles(roles)
                        .build();

                userRepository.save(user);
                log.warn("The super admin user has been created with default password: admin, please change it");
            }

            var companies = companyRepository.findAll();
            if (companies.isEmpty()) {
                if (isEmptyUser) {
                    user = userRepository
                            .findByUsername(SUPER_ADMIN_USERNAME)
                            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
                }

                Company company = Company.builder().name(COMPANY_NAME).build();

                try {
                    company = companyRepository.save(company);
                } catch (DataIntegrityViolationException exception) {
                    throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
                }

                CompanyInfos companyInfos = CompanyInfos.builder()
                        .company(company)
                        .personInCharge(user)
                        .build();

                companyInfoRepository.save(companyInfos);
                log.warn("Company Infos has been created successfully. You can now add or edit the company info");
            }

            log.warn("Application initialization completed .......");
        };
    }
}
