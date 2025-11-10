package com.example.pancharm.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.GlobalConfigResponse;
import com.example.pancharm.service.base.ConfigService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/config")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ConfigController {
    ConfigService configService;

    @GetMapping
    public ApiResponse<GlobalConfigResponse> getGlobalConfig() {
        return ApiResponse.<GlobalConfigResponse>builder()
                .result(configService.getGlobalConfig())
                .build();
    }
}
