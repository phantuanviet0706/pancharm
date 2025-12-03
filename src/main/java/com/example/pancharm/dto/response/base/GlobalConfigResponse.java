package com.example.pancharm.dto.response.base;

import com.example.pancharm.dto.response.category.CategoryDetailResponse;
import com.example.pancharm.dto.response.company.CompanyResponse;
import com.example.pancharm.entity.Configurations;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GlobalConfigResponse {
    CompanyResponse company;
    Configurations configuration;
    List<CategoryDetailResponse> categories;
}
