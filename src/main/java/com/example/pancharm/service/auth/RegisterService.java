package com.example.pancharm.service.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.auth.RegisterRequest;
import com.example.pancharm.dto.response.auth.RegisterResponse;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.RegisterMapper;
import com.example.pancharm.repository.UserRepository;
import com.example.pancharm.service.base.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;

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
    static String subjectVerification = "Welcome to Pancharm – Your Healing Journey Begins \uD83C\uDF3F";

    @NonFinal
    static String bodyVerification = "<html>\n" + "<body style=\"font-family: Arial, sans-serif; line-height: 1.6;\">\n"
            + "    <h2>Welcome to Pancharm 🌸</h2>\n"
            + "    <p>Thank you for joining the Pancharm community.</p>\n"
            + "    <p>Your journey toward balance, energy, and inner peace begins here.</p>\n"
            + "    <p>We’re so glad to have you with us.</p>\n"
            + "    <br>\n"
            + "    <p>Warm wishes,</p>\n"
            + "    <p><strong>Pancharm Team</strong></p>\n"
            + "    <hr>\n"
            + "    <p style=\"font-size: 12px; color: #777;\">Follow us for more updates: <a href=\"https://www.facebook.com/pancharm.official\">facebook.com/pancharm.official</a></p>\n"
            + "</body>\n"
            + "</html>";

    /**
     * @desc Register new User, then send email to notify to that user
     * @param registerRequest
     * @return RegisterResponse
     */
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
