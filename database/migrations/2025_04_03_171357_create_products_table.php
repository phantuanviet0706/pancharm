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
            $table->string('slug', 63)->nullable()->unique();
            $table->integer('quantity')->default(0);
            $table->integer('unit_price')->default(0);
            $table->string('color', 10)->nullable();
            $table->smallInteger('status')->default(1);
            $table->text('description')->nullable();
            $table->smallInteger('soft_deleted')->default(0);
            $table->unsignedBigInteger('category_id')->default(0);
            // $table->foreignId('category_id')->nullable()->constrained('categories')->nullOnDelete();
            $table->longText('data')->nullable();
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
