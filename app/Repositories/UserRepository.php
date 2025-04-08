<?php
    namespace App\Repositories;
    use App\Interfaces\UserRepositoryInterface;
    use App\Models\User;
	use App\Shared\Helper;
	use App\Shared\Translator;
	use Illuminate\Http\Request;
	use Illuminate\Support\Facades\Hash;
use Str;

    class UserRepository implements UserRepositoryInterface {
        public function create(array $request) {
            $request['password'] = Hash::make($request['password']);
			return User::create($request);
        }

		public function update(array $data, int $id) {
			$user = User::find($id);
			if ($user) {
				if (isset($data['password'])) {
					$data['password'] = bcrypt($data['password']);
				}
				$user->update($data);
				return $user;
			}
			return null;
		}

		public function delete(int $id) {
			$user = User::find($id)->first();
			if ($user) {
				$user->soft_delete = 1;
				$user->save();
				return true;
			}
			return false;
		}

		public function getAll() {
			return User::latest()->all()->where('soft_delete', 0);
		}

		public function getById(int $id) {
			return User::where($id)->where('soft_delete', 0)->first();
		}

		public function getByEmail(string $email) {
			return User::where('email', $email)->where('soft_delete', 0)->first();
		}

		public function getByUsername(string $username) {
			return User::where('username', $username)->where('soft_delete', 0)->first();
		}

		public function login(Request $request) {

			// Validate users
			$username = $request->username;
			$user = User::where("username", $username)->first();

			// Check remember me
			$remember_me = $request->remember_me ?? false;
			if (!$remember_me) {
				return Helper::release(Translator::trans("Login successfully"), Helper::$SUCCESS_CODE, (object) [
					"user" => $user
				]);
			}

			// Generate token
			$token = $user->createToken('token', ['*'], now()->addDays(30));
			
			return Helper::release(Translator::trans("Login successfully"), Helper::$SUCCESS_CODE, (object) [
				"user" => $user,
				"token" => $token->plainTextToken,
				"token_type" => "Bearer",
			]);
		}
    }

?>