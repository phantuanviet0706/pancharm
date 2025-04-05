<?php
	namespace App\Http\Controllers;

	use App\Http\Controllers\Controller;
	use App\Http\Requests\CompanyRequest;
	use App\Services\CompanyService;
	use Illuminate\Http\JsonResponse;

	class CompanyController extends Controller
	{
		protected $company_service;

		public function __construct(CompanyService $company_service)
		{
			$this->company_service = $company_service;
		}

		public function store(CompanyRequest $request)
		{
			$company = $this->company_service->createCompany($request->validated());
			return response()->json(['data' => $company, "message" => "Company created successfully"], JsonResponse::HTTP_CREATED);
		}
	}
?>