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

        /**
         * Create a new User (in admin management)
         * @param \App\Http\Requests\User\CreateUserRequest $request
         * @return JsonResponse|mixed
         */
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

        /**
         * Update basic info for user (for every users)
         * @param \App\Http\Requests\User\UpdateUserRequest $request
         * @param mixed $id
         * @return JsonResponse|mixed
         */
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

        /**
         * Update password for user (for every users)
         * @param \App\Http\Requests\User\UpdateUserPasswordRequest $request
         * @param mixed $id
         * @return JsonResponse|mixed
         */
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

        /**
         * Delete an user (soft delete user)
         * @param mixed $id
         * @return JsonResponse|mixed
         */
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

        /**
         * Login
         * @param \App\Http\Requests\User\LoginRequest $request
         * @return JsonResponse|mixed
         */
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

        /**
         * Logout
         * @param int $id
         * @return JsonResponse|mixed
         */
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

        /**
         * Register an user (for new user)
         * @param \App\Http\Requests\User\RegisterUserRequest $request
         * @return JsonResponse|mixed
         */
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