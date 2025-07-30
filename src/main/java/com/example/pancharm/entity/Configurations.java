package com.example.pancharm.entity;

import jakarta.persistence.*;

import com.example.pancharm.constant.ConfigurationName;
import com.example.pancharm.constant.LangConfiguration;

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
public class Configurations extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "name", nullable = false)
    ConfigurationName name = ConfigurationName.COMPANY_CONFIG;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "lang", columnDefinition = "VARCHAR(10)", nullable = false)
    LangConfiguration lang = LangConfiguration.VI;

    @Column(columnDefinition = "TEXT")
    String config;

    @Column(columnDefinition = "TEXT")
    String data;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", unique = true)
    Users user;
}
