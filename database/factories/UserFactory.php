<?php

namespace Database\Factories;

use Illuminate\Database\Eloquent\Factories\Factory;
use Str;

/**
 * @extends \Illuminate\Database\Eloquent\Factories\Factory<\App\Models\User>
 */
class UserFactory extends Factory
{
    /**
     * Define the model's default state.
     *
     * @return array<string, mixed>
     */
    public function definition(): array
    {
        return [
            'username' => fake()->userName(),
            'password' => bcrypt('password'),
            'email' => fake()->unique()->safeEmail(),
            'fullname' => fake()->name(),
            'avatar' => fake()->imageUrl(640, 480, 'people'),
            'address' => fake()->address(),
            'phone' => fake()->phoneNumber(),
            'status' => 1,
            'role' => fake()->numberBetween(1, 3),
            'token' => Str::random(32),
            'soft_delete' => 0,
        ];
    }
}
