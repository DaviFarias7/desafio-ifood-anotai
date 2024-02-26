package com.davifarias.desafioanotaai.controllers;

import com.davifarias.desafioanotaai.domain.product.Product;
import com.davifarias.desafioanotaai.domain.product.ProductDTO;
import com.davifarias.desafioanotaai.services.ProductService;
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

class ProductControllerTest {

    @Mock
    private ProductService service;

    @InjectMocks
    private ProductController controller;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void insert() {
        ProductDTO productDTO = new ProductDTO("ProductName",
                "ProductDescription", "OwnerId", 10, "CategoryId");
        Product createdProduct = new Product();

        when(service.insert(any(ProductDTO.class))).thenReturn(createdProduct);

        ResponseEntity<Product> responseEntity = controller.insert(productDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(createdProduct, responseEntity.getBody());
        verify(service, times(1)).insert(productDTO);
    }

    @Test
    void getAll() {
        List<Product> products = Arrays.asList(new Product(), new Product());

        when(service.getAll()).thenReturn(products);

        ResponseEntity<List<Product>> responseEntity = controller.getAll();

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(products, responseEntity.getBody());
        verify(service, times(1)).getAll();
    }

    @Test
    void update() {
        String productId = "someId";
        ProductDTO productDTO = new ProductDTO("UpdatedName", "UpdatedDescription", "OwnerId", 15, "UpdatedCategoryId");
        Product updatedProduct = new Product();

        when(service.update(productId, productDTO)).thenReturn(updatedProduct);

        ResponseEntity<Product> responseEntity = controller.update(productId, productDTO);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(updatedProduct, responseEntity.getBody());
        verify(service, times(1)).update(productId, productDTO);
    }

    @Test
    void delete() {
        String productId = "someId";

        ResponseEntity<Product> responseEntity = controller.delete(productId);

        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
        verify(service, times(1)).delete(productId);
    }
}