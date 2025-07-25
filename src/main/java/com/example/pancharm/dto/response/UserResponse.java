package com.example.pancharm.dto.response;

import java.time.LocalDate;
import java.util.Set;

import com.example.pancharm.constant.UserStatus;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {
    String username;
    String email;
    String fullname;
    LocalDate dob;
    String avatar;
    String address;
    String phone;
    UserStatus status;
    short softDeleted;
    Set<RoleResponse> roles;
    //	Set<ShippingAddresses>  shippingAddresses;
}
