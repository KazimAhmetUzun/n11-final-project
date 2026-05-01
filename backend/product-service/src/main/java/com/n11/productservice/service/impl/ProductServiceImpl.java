package com.n11.productservice.service.impl;


import com.n11.productservice.entity.Product;
import com.n11.productservice.exception.InsufficientStockException;
import com.n11.productservice.exception.ProductNotFoundException;
import com.n11.productservice.repository.ProductRepository;
import com.n11.productservice.request.ProductRequest;
import com.n11.productservice.response.PagedResponse;
import com.n11.productservice.response.ProductResponse;
import com.n11.productservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
    public PagedResponse<ProductResponse> getAll(int page, int size) {
        log.info("Fetching products. page={}, size={}", page, size);

        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Product> productPage = productRepository.findAll(pageable);

        return PagedResponse.<ProductResponse>builder()
                .content(productPage.getContent()
                        .stream()
                        .map(this::toResponse)
                        .toList())
                .page(productPage.getNumber())
                .size(productPage.getSize())
                .totalElements(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .last(productPage.isLast())
                .build();
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

    @Override
    public ProductResponse decreaseStock(Long id, int quantity) {
        log.info("Decreasing product stock. productId={}, quantity={}", id, quantity);

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Product stock decrease failed. Product not found. productId={}", id);
                    return new ProductNotFoundException(id);
                });

        if (quantity <= 0) {
            log.warn("Product stock decrease failed. Quantity must be positive. productId={}, quantity={}", id, quantity);
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        if (product.getStock() < quantity) {
            log.warn("Product stock decrease failed. Insufficient stock. productId={}, requestedQuantity={}, availableStock={}",
                    id, quantity, product.getStock());
            throw new InsufficientStockException(id, quantity, product.getStock());
        }

        product.setStock(product.getStock() - quantity);

        Product savedProduct = productRepository.save(product);

        log.info("Product stock decreased successfully. productId={}, remainingStock={}",
                savedProduct.getId(), savedProduct.getStock());

        return toResponse(savedProduct);
    }
}