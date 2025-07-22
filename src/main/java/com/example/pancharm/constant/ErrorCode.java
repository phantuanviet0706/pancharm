package com.example.pancharm.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
	UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
	INVALID_KEY(1000, "Uncategorized error", HttpStatus.BAD_REQUEST),

	PERMISSION_EXISTED(1001, "Permission already exists", HttpStatus.BAD_REQUEST),
	PERMISSION_NOT_FOUND(1001, "Permission not found", HttpStatus.NOT_FOUND),
	ROLE_EXISTED(1002, "Role already exists", HttpStatus.BAD_REQUEST),
	ROLE_NOT_FOUND(1002, "Role not found", HttpStatus.NOT_FOUND),

	USER_EXISTED(1003, "User already exists", HttpStatus.BAD_REQUEST),
	USER_NOT_FOUND(1003, "User not found", HttpStatus.NOT_FOUND),
	USER_EMAIL_EXISTED(1003, "This email has already registered, please check and try again", HttpStatus.BAD_REQUEST),
	INVALID_TOKEN(1003, "Invalid token", HttpStatus.BAD_REQUEST),
	TOKEN_EXPIRED(1003, "Token is expired", HttpStatus.BAD_REQUEST),

	COMPANY_NOT_FOUND(1004, "Company not found", HttpStatus.NOT_FOUND),
	COMPANY_INFO_NOT_FOUND(1004, "Cannot found company info", HttpStatus.NOT_FOUND),

	CATEGORY_NOT_FOUND(1005, "Category not found", HttpStatus.NOT_FOUND),

	SEND_EMAIL_ERROR(1997, "Failed to send email", HttpStatus.BAD_REQUEST),
	SEND_EMAIL_SENDGRID_ERROR(1997, "Error sending email with SendGrid", HttpStatus.BAD_REQUEST),
	UPDATE_ERROR(1998, "Cannot update object, please check and try again", HttpStatus.BAD_REQUEST),
	UNAUTHENTICATED(1999, "Unauthenticated", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1999, "You do not have permission", HttpStatus.FORBIDDEN),
	;

	int code;
	String message;
	HttpStatusCode statusCode;
}
