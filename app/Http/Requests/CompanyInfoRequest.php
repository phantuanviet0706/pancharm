<?php
	namespace App\Http\Requests;

	use Illuminate\Foundation\Http\FormRequest;

	class CompanyInfoRequest extends FormRequest
	{
		public function authorize()
		{
			return true;
		}

		public function rules()
		{
			return [
				'address' => 'nullable|string|max:255',
				'phone' => 'nullable|string|max:35',
				'email' => 'nullable|email|max:255',
				'person_in_charge' => 'required|integer|exists:users,id',
				'company_id' => 'required|integer|exists:company,id',
			];
		}
	}
?>