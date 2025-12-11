package com.example.pancharm.controller;

import com.example.pancharm.dto.request.configuration.ConfigurationVideoRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.company.CompanyResponse;
import com.example.pancharm.dto.response.configuration.ConfigurationResponse;
import com.example.pancharm.service.configuration.ConfigurationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/configurations")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigurationController {
    ConfigurationService configurationService;

    @GetMapping
    public ApiResponse<ConfigurationResponse> getConfiguration() {
        return ApiResponse.<ConfigurationResponse>builder()
                .result(configurationService.getConfiguration())
                .build();
    }

    @PostMapping("/{name}/update-video")
    public ApiResponse<ConfigurationResponse> updateConfigurationVideo(@PathVariable String name, @ModelAttribute MultipartFile uploadFile) {
        return ApiResponse.<ConfigurationResponse>builder()
                .result(configurationService.updateConfigSource(uploadFile, "video"))
                .build();
    }

    @PostMapping("/{name}/update-image")
    public ApiResponse<ConfigurationResponse> updateConfigurationImage(@PathVariable String name, @ModelAttribute MultipartFile uploadFile) {
        return ApiResponse.<ConfigurationResponse>builder()
                .result(configurationService.updateConfigSource(uploadFile, "image"))
                .build();
    }
}
