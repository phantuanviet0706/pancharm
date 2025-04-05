<?php
	namespace APP\Models;

	class Company extends Model
	{
		protected $fillable = [
			'name',
			'address',
			'avatar',
			'taxcode',
			'bank_attachment'
		];

		public function users()
		{
			return $this->hasMany(User::class);
		}
	}

?>