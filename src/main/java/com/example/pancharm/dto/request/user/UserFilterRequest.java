package com.example.pancharm.dto.request.user;

import com.example.pancharm.dto.request.base.PageDefaultRequest;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserFilterRequest extends PageDefaultRequest {
    String keyword;
    String email;
}
