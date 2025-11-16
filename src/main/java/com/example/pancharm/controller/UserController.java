package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.user.*;
import com.example.pancharm.dto.request.user.UserFilterRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.user.*;
import com.example.pancharm.service.user.UserService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    private UserService userService;

    @PostMapping
    ApiResponse<UserDetailResponse> createUser(@RequestBody @Valid UserCreationRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<UserDetailResponse> updateUser(@ModelAttribute @Valid UserUpdateRequest request, @PathVariable int id) {
        return ApiResponse.<UserDetailResponse>builder()
                .result(userService.updateUser(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    ApiResponse<PageResponse<UserListResponse>> findAllUsers(UserFilterRequest request) {
        return ApiResponse.<PageResponse<UserListResponse>>builder()
                .result(userService.getUsers(request))
                .build();
    }

    @GetMapping("/me")
    ApiResponse<UserDetailResponse> getInfo() {
        return ApiResponse.<UserDetailResponse>builder()
                .result(userService.getMyInfo())
                .build();
    }

    @PostMapping("/change-password")
    ApiResponse<UserDetailResponse> changeUserPassword(@RequestBody @Valid UserChangePassRequest request) {
        return ApiResponse.<UserDetailResponse>builder()
                .result(userService.changePassword(request))
                .build();
    }
}
