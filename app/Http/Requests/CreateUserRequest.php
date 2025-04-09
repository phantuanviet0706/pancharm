<?php
    namespace App\Http\Requests;

    use App\Models\User;
    use App\Shared\Helper;
    use App\Shared\Translator;
    use Illuminate\Foundation\Http\FormRequest;

    class CreateUserRequest extends FormRequest {
        private static $PATTERN_CHECK_PASSWORD = '/^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};\'":\\\\|,.<>\/?]).+$/';
        private static $PATTERN_CHECK_EMAIL = '/^[\w\.-]+@[\w\.-]+\.\w{2,}$/';

        public function authorize() {
            return true;
        }

        public function rules() {
            return [];
        }

        public function validatedResolved() {
            // Validate username
            $username = $this->input('username');
            if (Helper::isEmpty($username)) {
                return Helper::thrownExceptionValidator('username', Translator::trans("Please fill username"));
            }

            if (Helper::isLimitContent($username)) {
                return Helper::thrownExceptionValidator("username", Translator::trans("Username must be less than 255 characters"));
            }

            $user = User::where('username', $username)
                    ->where('soft_delete', 0)
                    ->first();
            if ($user) {
                return Helper::thrownExceptionValidator('username', Translator::trans('Username is existed, please choose another username'));
            }

            // Validate password
            $password = $this->input('password');
            if (Helper::isEmpty($password)) {
                return Helper::thrownExceptionValidator('password', Translator::trans('Please fill password'));
            }
            if (Helper::isLimitContent($password, 8, 0)) {
                return Helper::thrownExceptionValidator('password', Translator::trans('Password must be at least 8 characters'));
            }
            if (!preg_match(self::$PATTERN_CHECK_PASSWORD, $password)) {
                return Helper::thrownExceptionValidator('password', Translator::trans('Password must contain at least one uppercase letter, one number, and one special character'));
            }

            // Validate email
            $email = $this->input('email');
            if (Helper::isEmpty($email)) {
                return Helper::thrownExceptionValidator('email', Translator::trans('Please fill email'));
            }
            if (!Helper::isEmail($email)) {
                return Helper::thrownExceptionValidator('email', Translator::trans('Invalid email format'));
            }
            if (Helper::isLimitContent($email)) {
                return Helper::thrownExceptionValidator('email', Translator::trans('Email must be less than 255 characters'));
            }

            $user = User::where('email', $email)
                    ->where('soft_delete', 0)
                    ->first();
            if ($user) {
                return Helper::thrownExceptionValidator('email', Translator::trans('Email register for user is existed, please choose another email'));
            }
            
            // Validate fullname
            $fullname = $this->input('fullname');
            if (!Helper::isEmpty($fullname) && Helper::isLimitContent($fullname, 0, 100)) {
                return Helper::thrownExceptionValidator('fullname', Translator::trans('User fullname must be less than 100 characters'));
            }

            $avatar = $this->input('avatar');
            $address = $this->input('address');

            // Validate phone
            $phone = $this->input('phone');
            if (!Helper::isEmpty($phone)) {
                if (Helper::isLimitContent($phone, 0, 35)) {
                    return Helper::thrownExceptionValidator('phone', Translator::trans('Phone number must be less than 35 characters'));
                }
                if (Helper::isPhone($phone)) {
                    return Helper::thrownExceptionValidator('phone', Translator::trans('Invalid phone number format'));
                }
            }

            // Validate user role
            $role = $this->input('role');
            if (!Helper::inset($role, User::$ROLE_USER, User::$ROLE_ADMIN, User::$ROLE_SUPER_ADMIN)) {
                return Helper::thrownExceptionValidator('role', Translator::trans('Invalid user role'));
            }

            $this->merge(
                [
                    'username' => $username,
                    'password' => $password,
                    'email' => $email,
                    'fullname' => $fullname,
                    'avatar' => $avatar,
                    'address' => $address,
                    'phone' => $phone,
                    'role' => $role,
                ]
            );
        }
    }

?>