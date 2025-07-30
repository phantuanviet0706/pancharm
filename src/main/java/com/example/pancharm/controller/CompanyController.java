package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.company.CompanyRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.company.CompanyResponse;
import com.example.pancharm.service.company.CompanyService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
    CompanyService companyService;

    @PutMapping
    public ApiResponse<CompanyResponse> updateCompany(
            @RequestBody @Valid CompanyRequest companyRequest) {
        return ApiResponse.<CompanyResponse>builder()
                .result(companyService.updateCompany(companyRequest))
                .build();
    }

    @GetMapping
    public ApiResponse<CompanyResponse> getCompany() {
        return ApiResponse.<CompanyResponse>builder()
                .result(companyService.getCompany())
                .build();
    }
}
