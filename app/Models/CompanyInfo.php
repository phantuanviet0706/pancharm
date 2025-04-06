<?php

	namespace App\Models;

	use Illuminate\Database\Eloquent\Model;

	class CompanyInfo extends Model
	{
		protected $fillable = [
			'address',
			'phone',
			'email',
			'person_in_charge',
			'company_id',
		];

		/**
		 * Set reference to company
		 */
		public function company()
		{
			return $this->belongsTo(Company::class);
		}

		/**
		 * Set reference to users in charge
		 */
		public function users()
		{
			return $this->belongsTo(User::class);
		}
	}
?>