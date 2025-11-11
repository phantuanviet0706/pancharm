package com.example.pancharm.dto.response.category;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CategoryListResponse {
    int id;
    String name;
    String slug;
    Integer parentId;
}
