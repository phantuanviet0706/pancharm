<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Http\Requests\Company\CompanyInfoRequest;
use App\Http\Requests\Company\CompanyRequest;
use APP\Models\Company;
use App\Services\CompanyService;
use App\Services\UserService;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Http\JsonResponse;

class CompanyController extends Controller
{
	protected $company_service;
	protected $user_service;

	public function __construct(
		CompanyService $company_service,
		UserService $user_service
	) {
		$this->company_service = $company_service;
		$this->user_service = $user_service;
	}

	public function updateCompanyBasicInfo(CompanyRequest $request)
	{
		$res = $this->company_service->updateCompany($request);
		if ($res->code == 0) {
			return response()->json([
				'message' => $res->message
			], JsonResponse::HTTP_BAD_REQUEST);
		}
		return response()->json([
			'message' => $res->message,
			"data" => $res->data
		], JsonResponse::HTTP_OK);
	}

	/**
	 * @desc Create a company info (Register new company contact)
	 * @param CompanyInfoRequest $request
	 * @return JsonResponse
	 */
	public function storeCompanyInfo(CompanyInfoRequest $request)
	{
		$user_id = $request->input('person_in_charge');
		if (Helper::isEmpty($user_id)) {
			return response()->json([
				'message' => Translator::trans("Please input person in charge")
			], JsonResponse::HTTP_BAD_REQUEST);
		}
		$res = $this->user_service->getUserById($user_id);
		if ($res->code == 0) {
			return response()->json([
				'message' => Translator::trans("Invalid user, please check and try again")
			], JsonResponse::HTTP_BAD_REQUEST);
		}

		$res = $this->company_service->getDefaultCompany();
		$company = isset($res->data->company) ? $res->data->company : null;
		if (!$company) {
			return response()->json([
				"message" => Translator::trans("Invalid company, please check and try again")
			], JsonResponse::HTTP_BAD_REQUEST);
		}
		$request->merge([
			"company_id" => $company->id,
			"person_in_charge" => $user_id
		]);
		// $company_id = $request->input('company_id');
		// $res = $this->company_service->getCompanyById($company_id);
		// if ($res->code == 0) {
		// 	return response()->json([
		// 		'message' => $res->message
		// 	], JsonResponse::HTTP_BAD_REQUEST);
		// }

		$res = $this->company_service->createCompanyInfo($request);
		if ($res->code == 0) {
			return response()->json([
				'message' => $res->message
			], JsonResponse::HTTP_BAD_REQUEST);
		}
		return response()->json([
			"message" => $res->message,
			'data' => $res->data
		], JsonResponse::HTTP_OK);
	}

	public function updateCompanyInfo(CompanyInfoRequest $request)
	{
		$user_id = $request->input('person_in_charge');
		if (Helper::isEmpty($user_id)) {
			return response()->json([
				'message' => Translator::trans('Please input person in charge')
			], JsonResponse::HTTP_BAD_REQUEST);
		}
		$res = $this->user_service->getUserById($user_id);
		if ($res->code == 0) {
			return response()->json([
				'message' => Translator::trans("Invalid user, please check and try again")
			], JsonResponse::HTTP_BAD_REQUEST);
		}

		$res = $this->company_service->createCompanyInfo($request);
		if ($res->code == 0) {
			return response()->json([
				'message' => $res->message
			], JsonResponse::HTTP_BAD_REQUEST);
		}
		return response()->json([
			"message" => $res->message,
			'data' => $res->data
		], JsonResponse::HTTP_OK);
	}
}
