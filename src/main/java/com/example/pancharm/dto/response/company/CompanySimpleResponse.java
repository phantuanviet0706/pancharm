package com.example.pancharm.dto.response.company;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanySimpleResponse {
    String name;
    String address;
    String avatar;
    String taxcode;
}
