<?php
	namespace App\Http\Controllers;

	use App\Http\Controllers\Controller;
	use App\Http\Requests\Company\CompanyInfoRequest;
	use App\Http\Requests\Company\CompanyRequest;
	use APP\Models\Company;
	use App\Services\CompanyService;
	use Illuminate\Http\JsonResponse;

	class CompanyController extends Controller
	{
		protected $company_service;

		public function __construct(CompanyService $company_service)
		{
			$this->company_service = $company_service;
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
		public function store(CompanyInfoRequest $request)
		{
			$company_info = $this->company_service->createCompanyInfo($request->validated());
			return response()->json(['data' => $company_info, "message" => "Company Info created successfully"], JsonResponse::HTTP_CREATED);
		}
	}
?>