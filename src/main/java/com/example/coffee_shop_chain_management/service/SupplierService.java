package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateSupplierDTO;
import com.example.coffee_shop_chain_management.dto.UpdateSupplierDTO;
import com.example.coffee_shop_chain_management.entity.Supplier;
import com.example.coffee_shop_chain_management.repository.SupplierRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.SupplierResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public APIResponse<List<SupplierResponse>> getAllSuppliers() {
        List<Supplier> suppliers = supplierRepository.findAll();

        return new APIResponse<>(suppliers.stream().map(this::toSupplierResponse).toList(), "Suppliers retrieved successfully", true);
    }

    @Transactional
    public APIResponse<SupplierResponse> createSupplier(CreateSupplierDTO supplierDTO) {
        if (supplierRepository.existsByName(supplierDTO.getName())) {
            return new APIResponse<>(null, "Supplier already exists", false);
        }

        Supplier supplier = new Supplier();
        supplier.setName(supplierDTO.getName());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());

        Supplier newSupplier = supplierRepository.save(supplier);

        return new APIResponse<>(toSupplierResponse(newSupplier), "Supplier created successfully", true);
    }

    public APIResponse<SupplierResponse> getSupplierById(Long id) {
        Optional<Supplier> supplierExisted = supplierRepository.findById(id);

        if (supplierExisted.isEmpty()) {
            return new APIResponse<>(null, "Supplier not found", false);
        }

        Supplier supplier = supplierExisted.get();

        return new APIResponse<>(toSupplierResponse(supplier), "Supplier retrieved successfully", true);
    }

    @Transactional
    public APIResponse<SupplierResponse> updateSupplier(Long id, UpdateSupplierDTO supplierDTO) {
        Optional<Supplier> supplierExisted = supplierRepository.findById(id);

        if (supplierExisted.isEmpty()) {
            return new APIResponse<>(null, "Supplier not found", false);
        }

        Supplier supplier = supplierExisted.get();

        if (supplierDTO.getName() != null) {
            supplier.setName(supplierDTO.getName());
        }

        if (supplierDTO.getPhone() != null) {
            supplier.setPhone(supplierDTO.getPhone());
        }

        if (supplierDTO.getAddress() != null) {
            supplier.setAddress(supplierDTO.getAddress());
        }

        supplierRepository.save(supplier);

        return new APIResponse<>(toSupplierResponse(supplier), "Supplier updated successfully", true);
    }

    @Transactional
    public APIResponse<SupplierResponse> deleteSupplier(Supplier supplier) {
        if(supplierRepository.existsById(supplier.getSupplierID())){
            return new APIResponse<>(null, "Supplier not found", false);
        }

        supplierRepository.delete(supplier);
        return new APIResponse<>(null, "Supplier deleted successfully", true);
    }

    @Transactional
    public APIResponse<SupplierResponse> deleteSupplierById(Long id) {
        if (!supplierRepository.existsById(id)) {
            return new APIResponse<>(null, "Supplier not found", false);
        }

        supplierRepository.deleteById(id);
        return new APIResponse<>(null, "Supplier deleted successfully", true);
    }

    public SupplierResponse toSupplierResponse(Supplier supplier) {
        SupplierResponse supplierResponse = new SupplierResponse();
        supplierResponse.setSupplierID(supplier.getSupplierID());
        supplierResponse.setName(supplier.getName());
        supplierResponse.setPhone(supplier.getPhone());
        supplierResponse.setAddress(supplier.getAddress());
        return supplierResponse;
    }
}
