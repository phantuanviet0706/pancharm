package com.example.pancharm.service.category;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.category.CategoryFilterRequest;
import com.example.pancharm.dto.request.category.CategoryRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.category.*;
import com.example.pancharm.entity.Categories;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CategoryMapper;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.repository.CategoryRepository;
import com.example.pancharm.util.PageRequestUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
// @PreAuthorize("hasAnyRole(T(com.example.pancharm.constant.PredefineRole).SUPER_ADMIN.name(), "
//        + "T(com.example.pancharm.constant.PredefineRole).ADMIN.name())")
public class CategoryService {
    CategoryRepository categoryRepository;
    CategoryMapper categoryMapper;
    PageMapper pageMapper;

    /**
     * @desc Find all categories
     * @return PageResponse<CategoryListResponse>
     */
    public PageResponse<CategoryListResponse> findAll(CategoryFilterRequest request) {
        Pageable pageable = PageRequestUtil.from(request);

        Specification<Categories> spec = ((root, query, criteriaBuilder) -> criteriaBuilder.conjunction());

        if (request.getKeyword() != null && !request.getKeyword().isEmpty()) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.like(root.get("name").as(String.class), "%" + request.getKeyword() + "%")));
        }

        if (request.getSlug() != null && !request.getSlug().isEmpty()) {
            spec = spec.and(((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("slug").as(String.class), "%" + request.getSlug() + "%")));
        }

        return pageMapper.toPageResponse(
                categoryRepository.findAll(spec, pageable).map(categoryMapper::toCategoryListResponse));
    }

    /**
     * @desc Get Single Category
     * @param id
     * @return CategoryDetailResponse
     */
    public CategoryDetailResponse getCategory(int id) {
        var category = categoryRepository
                .findById(String.valueOf(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        return categoryMapper.toCategoryResponse(category);
    }

    /**
     * @desc Create new Category
     * @param request
     * @return CategoryDetailResponse
     */
    public CategoryDetailResponse createCategory(CategoryRequest request) {
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
     * @return CategoryDetailResponse
     */
    public CategoryDetailResponse updateCategory(int id, CategoryRequest request) {
        var category = categoryRepository
                .findById(String.valueOf(id))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

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

    /**
     * @desc Generate slug if not exist
     * @param slugId
     * @return String
     */
    private String generateSlug(int slugId) {
        return "CAT-" + slugId;
    }

    /**
     * @desc Set parent category if set
     * @param category
     * @param categoryId
     */
    private void setParentCategory(Categories category, int categoryId) {
        if (categoryId <= 0) {
            category.setParent(null);
            return;
        }
        var parentCategory = categoryRepository
                .findById(String.valueOf(categoryId))
                .orElseThrow(() -> new AppException(ErrorCode.CATEGORY_NOT_FOUND));

        category.setParent(parentCategory);
    }
}
