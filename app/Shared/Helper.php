<?php

    namespace App\Shared;

    use Illuminate\Validation\ValidationException;
    use Str;

    class Helper {
        public static $FAILED_CODE = 0;
        public static $SUCCESS_CODE = 1;

        /**
         * Check if a string is empty
         * @param mixed $str
         * @return bool
         */
        public static function isEmpty($str) {
            return empty(trim($str));
        }

        /**
         * Check if a string is a valid email address
         * @param mixed $str
         */
        public static function isEmail($str) {
            return filter_var($str, FILTER_VALIDATE_EMAIL);
        }

        /**
         * Check if a string is a valid phone number
         * @param mixed $str
         * @return bool|int
         */
        public static function isPhone($str) {
            return preg_match('/^\+?[0-9]{10,15}$/', $str);
        }

        /**
         * Check if a string is a valid integer
         * @param mixed $str
         */
        public static function isInteger($str) {
            return filter_var($str, FILTER_VALIDATE_INT);
        }

        /**
         * Check if a string is a valid number
         * @param mixed $str
         */
        public static function isNumber($str) {
            return filter_var($str, FILTER_VALIDATE_FLOAT);
        }

        /**
         * Release an object response
         * @param string $msg
         * @param bool $code
         * @param mixed $data
         * @return object (code, message, data)
         */
        public static function release(string $msg, bool $code = false, $data = null) {
            if ($code) {
                return (object) [
                    'code' => $code,
                    'message' => $msg,
                    "data" => $data
                ];
            }

            return (object) [
                'code' => 0,
                'message' => $msg ?? Translator::trans("The system occurs an error, please try again"),
                "data" => $data
            ];
        }

        public static function thrownExceptionValidator($field, $msg) {
            throw ValidationException::withMessages([
                $field => $msg
            ]);
        }

        public static function isLimitContent($str, $limit_from = 0, $limit_to = 255) {
            $str_len = Str::length($str);
            if ($limit_to == 0) {
                if ($str_len < $limit_from) return false;
                return true;
            }
            if ($str_len < $limit_from || $str_len > $limit_to) {
                return false;
            }
            return true;
        }

        public static function inset($var, ...$values) {
            return in_array($var, $values);
        }
    }

?>