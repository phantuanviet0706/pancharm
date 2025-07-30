package com.example.pancharm.controller;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.*;

import com.example.pancharm.dto.request.user.UserFilterRequest;
import com.example.pancharm.dto.request.user.UserRequest;
import com.example.pancharm.dto.response.auth.ApiResponse;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.user.UserResponse;
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
    ApiResponse<UserResponse> createUser(@RequestBody @Valid UserRequest request) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.createUser(request))
                .build();
    }

    @PutMapping("/{id}")
    ApiResponse<UserResponse> updateUser(@RequestBody @Valid UserRequest request, @PathVariable int id) {
        return ApiResponse.<UserResponse>builder()
                .result(userService.updateUser(request, id))
                .build();
    }

    @DeleteMapping("/{id}")
    ApiResponse<Void> deleteUser(@PathVariable int id) {
        userService.deleteUser(id);
        return ApiResponse.<Void>builder().build();
    }

    @GetMapping
    ApiResponse<PageResponse<UserResponse>> findAllUsers(UserFilterRequest request) {
        return ApiResponse.<PageResponse<UserResponse>>builder()
                .result(userService.getUsers(request))
                .build();
    }
}
