<?php
	namespace App\Http\Requests;

	use Illuminate\Foundation\Http\FormRequest;

	class CompanyRequest extends FormRequest
	{
		public function authorize() {
			return true;
		}

		public function rules() {
			return [
				'name' => 'required|string|max:255|unique:companies,name',
				'address' => 'nullable|string|max:255',
				'avatar' => 'nullable|string|max:255',
				'taxcode' => 'nullable|string|max:255',
				'bank_attachment' => 'nullable|string|max:255',
			];
		}
	}
?>