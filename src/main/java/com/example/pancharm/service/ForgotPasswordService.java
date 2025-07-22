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
	static String subjectResetPwd = "Reset Your Pancharm Password \uD83D\uDD12";

	@NonFinal
	static String bodyResetPwd = "<html>\n" +
			"<body style=\"font-family: Arial, sans-serif; line-height: 1.6;\">\n" +
			"    <h2>Forgot Your Password?</h2>\n" +
			"    <p>We received a request to reset the password for your Pancharm account.</p>\n" +
			"    <p>Click the link below to choose a new password:</p>\n" +
			"    <p style=\"margin: 24px 0;\">\n" +
			"        <a href=\""
			+ resetPasswordLink +
			"\" style=\"padding: 12px 20px; background-color: #F59E0B; color: white; text-decoration: none; border-radius: 6px;\">Reset My Password</a>\n" +
			"    </p>\n" +
			"    <p>If you didn’t request a password reset, no action is needed — your password remains secure.</p>\n" +
			"    <br>\n" +
			"    <p>With care,</p>\n" +
			"    <p><strong>Pancharm Support Team</strong></p>\n" +
			"    <hr>\n" +
			"    <p style=\"font-size: 12px; color: #777;\">Connect with us: <a href=\"https://www.facebook.com/pancharm.official\">facebook.com/pancharm.official</a></p>\n" +
			"</body>\n" +
			"</html>";

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
