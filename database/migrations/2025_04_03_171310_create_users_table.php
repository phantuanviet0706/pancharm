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
        Schema::create('users', function (Blueprint $table) {
            $table->id();
            $table->string('username', 255)->unique();
            $table->string('password');
            $table->string('email')->unique();
            $table->string('fullname', 100);
            $table->text('avatar')->nullable();
            $table->text('address')->nullable();
            $table->string('phone', 35)->nullable();
            $table->smallInteger('status')->default(1);
            $table->integer('role')->default(1);
            $table->text('token')->nullable();
            $table->smallInteger('soft_delete')->default(0);
            $table->timestamps();
        });
    }

    /**
     * Reverse the migrations.
     */
    public function down(): void
    {
        Schema::dropIfExists('users');
    }
};
