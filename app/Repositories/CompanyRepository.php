<?php
	namespace App\Repositories;
	use App\Interfaces\CompanyRepositoryInterface;
	use App\Models\Company;
	use App\Models\CompanyInfo;
	use Str;

	class CompanyRepository implements CompanyRepositoryInterface {
	
	/**
	 * @desc Create a new company info
	 * @param array $data
	 * @return CompanyInfo
	 */
	public function createCompanyInfo(array $data) {
		return CompanyInfo::create($data);
	}
	
	/**
	 * @inheritDoc
	 */
	public function update(array $data, int $id) {
		$company = Company::find($id);
		$company->update($data);
		return $company;
	}
	
	/**
	 * @inheritDoc
	 */
	public function updateCompanyInfo(array $data, int $id) {
		$company_info = CompanyInfo::find($id);
		$company_info->update($data);
		return $company_info;
	}
	
	/**
	 * @desc Delete a company info
	 * @param int $id
	 * @return CompanyInfo

	 */
	public function deleteCompanyInfo(int $id) {
		$company_info = CompanyInfo::where('id', $id)->first();
		if ($company_info) {
            $company_info->delete();
            return $company_info;
        }
		return false;
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
}
?>