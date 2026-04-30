package com.n11.productservice.service;

import com.n11.productservice.entity.Product;
import com.n11.productservice.exception.ProductNotFoundException;
import com.n11.productservice.repository.ProductRepository;
import com.n11.productservice.request.ProductRequest;
import com.n11.productservice.response.ProductResponse;
import com.n11.productservice.service.impl.ProductServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    @Test
    void create_ShouldSaveProductAndReturnResponse() {
        ProductRequest request = new ProductRequest();
        request.setName("iPhone 15");
        request.setDescription("Apple iPhone 15 128GB");
        request.setPrice(BigDecimal.valueOf(49999.99));
        request.setStock(10);
        request.setImageUrl("https://example.com/iphone15.jpg");
        request.setCategoryName("Telefon");

        Product savedProduct = Product.builder()
                .id(1L)
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .categoryName(request.getCategoryName())
                .build();

        when(productRepository.save(any(Product.class))).thenReturn(savedProduct);

        ProductResponse response = productService.create(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("iPhone 15", response.getName());
        assertEquals(BigDecimal.valueOf(49999.99), response.getPrice());
        assertEquals(10, response.getStock());

        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void getById_WhenProductExists_ShouldReturnProduct() {
        Product product = Product.builder()
                .id(1L)
                .name("iPhone 15")
                .description("Apple iPhone 15 128GB")
                .price(BigDecimal.valueOf(49999.99))
                .stock(10)
                .imageUrl("https://example.com/iphone15.jpg")
                .categoryName("Telefon")
                .build();

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductResponse response = productService.getById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("iPhone 15", response.getName());

        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void getById_WhenProductDoesNotExist_ShouldThrowException() {
        when(productRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getById(99L));

        verify(productRepository, times(1)).findById(99L);
    }

    @Test
    void getAll_ShouldReturnPagedProducts() {
        Product product = Product.builder()
                .id(1L)
                .name("iPhone 15")
                .description("Apple iPhone 15 128GB")
                .price(BigDecimal.valueOf(49999.99))
                .stock(10)
                .imageUrl("https://example.com/iphone15.jpg")
                .categoryName("Telefon")
                .build();

        Page<Product> productPage = new PageImpl<>(List.of(product));

        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        Page<ProductResponse> response = productService.getAll(0, 10);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals("iPhone 15", response.getContent().get(0).getName());

        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }
}