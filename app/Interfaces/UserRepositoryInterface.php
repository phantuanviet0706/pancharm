<?php

namespace App\Interfaces;

use Illuminate\Http\Request;

interface UserRepositoryInterface
{
	public function create(Request $data);
	public function update(Request $data, int $id);
	public function delete(int $id);
	public function getAll();
	public function getById(int $id);
	public function getByEmail(string $email);
	public function getByUsername(string $username);
	public function login(Request $request);
	public function logout(int $id);
	public function register(Request $request);
}
