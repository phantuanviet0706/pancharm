package com.example.pancharm.dto.request.company;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

import org.springframework.web.multipart.MultipartFile;

import com.example.pancharm.validator.annotation.PhoneConstraint;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyRequest {
    String address;
    String avatar;
    MultipartFile avatarFile;

    @Size(min = 10, max = 30)
    String taxcode;

    @Email
    String email;

    @PhoneConstraint
    String phone;

    String bankAttachment;
    MultipartFile bankAttachmentFile;

    String bankName;
    String bankAccountHolder;
    String bankNumber;
}
