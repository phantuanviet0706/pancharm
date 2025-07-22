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
	static String subjectVerification = "Welcome to Pancharm – Verify Your Email to Begin Your Journey \uD83C\uDF3F";

	@NonFinal
	static String bodyVerification = "<html>\n" +
			"<body style=\"font-family: Arial, sans-serif; line-height: 1.6;\">\n" +
			"    <h2>Welcome to Pancharm \uD83C\uDF38</h2>\n" +
			"    <p>Thank you for joining the Pancharm community.</p>\n" +
			"    <p>Your journey towards balance, energy, and inner peace begins here. To complete your registration, please verify your email address:</p>\n" +
			"    <p style=\"margin: 24px 0;\">\n" +
			"        <a href=\""
			+ verificationLink +
			"\" style=\"padding: 12px 20px; background-color: #8B5CF6; color: white; text-decoration: none; border-radius: 6px;\">Verify My Email</a>\n" +
			"    </p>\n" +
			"    <p>If you didn't sign up for Pancharm, you can safely ignore this message.</p>\n" +
			"    <br>\n" +
			"    <p>Warm wishes,</p>\n" +
			"    <p><strong>Pancharm Team</strong></p>\n" +
			"    <hr>\n" +
			"    <p style=\"font-size: 12px; color: #777;\">Follow us for more updates: <a href=\"https://www.facebook.com/pancharm.official\">facebook.com/pancharm.official</a></p>\n" +
			"</body>\n" +
			"</html>";

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
