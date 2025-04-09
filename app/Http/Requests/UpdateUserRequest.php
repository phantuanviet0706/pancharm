<?php
    namespace App\Http\Requests;

    use App\Models\User;
    use App\Shared\Helper;
    use App\Shared\Translator;
    use Illuminate\Foundation\Http\FormRequest;

    class UpdateUserRequest extends FormRequest {
        private static $PATTERN_CHECK_PASSWORD = '/^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};\'":\\\\|,.<>\/?]).+$/';
        private static $PATTERN_CHECK_EMAIL = '/^[\w\.-]+@[\w\.-]+\.\w{2,}$/';

        public function authorize() {
            return true;
        }

        public function rules() {
            return [];
        }

        public function validatedResolved() {
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