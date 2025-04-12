<?php
	namespace App\Http\Controllers;

	use App\Http\Controllers\Controller;
	use App\Http\Requests\Company\CompanyInfoRequest;
	use App\Http\Requests\Company\CompanyRequest;
	use App\Services\CompanyService;
	use Illuminate\Http\JsonResponse;

	class CompanyController extends Controller
	{
		protected $company_service;

		public function __construct(CompanyService $company_service)
		{
			$this->company_service = $company_service;
		}

		public function store(CompanyInfoRequest $request)
		{
			$company_info = $this->company_service->createCompanyInfo($request->validated());
			return response()->json(['data' => $company_info, "message" => "Company Info created successfully"], JsonResponse::HTTP_CREATED);
		}
	}
?>