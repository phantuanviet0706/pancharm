<?php
	namespace App\Repositories;

	use App\Interfaces\CompanyRepositoryInterface;
	use App\Models\Company;
	use App\Models\CompanyInfo;
	use App\Shared\Helper;
	use App\Shared\Translator;
	use Illuminate\Http\Request;
	use Illuminate\Support\Facades\DB;
	use Str;

	class CompanyRepository implements CompanyRepositoryInterface {
		
		/**
		 * @desc Update company basic information
		 * @param \Illuminate\Http\Request $request
		 * @param int $id
		 * @return object
		 */
		public function update(Request $request) {
			$company = Company::firstOrFail()->first();
			if (!$company && !Company::exists()) {
				$company = Company::initNewCompany();
			} else if (!$company) {
				return Helper::release(Translator::trans("Invalid company, please check and try again"));
			}

			DB::beginTransaction();
			$res = $company->update($request->all());
			if (!$res) {
				DB::rollBack();
				return Helper::release(Translator::trans("Cannot update company, please check and try again"));
			}

			DB::commit();
			return Helper::release(Translator::trans("Successfully Updated"), Helper::$SUCCESS_CODE, (object) [
				"company" => $company->fresh()
			]);
		}

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