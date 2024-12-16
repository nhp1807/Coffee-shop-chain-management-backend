package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateProductDTO;
import com.example.coffee_shop_chain_management.dto.DateRangeDTO;
import com.example.coffee_shop_chain_management.dto.ProductMaterialDTO;
import com.example.coffee_shop_chain_management.dto.UpdateProductDTO;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.ProductResponse;
import com.example.coffee_shop_chain_management.response.ProductStatResponse;
import com.example.coffee_shop_chain_management.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/product")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/get/all")
    public APIResponse<List<ProductResponse>> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/get/{id}")
    public APIResponse<ProductResponse> getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PostMapping("/create")
    public APIResponse<ProductResponse> createProduct(@RequestBody CreateProductDTO productDTO) {
        return productService.createProduct(productDTO);
    }

    @PutMapping("/add-material/{productId}")
    public APIResponse<ProductResponse> addProductMaterial(@PathVariable Long productId, @RequestBody ProductMaterialDTO productMaterialDTO) {
        return productService.addProductMaterial(productId, productMaterialDTO);
    }

    @DeleteMapping("/delete-material/{productId}/{materialId}")
    public APIResponse<ProductResponse> deleteProductMaterial(@PathVariable Long productId, @PathVariable Long materialId) {
        return productService.deleteProductMaterial(productId, materialId);
    }

    @PutMapping("/update/{id}")
    public APIResponse<ProductResponse> updateProduct(@RequestBody UpdateProductDTO productDTO, @PathVariable Long id) {
        return productService.updateProduct(id, productDTO);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<ProductResponse> deleteProduct(@PathVariable Long id) {
        return productService.deleteProductById(id);
    }

    @PostMapping("/get/stat")
    public APIResponse<List<ProductStatResponse>> getProductStat(@RequestBody DateRangeDTO dateRangeDTO) {
        return productService.getProductStat(dateRangeDTO);
    }
}
