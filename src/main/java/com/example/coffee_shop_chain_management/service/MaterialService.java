package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateMaterialDTO;
import com.example.coffee_shop_chain_management.dto.UpdateMaterialDTO;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.entity.Material;
import com.example.coffee_shop_chain_management.entity.Storage;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.repository.MaterialRepository;
import com.example.coffee_shop_chain_management.repository.StorageRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.MaterialResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class MaterialService {
    @Autowired
    private MaterialRepository materialRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private BranchRepository branchRepository;

    public APIResponse<List<MaterialResponse>> getAllMaterials() {
        List<Material> materials = materialRepository.findAll();

        return new APIResponse<>(materials.stream().map(this::toMaterialResponse).toList(), "Material retrieved successfully", true);
    }

    public APIResponse<MaterialResponse> createMaterial(CreateMaterialDTO createMaterialDTO) {
        if(materialRepository.existsByName(createMaterialDTO.getName())) {
            return new APIResponse<>(null, "Material already exists", false);
        }

        Material newMaterial = new Material();
        newMaterial.setName(createMaterialDTO.getName());

        newMaterial = materialRepository.save(newMaterial);

        List<Branch> branches = branchRepository.findAll();
        for (Branch branch : branches){
            Storage storage = new Storage();
            storage.setBranch(branch);
            storage.setMaterial(newMaterial);
            storage.setQuantity(0D);

            storageRepository.save(storage);
        }

        return new APIResponse<>(toMaterialResponse(newMaterial), "Material created successfully", true);
    }

    public APIResponse<MaterialResponse> getMaterialById(Long id) {
        Material material = materialRepository.findById(id).orElse(null);

        if (material == null) {
            return new APIResponse<>(null, "Material not found", false);
        }

        return new APIResponse<>(toMaterialResponse(material), "Material retrieved successfully", true);
    }

    public APIResponse<MaterialResponse> updateMaterial(Long id, UpdateMaterialDTO updateMaterialDTO) {
        Material material = materialRepository.findById(id).orElse(null);

        if (material == null) {
            return new APIResponse<>(null, "Material not found", false);
        }

        if (materialRepository.existsByName(updateMaterialDTO.getName())) {
            return new APIResponse<>(null, "Material already exists", false);
        }

        material.setName(updateMaterialDTO.getName());

        Material updatedMaterial = materialRepository.save(material);

        return new APIResponse<>(toMaterialResponse(updatedMaterial), "Material updated successfully", true);
    }

    public APIResponse<MaterialResponse> deleteMaterialById(Long id) {
        Material material = materialRepository.findById(id).orElse(null);

        if (material == null) {
            return new APIResponse<>(null, "Material not found", false);
        }

        materialRepository.deleteById(id);

        return new APIResponse<>(toMaterialResponse(material), "Material deleted successfully", true);
    }

    public MaterialResponse toMaterialResponse(Material material) {
        MaterialResponse materialResponse = new MaterialResponse();
        materialResponse.setMaterialID(material.getMaterialID());
        materialResponse.setName(material.getName());

        return materialResponse;
    }
}
