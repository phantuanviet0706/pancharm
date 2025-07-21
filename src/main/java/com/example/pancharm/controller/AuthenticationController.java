package com.example.pancharm.controller;

import com.example.pancharm.dto.request.AuthenticationRequest;
import com.example.pancharm.dto.request.IntrospectRequest;
import com.example.pancharm.dto.response.ApiResponse;
import com.example.pancharm.dto.response.AuthenticationResponse;
import com.example.pancharm.dto.response.IntrospectResponse;
import com.example.pancharm.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {
	AuthenticationService authenticationService;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
		var result = authenticationService.authenticate(request);

		return ApiResponse.<AuthenticationResponse>builder()
				.result(result)
				.build();
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
		var result = authenticationService.introspect(request);

		return ApiResponse.<IntrospectResponse>builder()
				.result(result)
				.build();
	}
}
