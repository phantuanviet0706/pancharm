package com.example.pancharm.service;

import com.example.pancharm.dto.request.user.UserCreationRequest;
import com.example.pancharm.dto.request.user.UserUpdateRequest;
import com.example.pancharm.dto.response.UserResponse;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.entity.Users;
import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.UserMapper;
import com.example.pancharm.repository.RoleRepository;
import com.example.pancharm.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
	UserRepository userRepository;
	UserMapper userMapper;

	PasswordEncoder passwordEncoder;
	private final RoleRepository roleRepository;

	public UserResponse createUser(UserCreationRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}
		Users user = userMapper.toUsers(request);
		user.setPassword(passwordEncoder.encode(user.getPassword()));

		try {
			user = userRepository.save(user);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.USER_EXISTED);
		}

		return userMapper.toUserResponse(user);
	}

	public UserResponse updateUser(UserUpdateRequest request, int id) {
		Users user = userRepository.findById(String.valueOf(id)).orElseThrow(() ->
				new AppException(ErrorCode.USER_NOT_FOUND));

		userMapper.updateUser(user, request);
		user.setPassword(passwordEncoder.encode(request.getPassword()));

		setRoles(user, request.getRoles());

		try {
			user = userRepository.save(user);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return userMapper.toUserResponse(user);
	}

	public List<UserResponse> getUsers() {
		return userRepository.findAll().stream().map(userMapper::toUserResponse).toList();
	}

	public Users getUser(int id) {
		return userRepository.findById(String.valueOf(id))
				.orElseThrow(() -> {
					return new AppException(ErrorCode.USER_NOT_FOUND);
				});
	}

	public void deleteUser(int id) {
		userRepository.deleteById(String.valueOf(id));
	}

	private void setRoles(Users user, Set<String> roleNames) {
		var roles = roleRepository.findAllByNameIn(roleNames);
		user.setRoles(new HashSet<>(roles));
	}
}
