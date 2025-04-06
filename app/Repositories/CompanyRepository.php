<?php
	namespace App\Repositories;
	use App\Interfaces\CompanyRepositoryInterface;
	use App\Models\Company;
	use App\Models\CompanyInfo;
	use Str;

	class CompanyRepository implements CompanyRepositoryInterface {
		
		/**
	 * @inheritDoc
	 */
	public function create(array $data) {
		return Company::create($data);
	}
	
	/**
	 * @inheritDoc
	 */
	public function createCompanyInfo(array $data) {
		return CompanyInfo::create($data);
	}
	
	/**
	 * @inheritDoc
	 */
	public function delete(int $id) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function deleteCompanyInfo(int $id) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function getAll() {
	}
	
	/**
	 * @inheritDoc
	 */
	public function getById(int $id) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function getByUserId(int $userId) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function getCompanyInfoByCompanyId(int $companyId) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function getCompanyInfoById(int $companyId) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function getCompanyInfoByUserId(int $userId) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function update(array $data, int $id) {
	}
	
	/**
	 * @inheritDoc
	 */
	public function updateCompanyInfo(array $data, int $id) {
	}
}
?>