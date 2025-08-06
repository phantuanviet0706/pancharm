package com.example.pancharm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.dto.request.user.UserCreationRequest;
import com.example.pancharm.dto.request.user.UserFilterRequest;
import com.example.pancharm.dto.request.user.UserUpdateRequest;
import com.example.pancharm.dto.response.user.UserDetailResponse;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.mapper.UserMapper;
import com.example.pancharm.repository.RoleRepository;
import com.example.pancharm.repository.UserRepository;
import com.example.pancharm.service.user.UserService;
import org.apache.kafka.security.PasswordEncoder;
import org.hibernate.query.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.awt.print.Pageable;
import java.util.*;

public class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private RoleRepository roleRepository;
    @Mock private PageMapper pageMapper;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser_Success() {
        UserCreationRequest request = new UserCreationRequest();
        Users userEntity = new Users();
        userEntity.setPassword("rawPassword");
        Users savedUser = new Users();
        savedUser.setPassword("encodedPassword");

        when(userMapper.toUsers(request)).thenReturn(userEntity);
        when(roleRepository.findById("USER"))
                .thenReturn(Optional.of(
                        Roles.builder()
                                .name("USER")
                                .description("User role")
                                .permissions(Collections.emptySet())
                                .build()
                ));
        when(userRepository.save(any(Users.class))).thenReturn(savedUser);
        when(userMapper.toUserResponse(savedUser)).thenReturn(new UserDetailResponse());

        UserDetailResponse response = userService.createUser(request);

        assertNotNull(response);
        verify(userRepository).save(userEntity);
    }

    @Test
    void testCreateUser_UserExists_ThrowsException() {
        UserCreationRequest request = new UserCreationRequest();
        Users userEntity = new Users();
        userEntity.setPassword("rawPassword");
        when(userMapper.toUsers(request)).thenReturn(userEntity);
        when(roleRepository.findById("USER"))
                .thenReturn(Optional.of(
                        Roles.builder()
                                .name("USER")
                                .description("User role")
                                .permissions(Collections.emptySet())
                                .build()
                ));
        when(userRepository.save(any(Users.class))).thenThrow(DataIntegrityViolationException.class);

        AppException exception = assertThrows(AppException.class, () ->
                userService.createUser(request));

        assertEquals(ErrorCode.USER_EXISTED, exception.getErrorCode());
    }

    @Test
    void testUpdateUser_Success() {
        UserUpdateRequest request = new UserUpdateRequest();
        request.setPassword("newPass");
        request.setRoles(Set.of("USER"));

        Users existingUser = new Users();
        existingUser.setPassword("oldPass");
        existingUser.setRoles(new HashSet<>());

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
//        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(roleRepository.findAllByNameIn(anySet())).thenReturn(Set.of(
                Roles.builder()
                        .name("USER")
                        .description("User role")
                        .permissions(Collections.emptySet())
                        .build()
        ));

        when(userRepository.save(any(Users.class))).thenReturn(existingUser);
        doNothing().when(userMapper).updateUser(existingUser, request);
        when(userMapper.toUserResponse(existingUser)).thenReturn(new UserDetailResponse());

        UserDetailResponse response = userService.updateUser(request, 1);

        assertNotNull(response);
        verify(userRepository).save(existingUser);
        assertEquals("encodedNewPass", existingUser.getPassword());
        assertTrue(existingUser.getRoles().stream().anyMatch(r -> r.getName().equals("USER")));
    }

    @Test
    void testUpdateUser_UserNotFound_ThrowsException() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () ->
                userService.updateUser(new UserUpdateRequest(), 1));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void testUpdateUser_SaveError_ThrowsException() {
        Users existingUser = new Users();

        when(userRepository.findById("1")).thenReturn(Optional.of(existingUser));
//        when(passwordEncoder.encode(anyString())).thenReturn("encoded");
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);
//        when(roleRepository.findAllByNameIn(anySet())).thenReturn(List.of());

        AppException exception = assertThrows(AppException.class, () ->
                userService.updateUser(new UserUpdateRequest(), 1));

        assertEquals(ErrorCode.UPDATE_ERROR, exception.getErrorCode());
    }

    // --- GetUsers ---

//    @Test
//    void testGetUsers_WithFilters() {
//        UserFilterRequest request = new UserFilterRequest();
//        request.setKeyword("john");
//        request.setEmail("john@example.com");
//
//        Pageable pageable = (Pageable) PageRequest.of(0, 10);
//        Page<Users> userPage = new Page<Users>(List.of(new Users()));
//
//        when(userRepository.findAll(any(), eq(pageable))).thenReturn(userPage);
//        when(pageMapper.toPageResponse(any())).thenReturn(new PageResponse<>());
//
//        PageResponse<UserListResponse> response = userService.getUsers(request);
//
//        assertNotNull(response);
//        verify(userRepository).findAll(any(Specification.class), eq(pageable));
//    }

//    @Test
//    void testGetUsers_WithoutFilters() {
//        UserFilterRequest request = new UserFilterRequest();
//
//        Pageable pageable = PageRequest.of(0, 10);
//        Page<Users> userPage = new PageImpl<>(List.of(new Users()));
//
//        when(userRepository.findAll(any(), eq(pageable))).thenReturn(userPage);
//        when(pageMapper.toPageResponse(any())).thenReturn(new PageResponse<>());
//
//        PageResponse<UserListResponse> response = userService.getUsers(request);
//
//        assertNotNull(response);
//        verify(userRepository).findAll(any(Specification.class), eq(pageable));
//    }
//
    @Test
    void testGetUser_Success() {
        Users user = new Users();

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        Users result = userService.getUser(1);

        assertEquals(user, result);
    }

    @Test
    void testGetUser_NotFound_ThrowsException() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.getUser(1));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }

    // --- DeleteUser ---

    @Test
    void testDeleteUser_Success() {
        Users user = new Users();
        Roles role = new Roles();
        role.setName(PredefineRole.USER.getName());

        user.setRoles(Set.of(role));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));
        doNothing().when(userRepository).deleteById("1");

        userService.deleteUser(1);

        verify(userRepository).deleteById("1");
    }

    @Test
    void testDeleteUser_MasterUserDeleteError() {
        Users user = new Users();
        Roles superAdminRole = new Roles();
        superAdminRole.setName(PredefineRole.SUPER_ADMIN.getName());

        user.setRoles(Set.of(superAdminRole));

        when(userRepository.findById("1")).thenReturn(Optional.of(user));

        AppException exception = assertThrows(AppException.class, () -> userService.deleteUser(1));

        assertEquals(ErrorCode.MASTER_USER_DELETE_ERROR, exception.getErrorCode());
    }

    @Test
    void testDeleteUser_UserNotFound() {
        when(userRepository.findById("1")).thenReturn(Optional.empty());

        AppException exception = assertThrows(AppException.class, () -> userService.deleteUser(1));

        assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }
}

