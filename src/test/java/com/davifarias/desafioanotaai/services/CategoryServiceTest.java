package com.davifarias.desafioanotaai.services;

import com.davifarias.desafioanotaai.domain.category.Category;
import com.davifarias.desafioanotaai.domain.category.CategoryDTO;
import com.davifarias.desafioanotaai.domain.category.CategoryNotFoundException;
import com.davifarias.desafioanotaai.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @Mock
    private CategoryRepository repository;

    @InjectMocks
    private CategoryService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insert() {
        CategoryDTO categoryDTO = new CategoryDTO("TestCategory", "TestDescription", "1");
        Category newCategory = new Category(categoryDTO);

        when(repository.save(any(Category.class))).thenReturn(newCategory);

        Category result = service.insert(categoryDTO);

        assertNotNull(result);
        assertEquals(newCategory, result);
        verify(repository, times(1)).save(any(Category.class));
    }

    @Test
    void getAll() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        when(repository.findAll()).thenReturn(categories);

        List<Category> result = service.getAll();

        assertNotNull(result);
        assertEquals(categories, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void getById() {
        String categoryId = "someId";
        Category category = new Category();
        Optional<Category> optionalCategory = Optional.of(category);

        when(repository.findById(categoryId)).thenReturn(optionalCategory);

        Optional<Category> result = service.getById(categoryId);

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
        verify(repository, times(1)).findById(categoryId);
    }

    @Test
    void update() {
        String categoryId = "someId";
        CategoryDTO categoryDTO = new CategoryDTO("UpdatedTitle",
                "UpdatedDescription", "updateOwnerId");
        Category existingCategory = new Category();

        when(repository.findById(categoryId)).thenReturn(Optional.of(existingCategory));
        when(repository.save(any(Category.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Category result = service.update(categoryId, categoryDTO);

        assertNotNull(result);
        assertEquals(existingCategory, result);
        assertEquals(categoryDTO.title(), result.getTitle());
        assertEquals(categoryDTO.description(), result.getDescription());
        verify(repository, times(1)).findById(categoryId);
        verify(repository, times(1)).save(any(Category.class));
    }

    @Test
    void updateNotFound() {
        String categoryId = "nonExistentId";
        CategoryDTO categoryDTO = new CategoryDTO("UpdatedTitle",
                "UpdatedDescription", "updateOwnerId");

        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.update(categoryId, categoryDTO));
        verify(repository, times(1)).findById(categoryId);
        verify(repository, never()).save(any(Category.class));
    }

    @Test
    void delete() {
        String categoryId = "someId";
        Category existingCategory = new Category();

        when(repository.findById(categoryId)).thenReturn(Optional.of(existingCategory));

        service.delete(categoryId);

        verify(repository, times(1)).findById(categoryId);
        verify(repository, times(1)).delete(existingCategory);
    }

    @Test
    void deleteNotFound() {
        String categoryId = "nonExistentId";

        when(repository.findById(categoryId)).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.delete(categoryId));
        verify(repository, times(1)).findById(categoryId);
        verify(repository, never()).delete(any(Category.class));
    }
}