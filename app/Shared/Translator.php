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