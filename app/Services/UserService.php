<?php
    namespace App\Services;

    use App\Interfaces\UserRepositoryInterface;

    class UserService {
        protected $user_repo;

        public function __construct(UserRepositoryInterface $user_repo) {
            $this->user_repo = $user_repo;
        }

        public function createUser( array $data) {
            return $this->user_repo->create($data);
        }
    }

?>