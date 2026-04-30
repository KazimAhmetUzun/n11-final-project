package com.n11.productservice.service;

import com.n11.productservice.request.ProductRequest;
import com.n11.productservice.response.PagedResponse;
import com.n11.productservice.response.ProductResponse;
import org.springframework.data.domain.Page;

public interface ProductService {

    ProductResponse create(ProductRequest request);

    PagedResponse<ProductResponse> getAll(int page, int size);

    ProductResponse getById(Long id);

    ProductResponse decreaseStock(Long id, int quantity);
}