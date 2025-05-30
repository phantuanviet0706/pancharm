<?php

use Illuminate\Database\Migrations\Migration;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Support\Facades\Schema;

return new class extends Migration
{
    /**
     * Run the migrations.
     */
    public function up(): void
    {
        Schema::create('products', function (Blueprint $table) {
            $table->id();
            $table->string('name');
            $table->string('slug', 63)->unique();
            $table->integer('quantity');
            $table->integer('unit_price');
            $table->string('color', 10)->nullable();
            $table->integer('status');
            $table->text('description')->nullable();
            $table->unsignedBigInteger('category_id');
            $table->integer('since');
            $table->integer('last_update');
            $table->smallInteger('soft_deleted')->default(0);
            $table->foreign('category_id')->references('id')->on('categories');
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('products');
    }
};
