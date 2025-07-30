package com.example.pancharm.dto.response.company;

import com.example.pancharm.entity.Company;
import com.example.pancharm.entity.Users;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyInfoResponse {
    int id;
    String address;
    String phone;
    String email;
    Users user;
    Company company;
}
