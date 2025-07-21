package com.example.pancharm.controller;

import com.example.pancharm.dto.request.company.CompanyRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.CompanyResponse;
import com.example.pancharm.service.CompanyService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/company")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CompanyController {
	CompanyService companyService;

	@PutMapping("/{id}")
	public ApiResponse<CompanyResponse> updateCompany(@RequestBody CompanyRequest companyRequest, @PathVariable int id) {
		return ApiResponse.<CompanyResponse>builder()
				.result(companyService.updateCompany(companyRequest, id))
				.build();
	}

	@GetMapping
	public ApiResponse<CompanyResponse> getCompany() {
		return ApiResponse.<CompanyResponse>builder()
				.result(companyService.getCompany())
				.build();
	}
}
