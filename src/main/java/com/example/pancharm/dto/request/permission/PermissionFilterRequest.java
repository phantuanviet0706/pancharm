package com.example.pancharm.dto.request.permission;

import com.example.pancharm.dto.request.base.PageDefaultRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PermissionFilterRequest extends PageDefaultRequest {
    String keyword;
    String names;
}
