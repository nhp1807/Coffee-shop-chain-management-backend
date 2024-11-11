package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateProductDTO;
import com.example.coffee_shop_chain_management.dto.ProductMaterialDTO;
import com.example.coffee_shop_chain_management.entity.Material;
import com.example.coffee_shop_chain_management.entity.Product;
import com.example.coffee_shop_chain_management.entity.ProductMaterial;
import com.example.coffee_shop_chain_management.repository.MaterialRepository;
import com.example.coffee_shop_chain_management.repository.ProductMaterialRepository;
import com.example.coffee_shop_chain_management.repository.ProductRepository;
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

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product createProduct(CreateProductDTO productDTO) {
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
        return productRepository.save(product);
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }

    public boolean deleteProduct(Product product) {
        if (productRepository.existsById(product.getProductID())) {
            return false;
        }

        productRepository.delete(product);
        return true;
    }

    public boolean deleteProductById(Long id) {
        if (productRepository.existsById(id)) {
            return false;
        }

        productRepository.deleteById(id);
        return true;
    }
}
