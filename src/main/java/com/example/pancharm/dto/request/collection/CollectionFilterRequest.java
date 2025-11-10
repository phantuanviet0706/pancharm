package com.example.pancharm.dto.request.collection;

import com.example.pancharm.dto.request.base.PageDefaultRequest;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CollectionFilterRequest extends PageDefaultRequest {
    String keyword;

    int isDefault;
}
