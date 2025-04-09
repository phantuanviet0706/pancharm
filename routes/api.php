<?php

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
    Route::post('/add.user', [UserController::class, 'store']);
    Route::post('/update.user/{id}', [UserController::class, 'updateBasicInfo']);
    Route::post('/update.password/{id}', [UserController::class, 'updatePassword']);
    Route::post('/delete.user/{id}', [UserController::class, 'delete']);

?>