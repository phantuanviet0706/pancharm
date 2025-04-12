<?php
    namespace App\Http\Controllers;

    use App\Http\Controllers\Controller;
    use App\Http\Requests\User\LoginRequest;
    use App\Http\Requests\User\CreateUserRequest;
    use App\Http\Requests\User\RegisterUserRequest;
    use App\Http\Requests\User\UpdateUserPasswordRequest;
    use App\Http\Requests\User\UpdateUserRequest;
    use App\Notifications\VerifyEmailNotification;
    use App\Services\UserService;
    use App\Shared\Translator;
    use Illuminate\Http\JsonResponse;

    class UserController extends Controller {
        protected $user_service;

        public function __construct(UserService $user_service) {
            $this->user_service = $user_service;
        }

        public function store(CreateUserRequest $request) {
            $res = $this->user_service->createUser($request);
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

        public function updateBasicInfo(UpdateUserRequest $request, $id) {
            $res = $this->user_service->updateUser($request, $id);
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

        public function updatePassword(UpdateUserPasswordRequest $request, $id) {
            $res = $this->user_service->updateUser($request, $id);
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

        public function delete($id) {
            $res = $this->user_service->deleteUser($id);
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

        public function register(RegisterUserRequest $request) {
            $res = $this->user_service->register($request);
            if ($res->code == 0) {
                return response()->json([
                    "message" => $res->message
                ], JsonResponse::HTTP_BAD_REQUEST);
            }

            // $user = $res->data;
            // if ($user) {
            //     $user->notify(new VerifyEmailNotification());
            // }
            return response()->json([
                "message" => $res->message,
                "data" => $res->data
            ], JsonResponse::HTTP_OK);
        }
    }
?>