<?php

namespace App\Interfaces;

use Illuminate\Http\Request;

interface CategoryRepositoryInterface
{
    public function create(Request $request);
    public function update(Request $request, int $id);
    public function delete(int $id);
    public function getAll();
    public function getById(int $id);
}