<?php

namespace App\Interfaces;

use Illuminate\Http\Request;

interface CompanyRepositoryInterface
{
	public function update(Request $request);
	public function getAll(int $id);
	public function getById(int $id);
	public function getByUserId(int $user_id);
	public function getCompanyInfoById(int $company_id);
	public function getCompanyInfoByUserId(int $user_id);
	public function createCompanyInfo(Request $request);
	public function updateCompanyInfo(Request $request, int $id);
	public function deleteCompanyInfo(int $id);
	public function getDefaultCompany();
}
