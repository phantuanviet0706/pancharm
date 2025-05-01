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
     * @desc Repository for creating a new category
     * @param \Illuminate\Http\Request $request
     * @return object
     * @throws \Exception
     */
    public function create(Request $request) {
        DB::beginTransaction();
        $category = Category::create($request->all());
        if (!$category || !$category->wasRecentlyCreated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot create category, please check and try again"));
        }

        $slug = $request->input("slug");
        if (Helper::isEmpty($slug)) {
            $slug = Helper::generateSlug(Category::$DB_SHORT, $category->id);
        }
        $updated = $category->update([
            "slug" => $slug
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
     * @desc Repository for updating a category
     * @param \Illuminate\Http\Request $request
     * @param int $id
     * @return object
     * @throws \Exception
     */
    public function update(Request $request, int $id) {
        $category = Category::find($id);
        if (!$category) {
            return Helper::release(Translator::trans("Invalid category, please check and try again"));
        }

        DB::beginTransaction();
        $updated = $category->update($request->all());
        if (!$updated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot update category, please check and try again"));
        }

        DB::commit();
        return Helper::release(Translator::trans("Successfully Updated"), Helper::$SUCCESS_CODE, (object) [
            "category" => $category->fresh()
        ]);
    }
    
    /**
     * @desc Repository for soft deleting a category
     * @param int $id
     * @return object
     * @throws \Exception
     */
    public function delete(int $id) {
        $category = Category::find($id)->first();
        if (!$category) {
            return Helper::release(Translator::trans("Invalid category, please check and try again"));
        }

        DB::beginTransaction();
        $category->soft_delete = 1;
        $updated = $category->save();
        if (!$updated) {
            DB::rollBack();
            return Helper::release(Translator::trans("Cannot delete category, please check and try again"));
        }
        DB::commit();
        return Helper::release(Translator::trans("Successfully Deleted"), Helper::$SUCCESS_CODE, (object) [
            "category" => $category->fresh()
        ]);
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