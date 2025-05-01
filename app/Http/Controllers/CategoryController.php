<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Http\Requests\Category\CategoryRequest;
use App\Services\CategoryService;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Http\JsonResponse;

class CategoryController extends Controller
{
    protected $category_service;

    public function __construct(CategoryService $category_service)
    {
        $this->category_service = $category_service;
    }

    /**
     * @desc Create a new category.
     * @param \App\Http\Requests\Category\CategoryRequest $request
     * @return JsonResponse|mixed
     */
    public function storeCategory(CategoryRequest $request)
    {
        $res = $this->category_service->createCategory($request);
        if ($res->code == 0) {
            return response()->json([
                'message' => $res->message
            ], JsonResponse::HTTP_BAD_REQUEST);
        }
        return response()->json([
            'message' => $res->message,
            "data" => $res->data
        ], JsonResponse::HTTP_OK);
    }

    /**
     * @desc Update a category.
     * @param \App\Http\Requests\Category\CategoryRequest $request
     * @param int $id
     * @return JsonResponse|mixed
     */
    public function updateCategory(CategoryRequest $request, int $id)
    {
        $res = $this->category_service->updateCategory($request, $id);
        if ($res->code == 0) {
            return response()->json([
                'message' => $res->message
            ], JsonResponse::HTTP_BAD_REQUEST);
        }
        return response()->json([
            'message' => $res->message,
            "data" => $res->data
        ], JsonResponse::HTTP_OK);
    }

    /**
     * @desc Soft delete a category.
     * @param int $id
     * @return JsonResponse|mixed
     */
    public function deleteCategory(int $id)
    {
        $res = $this->category_service->deleteCategory($id);
        if ($res->code == 0) {
            return response()->json([
                'message' => $res->message
            ], JsonResponse::HTTP_BAD_REQUEST);
        }
        return response()->json([
            'message' => $res->message,
            "data" => $res->data
        ], JsonResponse::HTTP_OK);
    }
}