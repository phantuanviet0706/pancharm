<?php
    namespace App\Http\Controllers;

    use App\Http\Controllers\Controller;
use App\Http\Requests\LoginRequest;
use App\Http\Requests\UserRequest;
    use App\Services\UserService;
    use Illuminate\Http\JsonResponse;

    class UserController extends Controller {
        protected $user_service;

        public function __construct(UserService $user_service) {
            $this->user_service = $user_service;
        }

        public function store(UserRequest $request) {
            $user = $this->user_service->createUser($request->validated());
            return response()->json(['data' => $user, "message" => "User created successfully"], JsonResponse::HTTP_CREATED);
        }

        public function login(LoginRequest $request) {
            $code = $this->user_service->login($request);
            return response()->json(["data"=> $code], JsonResponse::HTTP_REQUEST_TIMEOUT);
        }
    }
?>