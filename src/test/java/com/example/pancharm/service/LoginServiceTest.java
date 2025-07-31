package com.example.pancharm.service;

import static org.junit.jupiter.api.Assertions.*;

import com.example.pancharm.dto.request.auth.AuthenticationRequest;
import com.example.pancharm.dto.response.auth.AuthenticationResponse;
import com.example.pancharm.service.auth.AuthenticationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.auth.RegisterRequest;
import com.example.pancharm.dto.response.auth.RegisterResponse;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.RegisterMapper;
import com.example.pancharm.repository.UserRepository;
import com.example.pancharm.service.auth.RegisterService;
import com.example.pancharm.service.base.EmailService;

import java.util.Optional;

//@DataJpaTest
@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private UserRepository userRepository;

    @Test
    void LoginSuccess() {
        String username = "user1";
        String rawPassword = "password123";
        String encodedPassword = new BCryptPasswordEncoder(10).encode(rawPassword);
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(encodedPassword);

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(rawPassword);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        AuthenticationResponse response = authenticationService.authenticate(request);

        assertNotNull(response);
        assertTrue(response.isAuthenticated());
        assertNotNull(response.getToken());
        assertFalse(response.getToken().isEmpty());

        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void LoginUserDoesNotExist() {
        String username = "nonexist";
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword("Password");

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals(ErrorCode.USER_NOT_FOUND, ex.getErrorCode());
        Mockito.verify(userRepository).findByUsername(username);
    }

    @Test
    void LoginPasswordIncorrect() {
        String username = "user1";
        String rawPassword = "wrongPassword";
        String correctEncodedPassword = new BCryptPasswordEncoder(10).encode("correctPassword");
        Users user = new Users();
        user.setUsername(username);
        user.setPassword(correctEncodedPassword);

        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername(username);
        request.setPassword(rawPassword);

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        AppException ex = assertThrows(AppException.class, () -> {
            authenticationService.authenticate(request);
        });

        assertEquals(ErrorCode.UNAUTHENTICATED, ex.getErrorCode());
        Mockito.verify(userRepository).findByUsername(username);
    }
}
