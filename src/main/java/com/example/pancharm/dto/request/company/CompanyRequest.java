package com.example.pancharm.dto.request.company;

import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyRequest {
	String name;
	String address;
	String avatar;

	@Size(min = 10, max = 30)
	String taxcode;

	String bankAttachment;
	String config;
	Set<String> companyInfos;
}
