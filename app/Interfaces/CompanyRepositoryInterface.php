<?php
	namespace App\Interfaces;

	use Illuminate\Http\Request;

	interface CompanyRepositoryInterface {
		public function update(Request $request);
		public function getAll();
		public function getById(int $id);
		public function getByUserId(int $userId);
		public function getCompanyInfoById(int $companyId);
		public function getCompanyInfoByUserId(int $userId);
		public function createCompanyInfo(array $data);
		public function updateCompanyInfo(array $data, int $id);
		public function deleteCompanyInfo(int $id);
		public function getCompanyInfoByCompanyId(int $companyId);
	}

?>