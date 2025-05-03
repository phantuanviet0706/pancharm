<?php

namespace App\Http\Requests\Product;

use App\Models\Category;
use App\Models\Product;
use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Foundation\Http\FormRequest;

class ProductRequest extends FormRequest
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
            return Helper::thrownExceptionValidator('name', Translator::trans("Please fill product name"));
        }

        $quantity = $this->input('quantity');
        if (!Helper::isEmpty($quantity)) {
            if (!Helper::isNumber($quantity)) {
                return Helper::thrownExceptionValidator('quantity', Translator::trans("Quantity must be a number"));
            }
        } else {
            $quantity = 0;
        }

        $unit_price = $this->input('unit_price');
        if (!Helper::isEmpty($unit_price)) {
            if (!Helper::isNumber($unit_price)) {
                return Helper::thrownExceptionValidator('unit_price', Translator::trans("Unit price must be a number"));
            }
        } else {
            $unit_price = 0;
        }

        $parent_id = $this->input("category_id");
        if (!Helper::isEmpty($parent_id)) {
            $model = Category::where("id", $parent_id)->first();
            if (!$model) {
                return Helper::thrownExceptionValidator("category_id", Translator::trans("Linked category not found"));
            }
        } else {
            $parent_id = null;
        }

        $description = $this->input('description');

        $this->merge([
            'name' => $name,
            'quantity' => $quantity,
            'unit_price' => $unit_price,
            'category_id' => $parent_id,
            'description' => $description,
        ]);
    }
}