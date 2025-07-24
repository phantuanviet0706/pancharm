package com.example.pancharm.service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.category.CategoryRequest;
import com.example.pancharm.dto.response.CategoryResponse;
import com.example.pancharm.entity.Categories;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CategoryMapper;
import com.example.pancharm.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {
	CategoryRepository categoryRepository;
	CategoryMapper categoryMapper;

	/**
	 * @desc Find all categories
	 * @return List<CategoryResponse>
	 */
	public List<CategoryResponse> findAll() {
		return categoryRepository.findAll().stream().map(categoryMapper::toCategoryResponse).toList();
	}

	/**
	 * @desc Get Single Category
	 * @param id
	 * @return CategoryResponse
	 */
	public CategoryResponse getCategory(int id) {
		var category = categoryRepository.findById(String.valueOf(id)).orElseThrow(
				() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
		);

		return categoryMapper.toCategoryResponse(category);
	}

	/**
	 * @desc Create new Category
	 * @param request
	 * @return CategoryResponse
	 */
	public CategoryResponse createCategory(CategoryRequest request) {
		var category = categoryMapper.toCategories(request);
		if (request.getSlug() == null || request.getSlug().isBlank()) {
			category.setSlug("");
		} else if (categoryRepository.existsBySlug(request.getSlug())) {
			throw new AppException(ErrorCode.SLUG_EXISTED);
		} else {
			category.setSlug(request.getSlug());
		}

		setParentCategory(category, request.getParentId());

		try {
			category = categoryRepository.save(category);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		if (category.getSlug() == null || category.getSlug().isBlank()) {
			String generatedSlug = generateSlug(category.getId());
			category.setSlug(generatedSlug);
			try {
				category = categoryRepository.save(category);
			} catch (DataIntegrityViolationException exception) {
				throw new AppException(ErrorCode.UPDATE_ERROR);
			}
		}

		return categoryMapper.toCategoryResponse(category);
	}

	/**
	 * @desc Update existing category
	 * @param id
	 * @param request
	 * @return CategoryResponse
	 */
	public CategoryResponse updateCategory(int id, CategoryRequest request) {
		var category = categoryRepository.findById(String.valueOf(id)).orElseThrow(
				() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
		);

		categoryMapper.updateCategories(category, request);

		setParentCategory(category, request.getParentId());

		try {
			categoryRepository.save(category);
		} catch (DataIntegrityViolationException exception) {
			throw new AppException(ErrorCode.UPDATE_ERROR);
		}

		return categoryMapper.toCategoryResponse(category);
	}

	/**
	 * @desc Delete existing category
	 * @param id
	 */
	public void deleteCategory(int id) {
		if (!categoryRepository.existsById(String.valueOf(id))) {
			throw new AppException(ErrorCode.CATEGORY_NOT_FOUND);
		}

		var childrenCategories = categoryRepository.findAllByParentId(id);
		if (!childrenCategories.isEmpty()) {
			throw new AppException(ErrorCode.CATEGORY_DELETE_ERROR);
		}

		categoryRepository.deleteById(String.valueOf(id));
	}

	private String generateSlug(int slugId) {
		return "CAT-" + slugId;
	}

	private void setParentCategory(Categories category, int categoryId) {
		if (categoryId <= 0) {
			category.setParent(null);
			return;
		}
 		var parentCategory = categoryRepository.findById(String.valueOf(categoryId)).orElseThrow(
				() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND)
		);

		category.setParent(parentCategory);
	}
}
