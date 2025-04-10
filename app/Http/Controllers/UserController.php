<?php
    namespace App\Http\Controllers;

    use App\Http\Controllers\Controller;
    use App\Http\Requests\LoginRequest;
    use App\Http\Requests\CreateUserRequest;
    use App\Http\Requests\UpdateUserPasswordRequest;
    use App\Http\Requests\UpdateUserRequest;
    use App\Services\UserService;
use App\Shared\Translator;
use Illuminate\Http\JsonResponse;

    class UserController extends Controller {
        protected $user_service;

        public function __construct(UserService $user_service) {
            $this->user_service = $user_service;
        }

        public function store(CreateUserRequest $request) {
            $user = $this->user_service->createUser($request);
            return response()->json(['data' => $user, "message" => "User created successfully"], JsonResponse::HTTP_CREATED);
        }

        public function updateBasicInfo(UpdateUserRequest $request, $id) {
            $user = $this->user_service->updateUser($request, $id);
            if ($user) {
                return response()->json(['data' => $user, "message" => "User updated successfully"], JsonResponse::HTTP_OK);
            }
            return response()->json(['message' => "Invalid data"], JsonResponse::HTTP_BAD_REQUEST);
        }

        public function updatePassword(UpdateUserPasswordRequest $request, $id) {
            $user = $this->user_service->updateUser($request, $id);
            if ($user) {
                return response()->json(['data' => $user, "message" => "Update password successfully"], JsonResponse::HTTP_OK);
            }
            return response()->json(['message' => "Invalid data"], JsonResponse::HTTP_BAD_REQUEST);
        }

        public function delete($id) {
            $is_deleted = $this->user_service->deleteUser($id);
            if ($is_deleted) {
                return response()->json(['message' => "User deleted successfully"], JsonResponse::HTTP_OK);
            }
            return response()->json(['message' => "Invalid data"], JsonResponse::HTTP_BAD_REQUEST);
        }

        public function login(LoginRequest $request) {
            if ($request->user()) {
                return response()->json([
                    "message" => Translator::trans("Login successfully (1)"),
                    "data" => $request->user()
                ], JsonResponse::HTTP_OK);
            }
            $res = $this->user_service->login($request);
            if ($res->code == 0) {
                return response()->json([
                    "message" => $res->message
                ], JsonResponse::HTTP_BAD_REQUEST);
            }
            return response()->json([
                "message" => $res->message,
                "data" => $res->data
            ], JsonResponse::HTTP_OK);
        }

        public function logout(int $id) {
            $res = $this->user_service->logout($id);
            if ($res->code == 0) {
                return response()->json([
                    "message" => $res->message
                ], JsonResponse::HTTP_BAD_REQUEST);
            }
            return response()->json([
                "message" => $res->message,
                "data" => $res->data
            ], JsonResponse::HTTP_OK);
        }
    }
?>