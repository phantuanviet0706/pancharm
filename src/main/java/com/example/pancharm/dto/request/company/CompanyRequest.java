package com.example.pancharm.dto.request.company;

import java.util.Set;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyRequest {
    @NotBlank(message = "COMPANY_NAME_REQUIRED")
    String name;

    String address;
    String avatar;

    @Size(min = 10, max = 30)
    String taxcode;

    String bankAttachment;
    String config;
    Set<String> companyInfos;
}
