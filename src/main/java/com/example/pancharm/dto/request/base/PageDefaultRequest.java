package com.example.pancharm.dto.request.base;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageDefaultRequest {
    String search;
    int page = 0;
    int limit = 50;
    String sortBy = "id";
    String sortDirection = "asc";
}
