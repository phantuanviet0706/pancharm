<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use App\Http\Requests\Product\ProductRequest;
use App\Services\ProductService;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Http\JsonResponse;
use Illuminate\Http\Request;

class ProductController extends Controller
{
    protected $product_service;

    public function __construct(ProductService $product_service)
    {
        $this->product_service = $product_service;
    }

    /**
     * @desc Create a new product.
     * @param \App\Http\Requests\Product\ProductRequest $request
     * @return JsonResponse|mixed
     */
    public function storeProduct(ProductRequest $request)
    {
        $res = $this->product_service->createProduct($request);
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