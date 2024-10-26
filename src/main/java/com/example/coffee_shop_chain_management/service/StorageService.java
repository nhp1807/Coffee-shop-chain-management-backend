package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.Storage;
import com.example.coffee_shop_chain_management.repository.StorageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StorageService {
    @Autowired
    private StorageRepository storageRepository;

    public Storage createStorage(Storage storage) {
        return storageRepository.save(storage);
    }

    public Storage getStorageById(Long id) {
        return storageRepository.findById(id).orElse(null);
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
}
