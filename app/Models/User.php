<?php

	namespace App\Models;

	// use Illuminate\Contracts\Auth\MustVerifyEmail;
	use Illuminate\Database\Eloquent\Factories\HasFactory;
	use Illuminate\Foundation\Auth\User as Authenticatable;
	use Illuminate\Notifications\Notifiable;
	use Laravel\Sanctum\HasApiTokens;

	class User extends Authenticatable
	{
		use HasApiTokens, HasFactory, Notifiable;

		public $ROLE_USER = 1;
		public $ROLE_ADMIN = 2;
		public $ROLE_SUPER_ADMIN = 3;

		const STATUS_ACTIVE = 1;
		const STATUS_INACTIVE = 0;

		/**
		 * The attributes that are mass assignable.
		 *
		 * @var array<int, string>
		 */
		protected $fillable = [
			'username', 'password', 'email', 'fullname', 'avatar', 'address', 'phone', 'role'
		];

		/**
		 * The attributes that should be hidden for serialization.
		 *
		 * @var array<int, string>
		 */
		protected $hidden = [
			'password',
			'token',
		];

		/**
		 * The attributes that should be cast.
		 *
		 * @var array<string, string>
		 */
		protected $casts = [
			'password' => 'hashed',
		];

		protected $attributes = [
			"status" => self::STATUS_ACTIVE,
			"soft_delete" => 0
		];
	}

?.