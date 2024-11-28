package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateProductDTO;
import com.example.coffee_shop_chain_management.dto.ProductMaterialDTO;
import com.example.coffee_shop_chain_management.entity.Material;
import com.example.coffee_shop_chain_management.entity.Product;
import com.example.coffee_shop_chain_management.entity.ProductMaterial;
import com.example.coffee_shop_chain_management.repository.MaterialRepository;
import com.example.coffee_shop_chain_management.repository.ProductMaterialRepository;
import com.example.coffee_shop_chain_management.repository.ProductRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.ProductResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    @Autowired
    private MaterialRepository materialRepository;

    public APIResponse<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new APIResponse<>(products.stream().map(this::toProductResponse).toList(), "Products retrieved successfully", true);
    }

    public APIResponse<ProductResponse> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return new APIResponse<>(toProductResponse(product), "Product retrieved successfully", true);
    }

    public APIResponse<ProductResponse> createProduct(CreateProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName())) {
            return null;
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImage(productDTO.getImage());

        List<ProductMaterial> productMaterials = new ArrayList<>();

        if (productDTO.getProductMaterials() != null) {
            for (ProductMaterialDTO pmDTO : productDTO.getProductMaterials()) {
                Material material = materialRepository.findById(pmDTO.getMaterialId())
                        .orElseThrow(() -> new NotFoundException("Material not found with ID: " + pmDTO.getMaterialId()));

                ProductMaterial productMaterial = new ProductMaterial();
                productMaterial.setProduct(product);
                productMaterial.setMaterial(material);
                productMaterial.setQuantity(pmDTO.getQuantity());

                productMaterials.add(productMaterial);
            }
        }

        product.setProductMaterials(productMaterials);
        return new APIResponse<>(toProductResponse(productRepository.save(product)), "Product created successfully", true);
    }

    public APIResponse<ProductResponse> updateProduct(Long id, CreateProductDTO productDTO) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));

        if (productDTO.getName() != null) {
            product.setName(productDTO.getName());
        }

        if (productDTO.getDescription() != null) {
            product.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null) {
            product.setPrice(productDTO.getPrice());
        }

        if (productDTO.getImage() != null) {
            product.setImage(productDTO.getImage());
        }
        // Update product materials

        return new APIResponse<>(toProductResponse(productRepository.save(product)), "Product updated successfully", true);
    }

    public APIResponse<ProductResponse> deleteProduct(Product product) {
        if (!productRepository.existsById(product.getProductID())) {
            return new APIResponse<>(null, "Product not found", false);
        }

        productRepository.delete(product);
        return new APIResponse<>(toProductResponse(product), "Product deleted successfully", true);

    }

    public APIResponse<ProductResponse> deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            return new APIResponse<>(null, "Product not found", false);
        }

        productRepository.deleteById(id);
        return new APIResponse<>(null, "Product deleted successfully", true);
    }

    public ProductResponse toProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductID(product.getProductID());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setImage(product.getImage());

        return productResponse;
    }
}
