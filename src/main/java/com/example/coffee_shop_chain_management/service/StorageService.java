package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.Storage;
import com.example.coffee_shop_chain_management.repository.StorageRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.StorageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StorageService {
    @Autowired
    private StorageRepository storageRepository;

    public APIResponse<List<StorageResponse>> getAllStorages() {
        List<Storage> storages = storageRepository.findAll();

        return new APIResponse<>(storages.stream().map(this::toStorageResponse).collect(Collectors.toList()), "Storages retrieved successfully", true);
    }

    @Transactional
    public APIResponse<StorageResponse> createStorage(Storage storage) {

        return new APIResponse<>(toStorageResponse(storageRepository.save(storage)), "Storage created successfully", true);
    }

    public APIResponse<StorageResponse> getStorageById(Long id) {
        Optional<Storage> storage = storageRepository.findById(id);

        if(storage.isEmpty()){
            return new APIResponse<>(null, "Storage not found", false);
        }

        return new APIResponse<>(toStorageResponse(storage.get()), "Storage retrieved successfully", true);
    }

    public APIResponse<List<StorageResponse>> getStorageByBranchId(Long branchID) {
        List<Storage> storages = storageRepository.findByBranch_BranchID(branchID);

        if(storages.isEmpty()){
            return new APIResponse<>(null, "Storages not found", false);
        }

        return new APIResponse<>(storages.stream().map(this::toStorageResponse).collect(Collectors.toList()), "Storages retrieved successfully", true);
    }

    public Storage updateStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    public void deleteStorage(Storage storage) {
        storageRepository.delete(storage);
    }

    public void deleteStorageById(Long id) {
        storageRepository.deleteById(id);
    }

    public StorageResponse toStorageResponse(Storage storage) {
        StorageResponse storageResponse = new StorageResponse();
        storageResponse.setStorageID(storage.getStorageID());
        storageResponse.setQuantity(storage.getQuantity());
        storageResponse.setMaterialID(storage.getMaterial().getMaterialID());
        storageResponse.setBranchID(storage.getBranch().getBranchID());

        return storageResponse;
    }
}
