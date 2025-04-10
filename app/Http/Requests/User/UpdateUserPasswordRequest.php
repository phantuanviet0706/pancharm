<?php
    namespace App\Http\Requests\User;

    use App\Models\User;
    use App\Shared\Helper;
    use App\Shared\Translator;
    use Illuminate\Foundation\Http\FormRequest;
    use Illuminate\Support\Facades\Hash;

    class UpdateUserPasswordRequest extends FormRequest {
        private static $PATTERN_CHECK_PASSWORD = '/^(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*()_+\-=\[\]{};\'":\\\\|,.<>\/?]).+$/';
        private static $PATTERN_CHECK_EMAIL = '/^[\w\.-]+@[\w\.-]+\.\w{2,}$/';

        public function authorize() {
            return true;
        }

        public function rules() {
            return [];
        }

        public function prepareForValidation() {
            $user_id = $this->route('id');
            $user = User::find($user_id);
            if (!$user) {
                return Helper::thrownExceptionValidator('user', Translator::trans('Invalid user, please check and try again'));
            }

            $old_password = $this->input('old_password');
            $new_password = $this->input('new_password');
            $confirm_password = $this->input('confirm_password');

            if (Helper::isEmpty($old_password)) {
                return Helper::thrownExceptionValidator('old_password', Translator::trans('Please enter your current password'));
            }

            if (!Hash::check($old_password, $user->password)) {
                return Helper::thrownExceptionValidator('old_password', Translator::trans('The input password does not match with the current password, please check and try again'));
            }
                
            if (Helper::isEmpty($new_password)) {
                return Helper::thrownExceptionValidator('new_password', Translator::trans('Please enter a new password'));
            }

            if (!preg_match(self::$PATTERN_CHECK_PASSWORD, $new_password)) {
                return Helper::thrownExceptionValidator('new_password', Translator::trans('New password must contain at least one uppercase letter, one number, and one special character'));
            }

            // Validate confirm password
            if (Helper::isEmpty($confirm_password)) {
                return Helper::thrownExceptionValidator('confirm_password', Translator::trans('Please confirm your new password'));
            }

            if ($new_password !== $confirm_password) {
                return Helper::thrownExceptionValidator('confirm_password', Translator::trans('Confirm password does not match new password'));
            }

            $this->merge(
                [
                    'old_password'=> $old_password,
                    'new_password'=> $new_password,
                    'password' => Hash::make($new_password)
                ]
            );
        }
    }

?>