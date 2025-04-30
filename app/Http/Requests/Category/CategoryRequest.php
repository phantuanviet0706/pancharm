<?php

namespace App\Http\Requests\Category;

use App\Models\Category;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Foundation\Http\FormRequest;

class CategoryRequest extends FormRequest
{
    public function authorize()
    {
        return true;
    }

    public function rules()
    {
        return [];
    }

    public function prepareForValidation()
    {
        $name = $this->input('name');
        if (Helper::isEmpty($name)) {
            return Helper::thrownExceptionValidator('name', Translator::trans("Please fill category name"));
        }

        if (!Helper::isLimitContent($name, 0, 255)) {
            return Helper::thrownExceptionValidator('name', Translator::trans("Category name must be less than 255 characters"));
        }

        $parent_id = $this->input("category_id");
        if (!Helper::isEmpty($parent_id)) {
            $model = Category::where("id", $parent_id)->first();
            if (!$model) {
                return Helper::thrownExceptionValidator("category_id", Translator::trans("Parent category not found"));
            }
        }

        $this->merge([
            'name' => $name,
            'parent_id' => $parent_id,
        ]);
    }
}