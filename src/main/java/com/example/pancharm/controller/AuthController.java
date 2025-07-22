package com.example.pancharm.controller;

import com.example.pancharm.dto.request.auth.AuthenticationRequest;
import com.example.pancharm.dto.request.auth.ForgotPasswordRequest;
import com.example.pancharm.dto.request.auth.IntrospectRequest;
import com.example.pancharm.dto.request.auth.RegisterRequest;
import com.example.pancharm.dto.response.*;
import com.example.pancharm.service.AuthenticationService;
import com.example.pancharm.service.ForgotPasswordService;
import com.example.pancharm.service.RegisterService;
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
public class AuthController {
	AuthenticationService authenticationService;
	RegisterService registerService;
	ForgotPasswordService  forgotPasswordService;

	@PostMapping("/login")
	ApiResponse<AuthenticationResponse> login(@RequestBody AuthenticationRequest request) {
		return ApiResponse.<AuthenticationResponse>builder()
				.result(authenticationService.authenticate(request))
				.build();
	}

	@PostMapping("/introspect")
	ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) {
		return ApiResponse.<IntrospectResponse>builder()
				.result(authenticationService.introspect(request))
				.build();
	}

	@PostMapping("/register")
	ApiResponse<RegisterResponse> register(@RequestBody RegisterRequest request) {
		return ApiResponse.<RegisterResponse>builder()
				.result(registerService.register(request))
				.build();
	}

	@PostMapping("/forgot-password")
	ApiResponse<ForgotPasswordResponse> forgotPassword(@RequestBody ForgotPasswordRequest request) {
		return ApiResponse.<ForgotPasswordResponse>builder()
				.result(forgotPasswordService.forgotPassword(request))
				.build();
	}
}
