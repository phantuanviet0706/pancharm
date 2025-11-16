package com.example.pancharm.dto.response.company;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyResponse {
    String name;
    String address;
    String avatar;
    String phone;
    String email;
    String taxcode;
    String bankAttachment;
    String bankConfig;
    String config;
    Set<CompanyInfoResponse> companyInfos;
}
