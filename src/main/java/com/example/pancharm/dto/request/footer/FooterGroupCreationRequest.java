package com.example.pancharm.dto.request.footer;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FooterGroupCreationRequest {
    String title;
    String code;
    String description;
}
