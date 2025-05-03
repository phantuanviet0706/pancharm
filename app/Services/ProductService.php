<?php

namespace App\Services;

use App\Interfaces\ProductRepositoryInterface;
use Illuminate\Http\Request;

class ProductService
{
    protected $product_repo;

    public function __construct(ProductRepositoryInterface $product_repo)
    {
        $this->product_repo = $product_repo;
    }

    /**
     * @desc Create a new product.
     * @param Request $data
     * @return mixed
     */
    public function createProduct(Request $data)
    {
        return $this->product_repo->create($data);
    }

    public function updateProduct(Request $data, int $id)
    {
        return $this->product_repo->update($data, $id);
    }

    public function deleteProduct(int $id)
    {
        return $this->product_repo->delete($id);
    }

    public function getAllProducts()
    {
        return $this->product_repo->getAll();
    }

    public function getProductById(int $id)
    {
        return $this->product_repo->getById($id);
    }
}