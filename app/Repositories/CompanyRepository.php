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

class CompanyRepository implements CompanyRepositoryInterface
{

	/**
	 * @desc Update company basic information
	 * @param \Illuminate\Http\Request $request
	 * @param int $id
	 * @return object
	 */
	public function update(Request $request)
	{
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
	 * @param \Illuminate\Http\Request $request
	 * @return object
	 */
	public function createCompanyInfo(Request $request)
	{
		DB::beginTransaction();
		$company_info = CompanyInfo::create($request->all());
		if (!$company_info || !$company_info->wasRecentlyCreated) {
			DB::rollBack();
			return Helper::release(Translator::trans("Cannot create company info, please check and try again"));
		}

		DB::commit();
		return Helper::release(Translator::trans("Successfully Created"), Helper::$SUCCESS_CODE, (object) [
			"company_info" => $company_info
		]);
	}

	/**
	 * @desc Update a company info
	 * @param \Illuminate\Http\Request $request
	 * @return object
	 */
	public function updateCompanyInfo(Request $request, int $id)
	{
		$company_info = CompanyInfo::find($id);
		if (!$company_info) {
			return Helper::release(Translator::trans("Invalid company info, please check and try again"));
		}

		DB::beginTransaction();
		$updated = $company_info->update($request->all());
		if (!$updated) {
			DB::rollBack();
			return Helper::release(Translator::trans("Cannot update company info, please check and try again"));
		}

		DB::commit();
		return Helper::release(Translator::trans("Successfully Updated"), Helper::$SUCCESS_CODE, (object) [
			"company_info" => $company_info->fresh()
		]);
	}

	/**
	 * @desc Delete a company info
	 * @param int $id
	 * @return CompanyInfo

	 */
	public function deleteCompanyInfo(int $id)
	{
		$company_info = CompanyInfo::where('id', $id)->first();
		if (!$company_info) {
			return Helper::release(Translator::trans('Invalid company info, please check and try again'));
		}

		DB::beginTransaction();
		$updated = $company_info->delete();
		if (!$updated) {
			DB::rollBack();
			return Helper::release(Translator::trans('Cannot delete company info, please check and try again'));
		}

		DB::commit();
		return Helper::release(Translator::trans("Successfully Deleted"), Helper::$SUCCESS_CODE, (object) [
			"company_info" => $company_info
		]);
	}

	/**
	 * @desc Get all company info by company id
	 * @param int $id
	 * @return object
	 */
	public function getAll(int $id)
	{
		$company_infos = CompanyInfo::where("company_id", $id)->all();
		return Helper::release(Translator::trans("Get data successfully"), Helper::$SUCCESS_CODE, (object) [
			"company_infos" => $company_infos
		]);
	}

	/**
	 * @inheritDoc
	 */
	public function getById(int $id)
	{
		$company = Company::findOrFail($id);
		if (!$company) {
			return Helper::release(Translator::trans("Invalid company, please check and try again"));
		}

		return Helper::release(Translator::trans("Get data successfully"), Helper::$SUCCESS_CODE, (object) [
			"company" => $company
		]);
	}

	/**
	 * @inheritDoc
	 */
	public function getByUserId(int $userId) {}

	/**
	 * @inheritDoc
	 */
	public function getCompanyInfoById(int $companyId) {}

	/**
	 * @inheritDoc
	 */
	public function getCompanyInfoByUserId(int $userId) {}

	public function getDefaultCompany()
	{
		$company = Company::initNewCompany();
		return Helper::release(Translator::trans("Get data successfully"), Helper::$SUCCESS_CODE, (object) [
			"company" => $company
		]);
	}
}
