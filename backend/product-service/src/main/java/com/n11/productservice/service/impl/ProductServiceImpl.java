package com.n11.productservice.service.impl;


import com.n11.productservice.entity.Product;
import com.n11.productservice.exception.ProductNotFoundException;
import com.n11.productservice.repository.ProductRepository;
import com.n11.productservice.request.ProductRequest;
import com.n11.productservice.response.ProductResponse;
import com.n11.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    @Override
    public ProductResponse create(ProductRequest request) {
        log.info("Creating product. name={}, category={}", request.getName(), request.getCategoryName());

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .categoryName(request.getCategoryName())
                .build();

        Product savedProduct = productRepository.save(product);

        log.info("Product created successfully. id={}", savedProduct.getId());

        return toResponse(savedProduct);
    }

    @Override
    public Page<ProductResponse> getAll(int page, int size) {
        log.info("Fetching products. page={}, size={}", page, size);

        return productRepository.findAll(PageRequest.of(page, size))
                .map(this::toResponse);
    }

    @Override
    public ProductResponse getById(Long id) {
        log.info("Fetching product by id={}", id);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        return toResponse(product);
    }

    private ProductResponse toResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stock(product.getStock())
                .imageUrl(product.getImageUrl())
                .categoryName(product.getCategoryName())
                .build();
    }
}