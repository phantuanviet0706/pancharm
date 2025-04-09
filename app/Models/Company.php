<?php
	namespace APP\Models;

	use App\Shared\Helper;
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

		/**
		 * Initialize a new company.
		 * @param mixed $name
		 * @return Company|Model
		 */
		public static function initNewCompany($name = '') {
			return self::firstOrCreate([
				'name' => Helper::isEmpty($name) ? 'Pancharm' : $name,
			]);
		}
	}

?>