<?php
	namespace App\Services;

	use App\Interfaces\CompanyRepositoryInterface;

	class CompanyService
	{
		protected $company_repo;

		public function __construct(CompanyRepositoryInterface $company_repo)
		{
			$this->company_repo = $company_repo;
		}

		public function createCompany(array $data)
		{
			return $this->company_repo->create($data);
		}

		public function updateCompany(array $data, int $id)
		{
			return $this->company_repo->update($data, $id);
		}

		public function deleteCompany(int $id)
		{
			return $this->company_repo->delete($id);
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