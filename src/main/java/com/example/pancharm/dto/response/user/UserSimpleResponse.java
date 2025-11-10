package com.example.pancharm.dto.response.user;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserSimpleResponse {
    int id;
    String username;
    String email;
    String fullname;
    String phone;
    short softDeleted;
}
