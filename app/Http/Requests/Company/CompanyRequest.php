<?php

namespace App\Http\Requests\Company;

use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Foundation\Http\FormRequest;

class CompanyRequest extends FormRequest
{
	public function authorize()
	{
		return true;
	}

	public function rules()
	{
		return [];
	}

	public function prepareForValidation()
	{
		$name = $this->input("name");
		if (!Helper::isEmpty($name)) {
			if (!Helper::isLimitContent($name, 0, 255)) {
				return Helper::thrownExceptionValidator("name", Translator::trans("Name must be less than 255 characters"));
			}

			$this->merge([
				"name" => $name
			]);
		}

		$address = $this->input("address");
		if (!Helper::isEmpty($address && !Helper::isLimitContent($address, 0, 255))) {
			return Helper::thrownExceptionValidator("address", Translator::trans("Address must be less than 255 characters"));
		}

		$avatar = $this->input("avatar");
		$tax_code = $this->input("taxcode");
		if (!Helper::isEmpty($tax_code) && !Helper::isLimitContent($tax_code, 0, 63)) {
			return Helper::thrownExceptionValidator("taxcode", Translator::trans("Tax code must be less than 63 characters"));
		}

		$this->merge([
			"address" => $address,
			"avatar" => $avatar,
			"taxcode" => $tax_code
		]);
	}
}
