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
		/**
		 * Create a new user in admin page
		 * @param \Illuminate\Http\Request $request
		 * @return User|\Illuminate\Database\Eloquent\Model
		 */
        public function create(Request $request) {
            $request->merge([
				'password' => Hash::make($request->input('password'))
			]);
			return User::create($request->all());
        }

		/**
		 * Update user information
		 * @param \Illuminate\Http\Request $request
		 * @param int $id
		 * @return \Illuminate\Database\Eloquent\Builder<User>|null
		 */
		public function update(Request $request, int $id) {
			$user = User::find($id);
			if ($user) {
				$user->update($request->all());
				return $user;
			}
			return null;
		}

		/**
		 * Delete an user (Make soft delete because there's reference info in other tables)
		 * @param int $id
		 * @return bool
		 */
		public function delete(int $id) {
			$user = User::find($id)->first();
			if ($user) {
				$user->soft_delete = 1;
				$user->save();
				return true;
			}
			return false;
		}

		/**
		 * Get all users
		 * @return \Illuminate\Database\Eloquent\Collection<int, User>
		 */
		public function getAll() {
			return User::latest()->all()->where('soft_delete', 0);
		}

		/**
		 * Get User by id
		 * @param int $id
		 * @return object|User|\Illuminate\Database\Eloquent\Model|null
		 */
		public function getById(int $id) {
			return User::where($id)->where('soft_delete', 0)->first();
		}

		/**
		 * Get User by email
		 * @param string $email
		 * @return object|User|\Illuminate\Database\Eloquent\Model|null
		 */
		public function getByEmail(string $email) {
			return User::where('email', $email)->where('soft_delete', 0)->first();
		}

		/**
		 * Get User by username
		 * @param string $username
		 * @return object|User|\Illuminate\Database\Eloquent\Model|null
		 */
		public function getByUsername(string $username) {
			return User::where('username', $username)->where('soft_delete', 0)->first();
		}

		/**
		 * Login
		 * @param \Illuminate\Http\Request $request
		 * @return object
		 */
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

		/**
		 * Logout
		 * @param int $id
		 * @return object
		 */
		public function logout(int $id) {
			$user = User::where("id", $id)->first();
			if (!$user) {
				return Helper::release(Translator::trans("Invalid user, please check and try again"));
			}

			$res = $user->currentAccessToken()->delete();
			if (!$res) {
				return Helper::release(Translator::trans("Logout failed, please check and try again"));
			}

			return Helper::release(Translator::trans("Logout successfully"), Helper::$SUCCESS_CODE);
		}

		/**
		 * Register new user
		 * @param \Illuminate\Http\Request $request
		 * @return object
		 */
		public function register(Request $request) {
			
		}
    }

?>