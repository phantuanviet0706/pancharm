<?php
    namespace App\Repositories;
    use App\Interfaces\UserRepositoryInterface;
    use App\Models\User;
    use Str;

    class UserRepository implements UserRepositoryInterface {
        public function create(array $data) {
            $data['password'] = bcrypt($data['password']);
            $data['token'] = Str::random(60);
            return User::create($data);
        }
    }

?>