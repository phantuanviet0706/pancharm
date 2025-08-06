package com.example.pancharm.service;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.example.pancharm.constant.ErrorCode;
import com.example.pancharm.dto.request.category.CategoryFilterRequest;
import com.example.pancharm.dto.request.category.CategoryRequest;
import com.example.pancharm.dto.response.base.PageResponse;
import com.example.pancharm.dto.response.category.CategoryDetailResponse;
import com.example.pancharm.dto.response.category.CategoryListResponse;
import com.example.pancharm.entity.Categories;
import com.example.pancharm.exception.AppException;
import com.example.pancharm.mapper.CategoryMapper;
import com.example.pancharm.mapper.PageMapper;
import com.example.pancharm.repository.CategoryRepository;
import com.example.pancharm.service.category.CategoryService;
import com.example.pancharm.util.GeneralUtil;
import com.example.pancharm.util.PageRequestUtil;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

public class CategoryServiceTest {

    @Mock CategoryRepository categoryRepository;

    @Mock CategoryMapper categoryMapper;

    @Mock PageMapper pageMapper;

    @Mock GeneralUtil generalUtil;

    @InjectMocks CategoryService categoryService;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findAll_WithFilters_ReturnsPageResponse() {
        CategoryFilterRequest request = new CategoryFilterRequest();
        request.setKeyword("test");
        request.setSlug("slug");

        Pageable pageable = PageRequest.of(0, 10);
        Page<Categories> categoryPage = new PageImpl<>(List.of(new Categories()));

        when(PageRequestUtil.from(request)).thenReturn(pageable);
        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(categoryPage);
        when(categoryMapper.toCategoryListResponse(any(Categories.class))).thenReturn(new CategoryListResponse());
        when(pageMapper.toPageResponse(any(Page.class))).thenReturn(new PageResponse<CategoryListResponse>());


        PageResponse<CategoryListResponse> result = categoryService.findAll(request);

        assertNotNull(result);
        verify(categoryRepository).findAll(any(Specification.class), eq(pageable));
        verify(categoryMapper).toCategoryListResponse(any(Categories.class));
        verify(pageMapper).toPageResponse(any());
    }

