package com.example.pancharm.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public enum PredefineRole {
    SUPER_ADMIN("SUPER_ADMIN", "Super admin role - Grant all access."),
    ADMIN("ADMIN", "Admin role - Grant access from super admin, can access to admin page."),
    USER("USER", "User role - Normal User, can manage profile, view and order from website.");

    String name;
    String description;
}
