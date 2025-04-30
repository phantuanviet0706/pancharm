<?php

namespace App\Http\Requests\Company;

use App\Interfaces\UserRepositoryInterface;
use App\Repositories\UserRepository;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Foundation\Http\FormRequest;

class CompanyInfoRequest extends FormRequest
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
		$address = $this->input('address');
		if (!Helper::isEmpty($address) && !Helper::isLimitContent($address, 0, 255)) {
			Helper::release(Translator::trans("Address must be less than 255 characters"));
		}

		$phone = $this->input('phone');
		if (!Helper::isEmpty($phone) && !Helper::isPhone($phone)) {
			Helper::release(Translator::trans('Invalid phone number format'));
		}

		$email = $this->input('email');
		if (!Helper::isEmpty($email) && !Helper::isEmail($email)) {
			Helper::release(Translator::trans('Invalid email format'));
		}

		$this->merge([
			'address' => $address,
			'phone' => $phone,
			'email' => $email,
			'person_in_charge' => $this->input('person_in_charge'),
			'company_id' => $this->input('company_id'),
		]);
	}
}
