package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.auth.ForgotPasswordRequest;
import com.example.pancharm.dto.response.ForgotPasswordResponse;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ForgotPasswordService {
	UserRepository userRepository;
	EmailService emailService;

	@NonFinal
	static String resetPasswordLink = "https://facebook.com";

	@NonFinal
	static String subjectResetPwd = "Reset your password";

	@NonFinal
	static String bodyResetPwd = "Please click this link to reset your password: <a href='" +
			resetPasswordLink + "'>Reset your password</a>";

	public ForgotPasswordResponse forgotPassword(@RequestBody ForgotPasswordRequest request) {
		if (!userRepository.existsByEmail(request.getEmail())) {
			return ForgotPasswordResponse.builder()
					.message("An email has been sent to you email, please check it to change your password.")
					.build();
		}

		var user =  userRepository.findByEmail(request.getEmail());

		emailService.sendEmail(request.getEmail(), subjectResetPwd, bodyResetPwd);

		return ForgotPasswordResponse.builder()
				.message("An email has been sent to you email, please check it to change your password.")
				.build();
	}
}
