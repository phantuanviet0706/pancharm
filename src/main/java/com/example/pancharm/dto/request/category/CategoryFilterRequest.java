package com.example.pancharm.dto.request.category;

import com.example.pancharm.dto.request.base.PageDefaultRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryFilterRequest extends PageDefaultRequest {
    String keyword;
    String slug;
}
