<?php
    namespace App\Http\Requests;

    use Illuminate\Foundation\Http\FormRequest;

    class UserRequest extends FormRequest {
        public function authorize() {
            return true;
        }

        public function rules() {
            return [
                'username' => 'required|string|max:255|unique:users,username',
                'password' => 'required|string|min:8',
                'email' => 'required|email|max:255|unique:users,email',
                'fullname' => 'nullable|string|max:100',
                'avatar' => 'nullable|string',
                'address' => 'nullable|string',
                'phone' => 'nullable|string|max:35',
                'role' => 'required|in:1,2,3', // Assuming 1: User, 2: Admin, 3: Super Admin
            ];
        }
    }

?>