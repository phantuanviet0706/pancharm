<?php
    namespace App\Interfaces;

    interface UserRepositoryInterface {
        public function create(array $data);
		public function update(array $data, int $id);
		public function delete(int $id);
		public function getAll();
		public function getById(int $id);
		public function getByEmail(string $email);
		public function getByUsername(string $username);
    }

?>