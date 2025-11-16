package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.pancharm.dto.request.footer.FooterGroupCreationRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.footer.FooterGroupResponse;
import com.example.pancharm.service.footer.FooterGroupService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/footer-groups")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FooterGroupController {
    FooterGroupService footerGroupService;

    public ApiResponse<FooterGroupResponse> createFooterGroup(@RequestBody @Valid FooterGroupCreationRequest request) {
        return ApiResponse.<FooterGroupResponse>builder()
                .result(footerGroupService.create(request))
                .build();
    }
}
