package com.example.pancharm.entity;

import java.util.Set;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Getter
@Setter
@Builder
@Slf4j
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Company extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @NotBlank(message = "COMPANY_NAME_REQUIRED")
    @Column(unique = true)
    String name;

    String address;

    @Column(columnDefinition = "TEXT")
    String avatar;

    @Column(length = 63, unique = true)
    String taxcode;

    String email;

    @Column(length = 35)
    String phone;

    @Column(name = "bank_attachment", columnDefinition = "TEXT")
    String bankAttachment;

    @Column(name = "bank_config", columnDefinition = "TEXT")
    String bankConfig;

    @Column(columnDefinition = "TEXT")
    String config;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    Set<CompanyInfos> companyInfos;
}
