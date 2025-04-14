<?php
    namespace App\Shared;

    use APP\Models\Company;

    class Translator {
        private static $LANG_ENG = 'en';
        private static $LANG_VI = 'vi';
        protected static $dictionary_message = [
            "Successfully Updated" => "Đã cập nhật thành công", 
            "Please fill username" => "Vui lòng điền tên đăng nhập",
            "Invalid user, please check and try again" => "Người dùng không hợp lệ, vui lòng kiểm tra và thử lại",
            "User is deleted" => "Người dùng đã bị xóa",
            "User is inactive" => "Người dùng đã bị vô hiệu hóa",
            "Please fill password" => "Vui lòng điền mật khẩu",
            "Invalid password, please check and try again" => "Mật khẩu không khớp, vui lòng kiểm tra và thử lại",
            "Login successfully" => "Đăng nhập thành công",
            "The system occurs an error, please try again later" => "Hệ thống xảy ra lỗi, vui lòng thử lại sau",
            "Username must be less than 255 characters" => "Tên đăng nhập phải ít hơn 255 ký tự",
            "Username is existed, please choose another username" => "Tên đăng nhập đã tồn tại, vui lòng chọn tên khác",
            "Password must be at least 8 characters" => "Mật khẩu phải có ít nhất 8 ký tự",
            "Password must contain at least one uppercase letter, one number, and one special character" => "Mật khẩu phải chứa ít nhất một chữ cái viết hoa, một số và một ký tự đặc biệt",
            "Please fill email" => "Vui lòng điền email",
            "Invalid email format" => "Định dạng email không hợp lệ",
            "Email must be less than 255 characters" => "Email phải ít hơn 255 ký tự",
            "Email register for user is existed, please choose another email" => "Email đã tồn tại, vui lòng chọn email khác",
            "User fullname must be less than 100 characters" => "Họ tên người dùng phải ít hơn 100 ký tự",
            "Please fill phone number" => "Vui lòng điền số điện thoại",
            "Phone number must be less than 35 characters" => "Số điện thoại phải ít hơn 35 ký tự",
            "Invalid phone number format" => "Định dạng số điện thoại không hợp lệ",
            "Invalid user role" => "Vai trò người dùng không hợp lệ",
            "Cannot create user, please check and try again" => "Không thể tạo người dùng, vui lòng kiểm tra và thử lại",
            "Successfully Created" => "Đã tạo thành công",
            "Cannot update user, please check and try again" => "Không thể cập nhật người dùng, vui lòng kiểm tra và thử lại",
            "Cannot delete user, please check and try again" => "Không thể xóa người dùng, vui lòng kiểm tra và thử lại",
            "Successfully Deleted" => "Đã xóa thành công",
            "Logout failed, please check and try again" => "Đăng xuất không thành công, vui lòng kiểm tra và thử lại",
            "Logout successfully" => "Đăng xuất thành công",
            "User has been deleted" => "Người dùng đã bị xóa",
            "Please enter your current password" => "Vui lòng nhập mật khẩu hiện tại",
            "The input password does not match with the current password, please check and try again" => "Mật khẩu không khớp với mật khẩu hiện tại, vui lòng kiểm tra và thử lại",
            "Please enter a new password" => "Vui lòng nhập mật khẩu mới",
            "New password must contain at least one uppercase letter, one number, and one special character" => "Mật khẩu mới phải chứa ít nhất một chữ cái viết hoa, một số và một ký tự đặc biệt",
            "Please confirm your new password" => "Vui lòng xác nhận mật khẩu mới",
            "Confirm password does not match new password" => "Mật khẩu xác nhận không khớp với mật khẩu mới",
        ];

        protected static $dictionary = [

        ];

        public static function trans($key) {
            $company = Company::initNewCompany();
            $lang = $company->lang;
            if ($lang == self::$LANG_ENG) {
                return $key;
            }

            if (isset(self::$dictionary[$key])) {
                return self::$dictionary[$key];
            }

            if (isset(self::$dictionary_message[$key])) {
                return self::$dictionary_message[$key];
            }
            
            return $key;
        }
    }

?>