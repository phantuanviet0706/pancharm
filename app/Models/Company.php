<?php
	namespace APP\Models;

	use Illuminate\Database\Eloquent\Model;

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