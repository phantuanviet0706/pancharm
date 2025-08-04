package com.example.pancharm.dto.request.product;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductImageRequest {
	Set<MultipartFile> productImages;
}
