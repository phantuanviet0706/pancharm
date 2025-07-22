package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.auth.RegisterRequest;
import com.example.pancharm.dto.response.RegisterResponse;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.RegisterMapper;
import com.example.pancharm.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RegisterService {
	UserRepository userRepository;
	RegisterMapper registerMapper;
	PasswordEncoder passwordEncoder;
	EmailService emailService;

	@NonFinal
	@Value("${email.verificationLink}")
	static String verificationLink;

	@NonFinal
	static String subjectVerification = "Verify your account";

	@NonFinal
	static String bodyVerification = "Please click this link to verify your account: <a href='" + "https://facebook.com" + "'>Verify</a>";

	public RegisterResponse register(@RequestBody RegisterRequest registerRequest) {
		if (userRepository.existsByUsername(registerRequest.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}

		if (userRepository.existsByEmail(registerRequest.getEmail())) {
			throw new AppException(ErrorCode.USER_EMAIL_EXISTED);
		}

		var user = registerMapper.toRegisterUser(registerRequest);
		user.setFullname(registerRequest.getFirstName() + " " + registerRequest.getLastName());
		user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

		try {
			user = userRepository.save(user);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		emailService.sendEmail(user.getEmail(), subjectVerification, bodyVerification);

		return registerMapper.toRegisterResponse(user);
	}
}
