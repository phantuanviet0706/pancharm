package com.example.pancharm.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.company.CompanyInfoRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.CompanyInfoResponse;
import com.example.pancharm.service.CompanyInfoService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/company/info")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyInfoController {
    CompanyInfoService companyInfoService;

    @PostMapping
    public ApiResponse<CompanyInfoResponse> createCompanyInfo(
            @RequestBody @Valid CompanyInfoRequest companyInfoRequest) {
        return ApiResponse.<CompanyInfoResponse>builder()
                .result(companyInfoService.createCompanyInfo(companyInfoRequest))
                .build();
    }

    @PutMapping("/{id}")
    public ApiResponse<CompanyInfoResponse> updateCompanyInfo(
            @PathVariable int id, @RequestBody @Valid CompanyInfoRequest companyInfoRequest) {
        return ApiResponse.<CompanyInfoResponse>builder()
                .result(companyInfoService.updateCompanyInfo(companyInfoRequest, id))
                .build();
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Void> deleteCompanyInfo(@PathVariable int id) {
        companyInfoService.deleteCompanyInfo(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    public ApiResponse<List<CompanyInfoResponse>> getCompanyInfos() {
        return ApiResponse.<List<CompanyInfoResponse>>builder()
                .result(companyInfoService.getCompanyInfos())
                .build();
    }

    @GetMapping("/{id}")
    public ApiResponse<CompanyInfoResponse> getCompanyInfo(@PathVariable int id) {
        return ApiResponse.<CompanyInfoResponse>builder()
                .result(companyInfoService.getCompanyInfo(id))
                .build();
    }
}
