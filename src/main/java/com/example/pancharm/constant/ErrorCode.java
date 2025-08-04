package com.example.pancharm.constant;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    INVALID_KEY(1000, "Uncategorized error", HttpStatus.BAD_REQUEST),

    PERMISSION_EXISTED(1001, "Permission already exists", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1001, "Permission not found", HttpStatus.NOT_FOUND),
    PERMISSION_EMPTY(1001, "Please fill permission name", HttpStatus.BAD_REQUEST),

    ROLE_EXISTED(1002, "Role already exists", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1002, "Role not found", HttpStatus.NOT_FOUND),
    ROLE_EMPTY(1002, "Please fill role name", HttpStatus.BAD_REQUEST),
    ROLE_UPDATION_DENIED(1002, "Cannot update default role's name", HttpStatus.BAD_REQUEST),
    ROLE_DELETION_DENIED(1002, "This role cannot be deleted", HttpStatus.BAD_REQUEST),

    USER_EXISTED(1003, "User already exists", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND),
    USER_EMAIL_EXISTED(1003, "This email has already registered, please check and try again", HttpStatus.BAD_REQUEST),
    USER_NOT_NULL(1003, "User cannot be null", HttpStatus.BAD_REQUEST),
    USER_REQUIRED(1003, "Please select user", HttpStatus.BAD_REQUEST),
    USERNAME_SIZE_ERROR(1003, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    USERNAME_PATTERN_ERROR(1003, "Username can contain only letters and numbers", HttpStatus.BAD_REQUEST),
    USERNAME_EMPTY(1003, "Username cannot be empty", HttpStatus.BAD_REQUEST),
    USERNAME_REQUIRED(1003, "Please fill username", HttpStatus.BAD_REQUEST),
    MASTER_USER_DELETE_ERROR(1003, "Cannot delete super admin user", HttpStatus.BAD_REQUEST),

    EMAIL_REQUIRED(1003, "Please fill email", HttpStatus.BAD_REQUEST),

    INVALID_TOKEN(1003, "Invalid token", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1003, "Token is expired", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(1003, "Password cannot be empty", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE_ERROR(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN_ERROR(
            1003,
            "Password must include an uppercase letter, a lowercase letter, a digit, and a special character",
            HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1003, "Please fill password", HttpStatus.BAD_REQUEST),

    FIRST_NAME_REQUIRED(1003, "Please fill first name", HttpStatus.BAD_REQUEST),
    LAST_NAME_REQUIRED(1003, "Please fill last name", HttpStatus.BAD_REQUEST),

    COMPANY_ID_REQUIRED(1003, "Please select company", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FOUND(1004, "Company not found", HttpStatus.NOT_FOUND),
    COMPANY_NOT_NULL(1004, "Company cannot be null", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_REQUIRED(1004, "Please fill the company name", HttpStatus.BAD_REQUEST),

    COMPANY_INFO_NOT_FOUND(1004, "Cannot found company info", HttpStatus.NOT_FOUND),

    CATEGORY_NOT_FOUND(1005, "Category not found", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_REQUIRED(1005, "Please fill the category name", HttpStatus.BAD_REQUEST),
    INVALID_PARENT_CATEGORY(1005, "Cannot select itself as its parent.", HttpStatus.BAD_REQUEST),
    CATEGORY_DELETE_ERROR(
            1005,
            "Cannot delete this category because it has subcategories. Please delete the subcategories first.",
            HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_FOUND(1006, "Product not found", HttpStatus.NOT_FOUND),
    PRODUCT_NAME_REQUIRED(1006, "Please fill the product name", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_REQUIRED(1006, "Please fill the product quantity", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_MINIMUM(1006, "The product quantity must be greater than {min}", HttpStatus.BAD_REQUEST),
    PRODUCT_UNIT_PRICE_REQUIRED(1006, "Please fill the product price", HttpStatus.BAD_REQUEST),

    COLLECTION_NOT_FOUND(1007, "Collection not found", HttpStatus.NOT_FOUND),
    COLLECTION_NAME_REQUIRE(1007, "Please fill the collection name", HttpStatus.BAD_REQUEST),

    SLUG_EXISTED(1111, "Input slug already exists, please check and try again!", HttpStatus.BAD_REQUEST),
    SLUG_REQUIRED(1111, "Please fill the slug", HttpStatus.BAD_REQUEST),

    SEND_EMAIL_ERROR(1997, "Failed to send email", HttpStatus.BAD_REQUEST),
    SEND_EMAIL_SENDGRID_ERROR(1997, "Error sending email with SendGrid", HttpStatus.BAD_REQUEST),
    AWS_S3_UPLOAD_ERROR(1997, "Upload file to AWS Cloud S3 failed", HttpStatus.BAD_REQUEST),
    AWS_S3_INVALID_URL(1997, "Invalid URL attach from AWS", HttpStatus.BAD_REQUEST),
    AWS_S3_DELETE_ERROR(1997, "Delete file from AWS Cloud S3 failed", HttpStatus.BAD_REQUEST),

    UPDATE_ERROR(1998, "Cannot update object, please check and try again", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(1999, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1999, "You do not have permission", HttpStatus.FORBIDDEN),

    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
