<?php
    namespace App\Repositories;
    use App\Interfaces\UserRepositoryInterface;
    use App\Models\User;
    use Str;

    class UserRepository implements UserRepositoryInterface {
        public function create(array $data) {
            $data['password'] = bcrypt($data['password']);
            return User::create($data);
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
			$user = User::find($id);
			if ($user) {
				$user->delete();
				return true;
			}
			return false;
		}

		public function getAll() {
			
		}

		public function getById(int $id) {

		}

		public function getByEmail(string $email) {
			
		}

		public function getByUsername(string $username) {
			
		}
    }

?>