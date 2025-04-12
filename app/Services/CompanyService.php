<?php
	namespace App\Services;

	use App\Interfaces\CompanyRepositoryInterface;
use Illuminate\Http\Request;

	class CompanyService
	{
		protected $company_repo;

		public function __construct(CompanyRepositoryInterface $company_repo)
		{
			$this->company_repo = $company_repo;
		}

		/**
		 * Update Company basic information
		 * @param 
		 * @param int $id
		 */
		public function updateCompany(Request $request)
		{
			return $this->company_repo->update($request);
		}

		public function getAllCompanies()
		{
			return $this->company_repo->getAll();
		}

		public function getCompanyById(int $id)
		{
			return $this->company_repo->getById($id);
		}

		public function getCompanyByUserId(int $userId)
		{
			return $this->company_repo->getByUserId($userId);
		}

		public function getCompanyInfoById(int $companyId)
		{
			return $this->company_repo->getCompanyInfoById($companyId);
		}

		public function getCompanyInfoByUserId(int $userId)
		{
			return $this->company_repo->getCompanyInfoByUserId($userId);
		}

		public function createCompanyInfo(array $data)
		{
			return $this->company_repo->createCompanyInfo($data);
		}

		public function updateCompanyInfo(array $data, int $id)
		{
			return $this->company_repo->updateCompanyInfo($data, $id);
		}

		public function deleteCompanyInfo(int $id)
		{
			return $this->company_repo->deleteCompanyInfo($id);
		}

		public function getCompanyInfoByCompanyId(int $companyId)
		{
			return $this->company_repo->getCompanyInfoByCompanyId($companyId);
		}
	}
?>