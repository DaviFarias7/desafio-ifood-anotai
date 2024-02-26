package com.davifarias.desafioanotaai.controllers;

import com.davifarias.desafioanotaai.domain.category.Category;
import com.davifarias.desafioanotaai.domain.category.CategoryDTO;
import com.davifarias.desafioanotaai.services.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryControllerTest {

    @Mock
    private CategoryService service;

    @InjectMocks
    private CategoryController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void insert() {
        CategoryDTO categoryDTO = new CategoryDTO("categoriaTeste", "categoria", "1");

        Category createdCategory = new Category();

        when(service.insert(any(CategoryDTO.class))).thenReturn(createdCategory);

        ResponseEntity<Category> responseEntity = controller.insert(categoryDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createdCategory, responseEntity.getBody());

        verify(service, times(1)).insert(categoryDTO);
    }

    @Test
    void getAll() {
        List<Category> categories = Arrays.asList(new Category(), new Category());

        when(service.getAll()).thenReturn(categories);

        ResponseEntity<List<Category>> responseEntity = controller.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(categories, responseEntity.getBody());
        verify(service, times(1)).getAll();
    }

    @Test
    void update() {
        String categoryId = "someId";
        CategoryDTO categoryDTO = new CategoryDTO("categoriaTeste", "categoria", "1");
        Category updatedCategory = new Category();

        when(service.update(categoryId, categoryDTO)).thenReturn(updatedCategory);

        ResponseEntity<Category> responseEntity = controller.update(categoryId, categoryDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedCategory, responseEntity.getBody());
        verify(service, times(1)).update(categoryId, categoryDTO);
    }

    @Test
    void delete() {
        String categoryId = "someId";

        ResponseEntity<Category> responseEntity = controller.delete(categoryId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(service, times(1)).delete(categoryId);
    }
}