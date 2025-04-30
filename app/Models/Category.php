<?php

namespace App\Models;

use App\Shared\Helper;
use Illuminate\Database\Eloquent\Model;

class Category extends Model
{
    public static $DB = 'Categories';
    public static $DB_SHORT = "CAT";

    protected $fillable = [
        'name',
        'slug',
        'parent_id',
    ];

	protected $attributes = [
		"parent_id" => 0
	];

    protected static function boot()
	{
		parent::boot();

		static::creating(function ($model) {
			$model->parent_id = $model->parent_id ?? 0;
		});
	}
}