    @Test
    void findAll_WithoutFilters_ReturnsPageResponse() {
        CategoryFilterRequest request = new CategoryFilterRequest();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Categories> categoryPage = new PageImpl<>(List.of(new Categories()));

        when(PageRequestUtil.from(request)).thenReturn(pageable);
        when(categoryRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(categoryPage);
        when(categoryMapper.toCategoryListResponse(any(Categories.class))).thenReturn(new CategoryListResponse());
        when(pageMapper.toPageResponse(any(Page.class))).thenReturn(new PageResponse<CategoryListResponse>());

        PageResponse<CategoryListResponse> result = categoryService.findAll(request);

        assertNotNull(result);
        verify(categoryRepository).findAll(any(Specification.class), eq(pageable));
        verify(categoryMapper).toCategoryListResponse(any(Categories.class));
        verify(pageMapper).toPageResponse(any());
    }


    @Test
    void getCategory_Success() {
        int id = 1;
        Categories category = new Categories();
        CategoryDetailResponse response = new CategoryDetailResponse();

        when(categoryRepository.findById(String.valueOf(id))).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        CategoryDetailResponse result = categoryService.getCategory(id);

        assertEquals(response, result);
    }

    @Test
    void getCategory_NotFound_ThrowsException() {
        int id = 1;
        when(categoryRepository.findById(String.valueOf(id))).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> categoryService.getCategory(id));
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, ex.getErrorCode());
    }


    @Test
    void createCategory_Success_WithSlugProvided() {
        CategoryRequest request = new CategoryRequest();
        request.setSlug("slug-abc");
        request.setParentId(0);

        Categories category = new Categories();
        CategoryDetailResponse response = new CategoryDetailResponse();

        when(categoryMapper.toCategories(request)).thenReturn(category);
        when(categoryRepository.existsBySlug("slug-abc")).thenReturn(false);
        when(categoryRepository.findById(anyString())).thenThrow(new NoSuchElementException()); // parentId=0 -> no parent
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        CategoryDetailResponse result = categoryService.createCategory(request);

        assertEquals(response, result);
        verify(categoryRepository).existsBySlug("slug-abc");
        verify(categoryRepository, times(1)).save(category);
    }

    @Test
    void createCategory_Success_SlugNullOrBlank_GeneratesSlug() {
        CategoryRequest request = new CategoryRequest();
        request.setSlug(null);
        request.setParentId(0);

        Categories category = new Categories();
        category.setId(123);
        CategoryDetailResponse response = new CategoryDetailResponse();

        when(categoryMapper.toCategories(request)).thenReturn(category);
        when(categoryRepository.save(category))
                .thenReturn(category) // first save
                .thenReturn(category); // second save after slug generated

        when(generalUtil.generateSlug("category", category.getId())).thenReturn("generated-slug");
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        CategoryDetailResponse result = categoryService.createCategory(request);

        assertEquals(response, result);
        assertEquals("generated-slug", category.getSlug());
        verify(categoryRepository, times(2)).save(category);
    }

    @Test
    void createCategory_Throws_WhenSlugExisted() {
        CategoryRequest request = new CategoryRequest();
        request.setSlug("existing-slug");

        when(categoryRepository.existsBySlug("existing-slug")).thenReturn(true);

        AppException ex = assertThrows(AppException.class, () -> categoryService.createCategory(request));
        assertEquals(ErrorCode.SLUG_EXISTED, ex.getErrorCode());
    }

    @Test
    void createCategory_Throws_WhenSaveFails() {
        CategoryRequest request = new CategoryRequest();
        request.setSlug(null);
        request.setParentId(0);

        Categories category = new Categories();
        when(categoryMapper.toCategories(request)).thenReturn(category);
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).save(category);

        AppException ex = assertThrows(AppException.class, () -> categoryService.createCategory(request));
        assertEquals(ErrorCode.UPDATE_ERROR, ex.getErrorCode());
    }

    @Test
    void createCategory_Throws_WhenSaveFailsAfterSlugGenerated() {
        CategoryRequest request = new CategoryRequest();
        request.setSlug(null);
        request.setParentId(0);

        Categories category = new Categories();
        category.setId(123);

        when(categoryMapper.toCategories(request)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(generalUtil.generateSlug("category", category.getId())).thenReturn("generated-slug");
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).save(category);

        AppException ex = assertThrows(AppException.class, () -> categoryService.createCategory(request));
        assertEquals(ErrorCode.UPDATE_ERROR, ex.getErrorCode());
    }


    @Test
    void updateCategory_Success() {
        int id = 1;
        CategoryRequest request = new CategoryRequest();
        request.setParentId(0);

        Categories category = new Categories();
        CategoryDetailResponse response = new CategoryDetailResponse();

        when(categoryRepository.findById(String.valueOf(id))).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategories(category, request);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        CategoryDetailResponse result = categoryService.updateCategory(id, request);

        assertEquals(response, result);
    }

    @Test
    void updateCategory_Throws_WhenNotFound() {
        int id = 1;
        CategoryRequest request = new CategoryRequest();

        when(categoryRepository.findById(String.valueOf(id))).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> categoryService.updateCategory(id, request));
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void updateCategory_Throws_WhenSaveFails() {
        int id = 1;
        CategoryRequest request = new CategoryRequest();
        Categories category = new Categories();

        when(categoryRepository.findById(String.valueOf(id))).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategories(category, request);
        doThrow(DataIntegrityViolationException.class).when(categoryRepository).save(category);

        AppException ex = assertThrows(AppException.class, () -> categoryService.updateCategory(id, request));
        assertEquals(ErrorCode.UPDATE_ERROR, ex.getErrorCode());
    }

    @Test
    void deleteCategory_Success() {
        int id = 1;

        when(categoryRepository.existsById(String.valueOf(id))).thenReturn(true);
        when(categoryRepository.findAllByParentId(id)).thenReturn(Collections.emptyList());
        doNothing().when(categoryRepository).deleteById(String.valueOf(id));

        categoryService.deleteCategory(id);

        verify(categoryRepository).deleteById(String.valueOf(id));
    }

    @Test
    void deleteCategory_Throws_WhenNotFound() {
        int id = 1;

        when(categoryRepository.existsById(String.valueOf(id))).thenReturn(false);

        AppException ex = assertThrows(AppException.class, () -> categoryService.deleteCategory(id));
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, ex.getErrorCode());
    }

    @Test
    void deleteCategory_Throws_WhenHasChildren() {
        int id = 1;

        when(categoryRepository.existsById(String.valueOf(id))).thenReturn(true);
        when(categoryRepository.findAllByParentId(id)).thenReturn(List.of(new Categories()));

        AppException ex = assertThrows(AppException.class, () -> categoryService.deleteCategory(id));
        assertEquals(ErrorCode.CATEGORY_DELETE_ERROR, ex.getErrorCode());
    }

    @Test
    void createCategory_WithParent_SetsParentCategory() {
        CategoryRequest request = new CategoryRequest();
        request.setSlug("slug");
        request.setParentId(2);

        Categories category = new Categories();
        Categories parentCategory = new Categories();

        CategoryDetailResponse response = new CategoryDetailResponse();

        when(categoryMapper.toCategories(request)).thenReturn(category);
        when(categoryRepository.existsBySlug("slug")).thenReturn(false);
        when(categoryRepository.findById("2")).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        CategoryDetailResponse result = categoryService.createCategory(request);

        assertEquals(response, result);
        assertEquals(parentCategory, category.getParent());
    }

    @Test
    void updateCategory_WithParent_SetsParentCategory() {
        int id = 3;
        CategoryRequest request = new CategoryRequest();
        request.setParentId(5);

        Categories category = new Categories();
        Categories parentCategory = new Categories();
        CategoryDetailResponse response = new CategoryDetailResponse();

        when(categoryRepository.findById(String.valueOf(id))).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper).updateCategories(category, request);
        when(categoryRepository.findById("5")).thenReturn(Optional.of(parentCategory));
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryResponse(category)).thenReturn(response);

        CategoryDetailResponse result = categoryService.updateCategory(id, request);

        assertEquals(response, result);
        assertEquals(parentCategory, category.getParent());
    }

    @Test
    void createCategory_WithInvalidParent_ThrowsException() {
        CategoryRequest request = new CategoryRequest();
        request.setParentId(99);

        Categories category = new Categories();

        when(categoryMapper.toCategories(request)).thenReturn(category);
        when(categoryRepository.existsBySlug(anyString())).thenReturn(false);
        when(categoryRepository.findById("99")).thenReturn(Optional.empty());

        AppException ex = assertThrows(AppException.class, () -> categoryService.createCategory(request));
        assertEquals(ErrorCode.CATEGORY_NOT_FOUND, ex.getErrorCode());
    }
}

