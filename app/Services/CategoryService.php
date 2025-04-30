<?php

namespace App\Services;

use App\Interfaces\CategoryRepositoryInterface;
use Illuminate\Http\Request;

class CategoryService
{
    protected $category_repo;

    public function __construct(CategoryRepositoryInterface $category_repo)
    {
        $this->category_repo = $category_repo;
    }

    /**
     * @desc Create a new category.
     * @param Request $data
     * @return mixed
     */
    public function createCategory(Request $data)
    {
        return $this->category_repo->create($data);
    }
    public function updateCategory(Request $data, int $id)
    {
        return $this->category_repo->update($data, $id);
    }
    public function deleteCategory(int $id)
    {
        return $this->category_repo->delete($id);
    }
    public function getAllCategories()
    {
        return $this->category_repo->getAll();
    }
    
    public function getCategoryById(int $id)
    {
        return $this->category_repo->getById($id);
    }
}