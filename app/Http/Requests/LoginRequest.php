<?php
    namespace App\Http\Requests;

use App\Models\User;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Auth\Events\Validated;
use Illuminate\Foundation\Http\FormRequest;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\ValidationException;

    class LoginRequest extends FormRequest {
        public function authorize() {
            return true;
        }

        public function rules() {
            return [];
        }

        public function validateResolved() {
            $username = $this->input('username');
            if (Helper::isEmpty($username)) {
				return Helper::thrownExceptionValidator("username",Translator::trans("Please fill username"));
			}
			$user = User::where("username", $username)->first();
			if (!$user) {
				return Helper::thrownExceptionValidator('username', Translator::trans("Invalid user, please check and try again"));
			}

            $password = $this->input('password');
            if (Helper::isEmpty($password)) {
				return Helper::thrownExceptionValidator('password', Translator::trans("Please fill password"));
			}
            
			if (!Hash::check($password, $user->password)) {
				return Helper::thrownExceptionValidator('password', Translator::trans("Invalid password, please check and try again"));
			}

            if ($user->soft_delete == 1) {
			    return Helper::thrownExceptionValidator('user', Translator::trans("User is deleted"));
			}
			if ($user->status == 0) {
				return Helper::thrownExceptionValidator('user', Translator::trans("User is inactive"));
			}

            $this->merge(
                [
                    'user' => $user,
                    'password' => $password,
                ]
            );
        }
    }

?>