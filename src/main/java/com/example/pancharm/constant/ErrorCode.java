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
    INVALID_KEY(1000, "Lỗi không xác định", HttpStatus.BAD_REQUEST),

    PERMISSION_EXISTED(1001, "Quyền đã tồn tại", HttpStatus.BAD_REQUEST),
    PERMISSION_NOT_FOUND(1001, "Không tìm thấy quyền", HttpStatus.NOT_FOUND),
    PERMISSION_EMPTY(1001, "Vui lòng nhập tên quyền", HttpStatus.BAD_REQUEST),

    ROLE_EXISTED(1002, "Vai trò đã tồn tại", HttpStatus.BAD_REQUEST),
    ROLE_NOT_FOUND(1002, "Không tìm thấy vai trò", HttpStatus.NOT_FOUND),
    ROLE_EMPTY(1002, "Vui lòng nhập tên vai trò", HttpStatus.BAD_REQUEST),
    ROLE_UPDATION_DENIED(1002, "Không thể thay đổi tên của vai trò mặc định", HttpStatus.BAD_REQUEST),
    ROLE_DELETION_DENIED(1002, "Vai trò này không thể xóa", HttpStatus.BAD_REQUEST),

    USER_EXISTED(1003, "Người dùng đã tồn tại", HttpStatus.BAD_REQUEST),
    USER_NOT_FOUND(1003, "Không tìm thấy người dùng", HttpStatus.NOT_FOUND),
    USER_EMAIL_EXISTED(1003, "Email này đã được dùng để đăng ký, vui lòng thử lại!", HttpStatus.BAD_REQUEST),
    USER_NOT_NULL(1003, "Người dùng không thể để trống", HttpStatus.BAD_REQUEST),
    USER_REQUIRED(1003, "Vui lòng chọn người dùng", HttpStatus.BAD_REQUEST),
    USERNAME_SIZE_ERROR(1003, "Tên người dùng phải chứa ít nhất {min} ký tự", HttpStatus.BAD_REQUEST),
    USERNAME_PATTERN_ERROR(1003, "Tên người dùng chỉ có thể chứa chữ và số", HttpStatus.BAD_REQUEST),
    USERNAME_EMPTY(1003, "Tên người dùng không được để trống", HttpStatus.BAD_REQUEST),
    USERNAME_REQUIRED(1003, "Vui lòng nhập tên người dùng", HttpStatus.BAD_REQUEST),
    MASTER_USER_DELETE_ERROR(1003, "Không thể xóa người dùng tối cao", HttpStatus.BAD_REQUEST),

    EMAIL_REQUIRED(1003, "Vui lòng nhập email", HttpStatus.BAD_REQUEST),

    INVALID_TOKEN(1003, "Token không hợp lệ", HttpStatus.BAD_REQUEST),
    TOKEN_EXPIRED(1003, "Token đã hết hạn", HttpStatus.BAD_REQUEST),
    PASSWORD_EMPTY(1003, "Mật khẩu không được để trống", HttpStatus.BAD_REQUEST),
    PASSWORD_SIZE_ERROR(1003, "Mật khẩu phải chứa ít nhât {min} ký tự", HttpStatus.BAD_REQUEST),
    PASSWORD_PATTERN_ERROR(
            1003,
            "Mật khẩu phải chứa ít nhất 1 ký tự in hoa, 1 ký tự in thường, số và 1 ký tự đặc biệt",
            HttpStatus.BAD_REQUEST),
    PASSWORD_REQUIRED(1003, "Vui lòng nhập mật khẩu", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_ERROR(1003, "Mật khẩu nhập lại phải trùng với mật khẩu", HttpStatus.BAD_REQUEST),

    FIRST_NAME_REQUIRED(1003, "Vui lòng nhập Họ", HttpStatus.BAD_REQUEST),
    LAST_NAME_REQUIRED(1003, "Vui lòng nhập Tên", HttpStatus.BAD_REQUEST),

    COMPANY_ID_REQUIRED(1003, "Vui lòng chọn công ty", HttpStatus.BAD_REQUEST),
    COMPANY_NOT_FOUND(1004, "Công ty không tồn tại", HttpStatus.NOT_FOUND),
    COMPANY_NOT_NULL(1004, "Công ty không thể để trống", HttpStatus.BAD_REQUEST),
    COMPANY_NAME_REQUIRED(1004, "Vui lòng nhập tên công ty", HttpStatus.BAD_REQUEST),

    COMPANY_INFO_NOT_FOUND(1004, "Không tìm thấy thông tin công ty", HttpStatus.NOT_FOUND),

    CATEGORY_NOT_FOUND(1005, "Không tìm thấy danh mục", HttpStatus.NOT_FOUND),
    CATEGORY_NAME_REQUIRED(1005, "Vui lòng nhập tên danh mục", HttpStatus.BAD_REQUEST),
    INVALID_PARENT_CATEGORY(1005, "Không thể lựa chọn bản thân làm danh mục cha của chính mình!", HttpStatus.BAD_REQUEST),
    CATEGORY_DELETE_ERROR(
            1005,
            "Không thể xóa danh mục này vì nó chứa danh mục con. Vui lòng xóa các danh mục con trước!",
            HttpStatus.BAD_REQUEST),
    CATEGORY_NAME_MAX_255(1006, "Tên danh mục chỉ có thể chứa tối đa 255 ký tự, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    CATEGORY_SLUG_MAX_255(1006, "Mã danh mục chỉ có thể chứa 63 ký tự, vui lòng thử lại", HttpStatus.BAD_REQUEST),

    PRODUCT_NOT_FOUND(1006, "Không tìm thấy sản phẩm", HttpStatus.NOT_FOUND),
    PRODUCT_NAME_REQUIRED(1006, "Vui lòng nhập tên sản phẩm", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_REQUIRED(1006, "Vui lòng nhập số lượng sản phẩm", HttpStatus.BAD_REQUEST),
    PRODUCT_QUANTITY_MINIMUM(1006, "Số lượng sản phẩm phải lớn hơn {min}", HttpStatus.BAD_REQUEST),
    PRODUCT_UNIT_PRICE_REQUIRED(1006, "Vui lòng nhập đơn giá của sản phẩm", HttpStatus.BAD_REQUEST),
    PRODUCT_DELETED(1006, "Sản phẩm đã bị xóa, vui lòng chọn sản phẩm khác", HttpStatus.BAD_REQUEST),
    PRODUCT_INVALID_STATUS(1006, "Sản phẩm không khả dụng để bán", HttpStatus.BAD_REQUEST),
    PRODUCT_OUT_OF_STOCK(1006, "Sản phẩm đã hết hàng, vui lòng chọn sản phẩm khác", HttpStatus.BAD_REQUEST),
    PRODUCT_NAME_MAX_255(1006, "Tên sản phẩm chỉ có thể chứa tối đa 255 ký tự, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    PRODUCT_SLUG_MAX_255(1006, "Mã sản phẩm chỉ có thể chứa 63 ký tự, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    PRODUCT_CATEGORY_REQUIRED(1006, "Vui lòng chọn Danh mục sản phẩm", HttpStatus.BAD_REQUEST),

    COLLECTION_NOT_FOUND(1007, "Không tìm thấy bộ sưu tập", HttpStatus.NOT_FOUND),
    COLLECTION_NAME_REQUIRE(1007, "Vui lòng nhập tên bộ sưu tập", HttpStatus.BAD_REQUEST),
    COLLECTION_NAME_MAX_255(1006, "Tên bộ sưu tập chỉ có thể chứa tối đa 255 ký tự, vui lòng thử lại", HttpStatus.BAD_REQUEST),
    COLLECTION_SLUG_MAX_255(1006, "Mã bộ sưu tập chỉ có thể chứa 63 ký tự, vui lòng thử lại", HttpStatus.BAD_REQUEST),

    ORDER_RECIPIENT_NAME_REQUIRE(1008, "Vui lòng nhập tên người mua", HttpStatus.BAD_REQUEST),
    ORDER_ADDRESS_REQUIRE(1008, "Vui lòng nhập địa chỉ", HttpStatus.BAD_REQUEST),
    ORDER_PROVINCE_REQUIRE(1008, "Vui lòng nhập thành phố", HttpStatus.BAD_REQUEST),
    ORDER_PHONE_NUMBER_REQUIRE(1008, "Vui lòng nhập số điện thoại người mua", HttpStatus.BAD_REQUEST),

    SLUG_EXISTED(1111, "Mã nhập không hợp lệ, vui lòng kiểm tra và thử lại!", HttpStatus.BAD_REQUEST),
    SLUG_REQUIRED(1111, "Vui lòng nhập mã", HttpStatus.BAD_REQUEST),

    FILE_PATH_NOT_ALLOWED(1996, "Định dạng File không hợp lệ", HttpStatus.BAD_REQUEST),
    MULTIPART_INVALID(1996, "Có lỗi xảy ra khi tải nhiều ảnh", HttpStatus.BAD_REQUEST),

    SEND_EMAIL_ERROR(1997, "Không thể gửi email", HttpStatus.BAD_REQUEST),
    SEND_EMAIL_SENDGRID_ERROR(1997, "Có lỗi xảy ra khi gửi email bằng SendGrid", HttpStatus.BAD_REQUEST),

    // AWS S3 error
    AWS_S3_UPLOAD_ERROR(1997, "Upload file to AWS Cloud S3 failed", HttpStatus.BAD_REQUEST),
    AWS_S3_INVALID_URL(1997, "Invalid URL attach from AWS", HttpStatus.BAD_REQUEST),
    AWS_S3_DELETE_ERROR(1997, "Delete file from AWS Cloud S3 failed", HttpStatus.BAD_REQUEST),

    // MinIO error
    MINIO_UPLOAD_ERROR(1997, "Tải file lên MinIO thất bại", HttpStatus.BAD_REQUEST),
    MINIO_INVALID_URL(1997, "Đường dẫn MinIO không hợp lệ", HttpStatus.BAD_REQUEST),
    MINIO_DELETE_ERROR(1997, "Không thể xóa file từ MinIO", HttpStatus.BAD_REQUEST),

    BAD_REQUEST(1998, "Lỗi hệ thống", HttpStatus.BAD_REQUEST),
    UPDATE_ERROR(1998, "Không thể cập nhật đối tượng, vui lòng kiểm tra và thử lại", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(1999, "Chưa xác minh", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1999, "Bạn không có quyền truy cập hệ thống", HttpStatus.FORBIDDEN),
    JWT_EXCEPTION(1999, "Có lỗi xảy ra khi xác thực", HttpStatus.BAD_REQUEST),

    UNCATEGORIZED_EXCEPTION(9999, "Lỗi chưa phân loại", HttpStatus.INTERNAL_SERVER_ERROR),
    ;

    int code;
    String message;
    HttpStatusCode statusCode;
}
