<?php

namespace App\Models;

use App\Shared\Helper;
use App\Shared\Translator;
use Illuminate\Database\Eloquent\Model;

class Product extends Model
{
    public static $DB = 'Products';
    public static $DB_SHORT = "PRO";

    public static $ACTIVE = 1;
    public static $OUT_OF_STOCK = -1;
    public static $INACTIVE = 0;

    protected $fillable = [
        'name',
        'slug',
        'quantity',
        'unit_price',
        'status',
        'category_id',
        'description',
    ];

    protected $attributes = [
        "category_id" => 0,
        "unit_price" => 0,
        "quantity" => 1,
    ];

    protected static function boot()
    {
        parent::boot();

        static::creating(function ($model) {
            $model->category_id = $model->category_id ?? 0;
            $model->quantity = $model->quantity ?? 1;
            $model->unit_price = $model->unit_price ?? 0;
            $model->status = $model->status ?? self::$ACTIVE;
        });
    }

    private static function getStatus()
    {
        return [
            self::$ACTIVE => Translator::trans("Active"),
            self::$OUT_OF_STOCK => Translator::trans("Out of stock"),
            self::$INACTIVE => Translator::trans("Inactive"),
        ];
    }
}