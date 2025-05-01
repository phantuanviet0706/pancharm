<?php

use App\Http\Controllers\CategoryController;
use App\Http\Controllers\CompanyController;
use App\Http\Controllers\UserController;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Route;

/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider and all of them will
| be assigned to the "api" middleware group. Make something great!
|
*/

Route::post('/login', [UserController::class, 'login']);
Route::middleware('auth:sanctum')->post('/login/check', function (Request $request) {
    return $request->user();
});

// User routes
Route::prefix("user")->group(function () {
    Route::post('/add.user', [UserController::class, 'store']);
    Route::post('/update.user/{id}', [UserController::class, 'updateBasicInfo']);
    Route::post('/update.password/{id}', [UserController::class, 'updatePassword']);
    Route::post('/delete.user/{id}', [UserController::class, 'delete']);
});

Route::prefix("company")->group(function () {
    Route::post('/update.company', [CompanyController::class, 'updateCompanyBasicInfo']);
    // Route::get('/get.all.companies', [CompanyController::class, 'getAllCompanies']);
    // Route::get('/get.company.by.id/{id}', [CompanyController::class, 'getCompanyById']);
    // Route::get('/get.company.by.user.id/{userId}', [CompanyController::class, 'getCompanyByUserId']);
    Route::post('/create.company.info', [CompanyController::class, 'storeCompanyInfo']);
    // Route::post('/update.company.info/{id}', [CompanyController::class, 'updateCompanyInfo']);
});

Route::prefix("category")->group(function () {
    Route::post('/create', [CategoryController::class, 'storeCategory']);
    Route::post('/update/{id}', [CategoryController::class, 'updateCategory']);
});
