 package com.example.pancharm.service;

 import static org.junit.jupiter.api.Assertions.*;

 import org.junit.jupiter.api.Assertions;
 import org.junit.jupiter.api.Test;
 import org.junit.jupiter.api.extension.ExtendWith;
 import org.mockito.InjectMocks;
 import org.mockito.Mock;
 import org.mockito.Mockito;
 import org.mockito.junit.jupiter.MockitoExtension;
 import org.springframework.dao.DataIntegrityViolationException;
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

 @ExtendWith(MockitoExtension.class)
 class RegisterServiceTest {
    String subjectVerification = "Welcome to Pancharm – Your Healing Journey Begins \uD83C\uDF3F";
    String bodyVerification = "<html>...</html>";

    @InjectMocks
    private RegisterService registerService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RegisterMapper registerMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @Test
    void registerSuccessfully() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setEmail("email@example.com");
        request.setFirstName("John");
        request.setPhoneNumber("0982231418");
        request.setLastName("Doe");
        request.setPassword("password");

        Users savedUser = new Users();
        savedUser.setId(3);
        savedUser.setUsername("newUser");
        savedUser.setEmail("email@example.com");
        savedUser.setAddress("VN");
        savedUser.setFullname("John Doe");
        savedUser.setPassword("encodedPassword");

        RegisterResponse response = new RegisterResponse();
        response.setEmail("email@example.com");

        Mockito.when(userRepository.existsByUsername("newUser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        Mockito.when(registerMapper.toRegisterUser(request)).thenReturn(savedUser);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(savedUser)).thenReturn(savedUser);
        Mockito.when(registerMapper.toRegisterResponse(savedUser)).thenReturn(response);

        RegisterResponse result = registerService.register(request);

        assertNotNull(result);
        assertEquals("email@example.com", result.getEmail());

        Mockito.verify(userRepository).existsByUsername("newUser");
        Mockito.verify(userRepository).existsByEmail("email@example.com");
        Mockito.verify(registerMapper).toRegisterUser(request);
        Mockito.verify(passwordEncoder).encode("password");
        Mockito.verify(userRepository).save(savedUser);
        Mockito.verify(emailService).sendEmail(savedUser.getEmail(), subjectVerification, bodyVerification);
        Mockito.verify(registerMapper).toRegisterResponse(savedUser);
    }

    @Test
    void RegisterWhenUsernameExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("existingUser");

        Mockito.when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            registerService.register(request);
        });
        assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());

        Mockito.verify(userRepository).existsByUsername("existingUser");
        Mockito.verifyNoMoreInteractions(userRepository, registerMapper, passwordEncoder, emailService);
    }

    @Test
    void RegisterWhenEmailExists() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setEmail("existingemail@example.com");

        Mockito.when(userRepository.existsByUsername("newUser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("existingemail@example.com")).thenReturn(true);

        AppException exception = assertThrows(AppException.class, () -> {
            registerService.register(request);
        });
        assertEquals(ErrorCode.USER_EMAIL_EXISTED, exception.getErrorCode());

        Mockito.verify(userRepository).existsByUsername("newUser");
        Mockito.verify(userRepository).existsByEmail("existingemail@example.com");
        Mockito.verifyNoMoreInteractions(userRepository, registerMapper, passwordEncoder, emailService);
    }

    @Test
    public void register_shouldThrowException_whenSaveFails() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("newUser");
        request.setEmail("email@example.com");
        request.setFirstName("John");
        request.setLastName("Doe");
        request.setPassword("password");

        Users userToSave = new Users();

        Mockito.when(userRepository.existsByUsername("newUser")).thenReturn(false);
        Mockito.when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        Mockito.when(registerMapper.toRegisterUser(request)).thenReturn(userToSave);
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(userToSave)).thenThrow(new DataIntegrityViolationException(""));

        AppException exception = Assertions.assertThrows(AppException.class, () -> {
            registerService.register(request);
        });

        Assertions.assertEquals(ErrorCode.UPDATE_ERROR, exception.getErrorCode());
        Mockito.verify(userRepository).existsByUsername("newUser");
        Mockito.verify(userRepository).existsByEmail("email@example.com");
        Mockito.verify(registerMapper).toRegisterUser(request);
        Mockito.verify(passwordEncoder).encode("password");
        Mockito.verify(userRepository).save(userToSave);
        Mockito.verifyNoMoreInteractions(emailService);
    }
 }
