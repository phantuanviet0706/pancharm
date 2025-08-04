package com.example.pancharm.service.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.constant.PredefineRole;
import com.example.pancharm.dto.request.user.*;
import com.example.pancharm.dto.request.user.UserFilterRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.user.UserDetailResponse;
import com.example.pancharm.dto.response.user.UserListResponse;
import com.example.pancharm.entity.Roles;
import com.example.pancharm.entity.Users;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.mapper.UserMapper;
import com.example.pancharm.repository.RoleRepository;
import com.example.pancharm.repository.UserRepository;
import com.example.pancharm.util.PageRequestUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    PasswordEncoder passwordEncoder;
    RoleRepository roleRepository;
    PageMapper pageMapper;

    /**
     * @desc Create new user - by Super Admin role
     * @param request
     * @return UserDetailResponse
     */
    public UserDetailResponse createUser(UserCreationRequest request) {
        Users user = userMapper.toUsers(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<Roles> roles = new HashSet<>();
        roleRepository.findById(PredefineRole.USER.getName()).ifPresent(roles::add);

        user.setRoles(roles);

        try {
            user = userRepository.save(user);
        } catch (DataIntegrityViolationException exception) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        return userMapper.toUserResponse(user);
    }

    /**
     * @desc Update existing user
     * @param request
     * @param id
     * @return UserDetailResponse
     */
    public UserDetailResponse updateUser(UserUpdateRequest request, int id) {
        Users user = userRepository
                .findById(String.valueOf(id))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

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

    /**
     * @desc Get all users
     * @param request
     * @return PageResponse<UserListResponse>
     */
    public PageResponse<UserListResponse> getUsers(UserFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Users> spec = (root, query, criteriaBuilder) -> criteriaBuilder.conjunction();

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("username").as(String.class), "%" + request.getKeyword() + "%")));
        }

        if (request.getEmail() != null && !request.getEmail().isEmpty()) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("email").as(String.class), "%" + request.getEmail() + "%")));
        }

        return pageMapper.toPageResponse(userRepository.findAll(spec, pageable).map(userMapper::toUserListResponse));
    }

    /**
     * @desc Get single user's information
     * @param id
     * @return Users
     */
    public Users getUser(int id) {
        return userRepository
                .findById(String.valueOf(id))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
    }

    /**
     * @desc Delete existing user
     * @param id
     */
    public void deleteUser(int id) {
        Users user = userRepository
                .findById(String.valueOf(id))
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));
        if (user.getRoles().contains(PredefineRole.SUPER_ADMIN)) {
            throw new AppException(ErrorCode.MASTER_USER_DELETE_ERROR);
        }

        userRepository.deleteById(String.valueOf(id));
    }

    /**
     * @desc Set roles for specific user
     * @param user
     * @param roleNames
     */
    private void setRoles(Users user, Set<String> roleNames) {
        var roles = roleRepository.findAllByNameIn(roleNames);
        user.setRoles(new HashSet<>(roles));
    }
}
