<?php

namespace App\Repositories;

use App\Interfaces\ProductRepositoryInterface;
use App\Models\Product;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;

class ProductRepository implements ProductRepositoryInterface
{

    /**
     * @desc Repository for creating a new product
     * @param \Illuminate\Http\Request $request
     * @return object
     * @throws \Exception
     *     */
    public function create(Request $request) {
        DB::beginTransaction();
        $product = Product::create($request->all());
        if (!$product || !$product->wasRecentlyCreated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot create product, please check and try again"));
        }

        $slug = $request->input("slug");
        if (Helper::isEmpty($slug)) {
            $slug = Helper::generateSlug(Product::$DB_SHORT, $product->id);
        }
        $updated = $product->update([
            "slug" => $slug
        ]);
        if (!$updated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot update product, please check and try again"));
        }
        DB::commit();
        return Helper::release(Translator::trans("Successfully Created"), Helper::$SUCCESS_CODE, (object) [
            "product" => $product
        ]);
    }
    
    /**
     * @desc Repository for updating a product
     */
    public function update(Request $request, int $id) {
    }
    
    /**
     * @inheritDoc
     */
    public function delete(int $id) {
    }
    
    /**
     * @inheritDoc
     */
    public function getAll() {
    }
    
    /**
     * @inheritDoc
     */
    public function getByCategoryId(int $category_id) {
    }
    
    /**
     * 
     * @inheritDoc
     */
    public function getById(int $id) {
    }
}