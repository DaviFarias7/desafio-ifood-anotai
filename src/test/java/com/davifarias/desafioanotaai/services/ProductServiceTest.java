package com.davifarias.desafioanotaai.services;

import com.davifarias.desafioanotaai.domain.category.Category;
import com.davifarias.desafioanotaai.domain.category.CategoryNotFoundException;
import com.davifarias.desafioanotaai.domain.product.Product;
import com.davifarias.desafioanotaai.domain.product.ProductDTO;
import com.davifarias.desafioanotaai.domain.product.ProductNotFoundException;
import com.davifarias.desafioanotaai.repositories.ProductRepository;
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

class ProductServiceTest {

    @Mock
    private CategoryService categoryService;

    @Mock
    private ProductRepository repository;

    @InjectMocks
    private ProductService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insert() {
        ProductDTO productDTO = new ProductDTO("TestProduct",
                "TestDescription", "1", 10, "categoryId");
        Category category = new Category();
        Product newProduct = new Product(productDTO);
        newProduct.setCategory(category);

        when(categoryService.getById(productDTO.categoryId())).thenReturn(Optional.of(category));
        when(repository.save(any(Product.class))).thenReturn(newProduct);

        Product result = service.insert(productDTO);

        assertNotNull(result);
        assertEquals(newProduct, result);
        verify(categoryService, times(1)).getById(productDTO.categoryId());
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void insertCategoryNotFound() {
        ProductDTO productDTO = new ProductDTO("TestProduct",
                "TestDescription", "1", 10, "nonExistentCategoryId");

        when(categoryService.getById(productDTO.categoryId())).thenReturn(Optional.empty());

        assertThrows(CategoryNotFoundException.class, () -> service.insert(productDTO));
        verify(categoryService, times(1)).getById(productDTO.categoryId());
        verify(repository, never()).save(any(Product.class));
    }


    @Test
    void getAll() {
        List<Product> products = Arrays.asList(new Product(), new Product());

        when(repository.findAll()).thenReturn(products);

        List<Product> result = service.getAll();

        assertNotNull(result);
        assertEquals(products, result);
        verify(repository, times(1)).findAll();
    }

    @Test
    void update() {
        String productId = "someId";
        ProductDTO productDTO = new ProductDTO("UpdatedTitle",
                "UpdatedDescription", "1", 15, "categoryId");
        Category category = new Category();
        Product existingProduct = new Product();

        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(categoryService.getById(productDTO.categoryId())).thenReturn(Optional.of(category));
        when(repository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product result = service.update(productId, productDTO);

        assertNotNull(result);
        assertEquals(existingProduct, result);
        assertEquals(productDTO.title(), result.getTitle());
        assertEquals(productDTO.description(), result.getDescription());
        assertEquals(productDTO.price(), result.getPrice());
        verify(categoryService, times(1)).getById(productDTO.categoryId());
        verify(repository, times(1)).findById(productId);
        verify(repository, times(1)).save(any(Product.class));
    }

    @Test
    void updateProductNotFound() {
        String productId = "nonExistentId";
        ProductDTO productDTO = new ProductDTO("UpdatedTitle",
                "UpdatedDescription", "1", 15, "categoryId");

        when(repository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> service.update(productId, productDTO));
        verify(categoryService, never()).getById(anyString());
        verify(repository, times(1)).findById(productId);
        verify(repository, never()).save(any(Product.class));
    }

    @Test
    void delete() {
        String productId = "someId";
        Product existingProduct = new Product();

        when(repository.findById(productId)).thenReturn(Optional.of(existingProduct));

        service.delete(productId);

        verify(repository, times(1)).findById(productId);
        verify(repository, times(1)).delete(existingProduct);
    }

    @Test
    void deleteNotFound() {
        String productId = "nonExistentId";
    }
}