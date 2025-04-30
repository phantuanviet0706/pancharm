<?php

namespace App\Services;

use App\Interfaces\UserRepositoryInterface;
use Illuminate\Http\Request;

class UserService
{
    protected $user_repo;

    public function __construct(UserRepositoryInterface $user_repo)
    {
        $this->user_repo = $user_repo;
    }

    public function createUser(Request $data)
    {
        return $this->user_repo->create($data);
    }

    public function updateUser(Request $data, int $id)
    {
        return $this->user_repo->update($data, $id);
    }

    public function deleteUser(int $id)
    {
        return $this->user_repo->delete($id);
    }
    public function getAllUsers()
    {
        return $this->user_repo->getAll();
    }
    public function getUserById(int $id)
    {
        return $this->user_repo->getById($id);
    }
    public function getUserByEmail(string $email)
    {
        return $this->user_repo->getByEmail($email);
    }
    public function getUserByUsername(string $username)
    {
        return $this->user_repo->getByUsername($username);
    }
    public function login(Request $request)

    {
        return $this->user_repo->login($request);
    }

    public function logout(int $id)
    {
        return $this->user_repo->logout($id);
    }

    public function register(Request $request)
    {
        return $this->user_repo->register($request);
    }
}
