package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateProductDTO;
import com.example.coffee_shop_chain_management.dto.ProductMaterialDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.DetailExportOrderRepository;
import com.example.coffee_shop_chain_management.repository.MaterialRepository;
import com.example.coffee_shop_chain_management.repository.ProductMaterialRepository;
import com.example.coffee_shop_chain_management.repository.ProductRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.ProductMaterialResponse;
import com.example.coffee_shop_chain_management.response.ProductResponse;
import com.example.coffee_shop_chain_management.response.ProductStatResponse;
import jakarta.ws.rs.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private DetailExportOrderRepository detailExportOrderRepository;

    public APIResponse<List<ProductResponse>> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return new APIResponse<>(products.stream().map(this::toProductResponse).toList(), "Products retrieved successfully", true);
    }

    public APIResponse<ProductResponse> getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product not found"));
        return new APIResponse<>(toProductResponse(product), "Product retrieved successfully", true);
    }

    @Transactional
    public APIResponse<ProductResponse> createProduct(CreateProductDTO productDTO) {
        if (productRepository.existsByName(productDTO.getName())) {
            return new APIResponse<>(null, "Product already exists", false);
        }

        Product product = new Product();
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setImage(productDTO.getImage());

        List<ProductMaterial> productMaterials = new ArrayList<>();

        if (productDTO.getProductMaterials() != null) {
            for (ProductMaterialDTO pmDTO : productDTO.getProductMaterials()) {
                Optional<Material> material = materialRepository.findById(pmDTO.getMaterialID());

                if (material.isEmpty()) {
                    return new APIResponse<>(null, "Material not found", false);
                }

                ProductMaterial productMaterial = new ProductMaterial();
                ProductMaterialId productMaterialId = new ProductMaterialId();
                productMaterialId.setProductId(product.getProductID());
                productMaterialId.setMaterialId(material.get().getMaterialID());
                productMaterial.setId(productMaterialId);
                productMaterial.setProduct(product);
                productMaterial.setMaterial(material.get());
                productMaterial.setQuantity(pmDTO.getQuantity());

                productMaterials.add(productMaterial);
            }
        }

        product.setProductMaterials(productMaterials);
        Product savedProduct = productRepository.save(product);
        return new APIResponse<>(toProductResponse(savedProduct), "Product created successfully", true);
    }

    @Transactional
    public APIResponse<ProductResponse> addProductMaterial(Long id, ProductMaterialDTO productMaterialDTO) {
        Optional<Product> productExisted = productRepository.findById(id);

        if (productExisted.isEmpty()) {
            return new APIResponse<>(null, "Product not found", false);
        }

        Product product = productExisted.get();

        Optional<Material> material = materialRepository.findById(productMaterialDTO.getMaterialID());

        if (material.isEmpty()) {
            return new APIResponse<>(null, "Material not found", false);
        }

        ProductMaterial productMaterial = new ProductMaterial();
        ProductMaterialId productMaterialId = new ProductMaterialId();
        productMaterialId.setProductId(product.getProductID());
        productMaterialId.setMaterialId(material.get().getMaterialID());
        productMaterial.setId(productMaterialId);
        productMaterial.setProduct(product);
        productMaterial.setMaterial(material.get());
        productMaterial.setQuantity(productMaterialDTO.getQuantity());

        product.getProductMaterials().add(productMaterial);
        productRepository.save(product);

        return new APIResponse<>(toProductResponse(product), "Product material added successfully", true);
    }

    @Transactional
    public APIResponse<ProductResponse> deleteProductMaterial(Long id, Long materialId) {
        Optional<Product> productExisted = productRepository.findById(id);

        if (productExisted.isEmpty()) {
            return new APIResponse<>(null, "Product not found", false);
        }

        Product product = productExisted.get();

        Optional<ProductMaterial> productMaterial = product.getProductMaterials().stream()
                .filter(pm -> pm.getMaterial().getMaterialID().equals(materialId))
                .findFirst();

        if (productMaterial.isEmpty()) {
            return new APIResponse<>(null, "Product material not found", false);
        }

        product.getProductMaterials().remove(productMaterial.get());
        productRepository.save(product);

        return new APIResponse<>(toProductResponse(product), "Product material removed successfully", true);
    }

    @Transactional
    public APIResponse<ProductResponse> updateProduct(Long id, CreateProductDTO productDTO) {
        Optional<Product> productExisted = productRepository.findById(id);

        if (productExisted.isEmpty()) {
            return new APIResponse<>(null, "Product not found", false);
        }

        Product product = productExisted.get();

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

    @Transactional
    public APIResponse<ProductResponse> deleteProduct(Product product) {
        if (!productRepository.existsById(product.getProductID())) {
            return new APIResponse<>(null, "Product not found", false);
        }

        productRepository.delete(product);
        return new APIResponse<>(toProductResponse(product), "Product deleted successfully", true);

    }

    @Transactional
    public APIResponse<ProductResponse> deleteProductById(Long id) {
        if (!productRepository.existsById(id)) {
            return new APIResponse<>(null, "Product not found", false);
        }

        productRepository.deleteById(id);
        return new APIResponse<>(null, "Product deleted successfully", true);
    }

    public APIResponse<List<ProductStatResponse>> getProductStat() {
        List<Product> products = productRepository.findAll();

        List<ProductStatResponse> productStatResponses = new ArrayList<>();

        for (Product product : products) {
            ProductStatResponse productStatResponse = new ProductStatResponse();
            productStatResponse.setProductID(product.getProductID());

            List<DetailExportOrder> productStat = detailExportOrderRepository.findByProduct_ProductID(product.getProductID());
            long totalSales = productStat.stream().mapToLong(DetailExportOrder::getQuantity).sum();
            double totalRevenue = productStat.stream().mapToDouble(detailExportOrder -> detailExportOrder.getQuantity() * detailExportOrder.getPrice()).sum();

            productStatResponse.setTotalSales(totalSales);
            productStatResponse.setTotalRevenue(totalRevenue);

            productStatResponses.add(productStatResponse);
        }

        return new APIResponse<>(productStatResponses, "Product statistics retrieved successfully", true);
    }

    public ProductResponse toProductResponse(Product product) {
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductID(product.getProductID());
        productResponse.setName(product.getName());
        productResponse.setDescription(product.getDescription());
        productResponse.setPrice(product.getPrice());
        productResponse.setImage(product.getImage());
        productResponse.setProductMaterials(product.getProductMaterials().stream().map(productMaterial -> {
            ProductMaterialResponse productMaterialResponse = new ProductMaterialResponse();
            productMaterialResponse.setMaterialID(productMaterial.getMaterial().getMaterialID());
            productMaterialResponse.setMaterialName(productMaterial.getMaterial().getName());
            productMaterialResponse.setProductID(productMaterial.getProduct().getProductID());
            productMaterialResponse.setQuantity(productMaterial.getQuantity());
            return productMaterialResponse;
        }).toList());

        return productResponse;
    }
}
