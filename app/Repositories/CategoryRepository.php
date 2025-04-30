<?php

namespace App\Repositories;

use App\Interfaces\CategoryRepositoryInterface;
use App\Models\Category;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Str;

class CategoryRepository implements CategoryRepositoryInterface
{
    
    /**
     * @inheritDoc
     */
    public function create(Request $request) {
        DB::beginTransaction();
        $category = Category::create($request->all());
        if (!$category || !$category->wasRecentlyCreated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot create category, please check and try again"));
        }

        if (Helper::isEmpty($category->slug)) {
            $category->slug = Helper::generateSlug(Category::$DB_SHORT, $category->id);
            $category->save();
        }
        $updated = $category->update([
            "slug" => $category->slug
        ]);
        if (!$updated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot update category, please check and try again"));
        }

        DB::commit();
        return Helper::release(Translator::trans("Successfully Created"), Helper::$SUCCESS_CODE, (object) [
            "category" => $category
        ]);
    }
    
    /**
     * @inheritDoc
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
    public function getById(int $id) {
    }
}