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

	COMPANY_NOT_FOUND(1004, "Company not found", HttpStatus.NOT_FOUND),

	UPDATE_ERROR(1998, "Cannot update object, please check and try again", HttpStatus.BAD_REQUEST),
	UNAUTHENTICATED(1999, "Unauthenticated", HttpStatus.UNAUTHORIZED),
	UNAUTHORIZED(1999, "You do not have permission", HttpStatus.FORBIDDEN),
	;

	int code;
	String message;
	HttpStatusCode statusCode;
}
