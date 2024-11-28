package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.ProductMaterial;
import com.example.coffee_shop_chain_management.entity.ProductMaterialId;
import com.example.coffee_shop_chain_management.repository.ProductMaterialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductMaterialService {
    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    public List<ProductMaterial> getAllProductMaterials() {
        return productMaterialRepository.findAll();
    }

    public ProductMaterial createProductMaterial(ProductMaterial productMaterial) {
        if (productMaterialRepository.existsById(productMaterial.getId())) {
            return null;
        }

        return productMaterialRepository.save(productMaterial);
    }

    public ProductMaterial getProductMaterialById(ProductMaterialId id) {
        return productMaterialRepository.findById(id).orElse(null);
    }

    public ProductMaterial updateProductMaterial(ProductMaterial productMaterial) {
        return productMaterialRepository.save(productMaterial);
    }

    public boolean deleteProductMaterial(ProductMaterial productMaterial) {
        if (!productMaterialRepository.existsById(productMaterial.getId())) {
            return false;
        }

        productMaterialRepository.delete(productMaterial);
        return true;
    }

    public boolean deleteProductMaterialById(ProductMaterialId id) {
        if (!productMaterialRepository.existsById(id)) {
            return false;
        }

        productMaterialRepository.deleteById(id);
        return true;
    }
}
