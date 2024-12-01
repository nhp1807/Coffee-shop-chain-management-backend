package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateMaterialDTO;
import com.example.coffee_shop_chain_management.dto.UpdateMaterialDTO;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.MaterialResponse;
import com.example.coffee_shop_chain_management.service.MaterialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/material")
public class MaterialController {
    private final MaterialService materialService;

    public MaterialController(MaterialService materialService) {
        this.materialService = materialService;
    }

    @GetMapping("/get/all")
    public APIResponse<List<MaterialResponse>> getAllMaterials() {
        return materialService.getAllMaterials();
    }

    @GetMapping("/get/{id}")
    public APIResponse<MaterialResponse> getMaterialById(Long id) {
        return materialService.getMaterialById(id);
    }

    @PostMapping("/create")
    public APIResponse<MaterialResponse> createMaterial(@RequestBody CreateMaterialDTO createMaterialDTO) {
        return materialService.createMaterial(createMaterialDTO);
    }

    @PutMapping("/update/{id}")
    public APIResponse<MaterialResponse> updateMaterial(@PathVariable Long id, @RequestBody UpdateMaterialDTO updateMaterialDTO) {
        return materialService.updateMaterial(id, updateMaterialDTO);
    }

    @DeleteMapping("/delete/{id}")
    public APIResponse<MaterialResponse> deleteMaterialById(@PathVariable Long id) {
        return materialService.deleteMaterialById(id);
    }
}
