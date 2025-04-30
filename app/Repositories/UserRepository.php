<?php

namespace App\Repositories;

use App\Interfaces\UserRepositoryInterface;
use App\Models\User;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Hash;
use Str;

class UserRepository implements UserRepositoryInterface
{
	/**
	 * Create a new user in admin page
	 * @param \Illuminate\Http\Request $request
	 * @return object
	 */
	public function create(Request $request)
	{
		$request->merge([
			'password' => Hash::make($request->input('password'))
		]);
		DB::beginTransaction();
		$user = User::create($request->all());
		if (!$user || !$user->wasRecentlyCreated) {
			DB::rollBack();
			return Helper::release(Translator::trans("Cannot create user, please check and try again"));
		}

		DB::commit();
		return Helper::release(Translator::trans("Successfully Created"), Helper::$SUCCESS_CODE, (object) [
			"user" => $user
		]);
	}

	/**
	 * Update user information
	 * @param \Illuminate\Http\Request $request
	 * @param int $id
	 * @return object
	 */
	public function update(Request $request, int $id)
	{
		$user = User::find($id);
		if (!$user) {
			return Helper::release(Translator::trans("Invalid user, please check and try again"));
		}

		DB::beginTransaction();
		$updated = $user->update($request->all());
		if (!$updated) {
			DB::rollBack();
			return Helper::release(Translator::trans("Cannot update user, please check and try again"));
		}

		DB::commit();
		return Helper::release(Translator::trans("Successfully Updated"), Helper::$SUCCESS_CODE, (object) [
			"user" => $user->fresh()
		]);
	}

	/**
	 * Delete an user (Make soft delete because there's reference info in other tables)
	 * @param int $id
	 * @return object
	 */
	public function delete(int $id)
	{
		$user = User::find($id)->first();
		if (!$user) {
			return Helper::release(Translator::trans("Invalid user, please check and try again"));
		}

		DB::beginTransaction();
		$user->soft_delete = 1;
		$updated = $user->save();
		if (!$updated) {
			DB::rollBack();
			return Helper::release(Translator::trans("Cannot delete user, please check and try again"));
		}
		DB::commit();
		return Helper::release(Translator::trans("Successfully Deleted"), Helper::$SUCCESS_CODE, (object) [
			"user" => $user->fresh()
		]);
	}

	/**
	 * Get all users
	 * @return \Illuminate\Database\Eloquent\Collection<int, User>
	 */
	public function getAll()
	{
		$users = User::latest()->all()->where('soft_delete', 0);
		return Helper::release(Translator::trans('Get data successfully'), Helper::$SUCCESS_CODE, (object) [
			"users" => $users
		]);
	}

	/**
	 * Get User by id
	 * @param int $id
	 * @return object|User|\Illuminate\Database\Eloquent\Model|null
	 */
	public function getById(int $id)
	{
		$user = User::where("id", $id)->where('soft_delete', 0)->first();
		if (!$user) {
			return Helper::release(Translator::trans('Invalid user, please check and try again'));
		}
		if ($user->soft_delete == 1) {
			return Helper::release(Translator::trans('User has been deleted'));
		}
		return Helper::release(Translator::trans('Get data successfully'), Helper::$SUCCESS_CODE, (object) [
			'user' => $user
		]);
	}

	/**
	 * Get User by email
	 * @param string $email
	 * @return object|User|\Illuminate\Database\Eloquent\Model|null
	 */
	public function getByEmail(string $email)
	{
		$user = User::where('email', $email)->where('soft_delete', 0)->first();
		if (!$user) {
			return Helper::release(Translator::trans('Invalid user, please check and try again'));
		}
		if ($user->soft_delete == 1) {
			return Helper::release(Translator::trans('User has been deleted'));
		}
		return Helper::release(Translator::trans('Get data successfully'), Helper::$SUCCESS_CODE, (object) [
			'user' => $user
		]);
	}

	/**
	 * Get User by username
	 * @param string $username
	 * @return object|User|\Illuminate\Database\Eloquent\Model|null
	 */
	public function getByUsername(string $username)
	{
		$user = User::where('username', $username)->where('soft_delete', 0)->first();
		if (!$user) {
			return Helper::release(Translator::trans('Invalid user, please check and try again'));
		}
		if ($user->soft_delete == 1) {
			return Helper::release(Translator::trans('User has been deleted'));
		}
		return Helper::release(Translator::trans('Get data successfully'), Helper::$SUCCESS_CODE, (object) [
			'user' => $user
		]);
	}

	/**
	 * Login
	 * @param \Illuminate\Http\Request $request
	 * @return object
	 */
	public function login(Request $request)
	{

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
	public function logout(int $id)
	{
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
	public function register(Request $request)
	{
		$request->merge([
			'password' => Hash::make($request->input('password')),
			'status' => User::$STATUS_PENDING_VERIFICATION
		]);

		DB::beginTransaction();
		$user = User::create($request->all());
		if (!$user || !$user->wasRecentlyCreated) {
			DB::rollBack();
			return Helper::release(Translator::trans("Cannot create user, please check and try again"));
		}

		DB::commit();
		return Helper::release(Translator::trans("Successfully Created"), Helper::$SUCCESS_CODE, (object) [
			"user" => $user->fresh(),
		]);
	}
}
