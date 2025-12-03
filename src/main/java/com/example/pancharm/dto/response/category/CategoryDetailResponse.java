package com.example.pancharm.dto.response.category;

import java.util.Set;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryDetailResponse {
    int id;
    String name;
    String slug;
    int parentId;
    String parentName;
    String config;
    short isDefault;
    Set<CategoryDetailResponse> categories;
}
