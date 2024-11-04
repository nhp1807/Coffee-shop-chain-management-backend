package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateSupplierDTO;
import com.example.coffee_shop_chain_management.dto.UpdateSupplierDTO;
import com.example.coffee_shop_chain_management.entity.Supplier;
import com.example.coffee_shop_chain_management.repository.SupplierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SupplierService {
    @Autowired
    private SupplierRepository supplierRepository;

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.findAll();
    }

    public Supplier createSupplier(CreateSupplierDTO supplierDTO) {
        if (supplierRepository.existsByName(supplierDTO.getName())) {
            return null;
        }

        Supplier supplier = new Supplier();
        supplier.setName(supplierDTO.getName());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());

        return supplierRepository.save(supplier);
    }

    public Supplier getSupplierById(Long id) {
        return supplierRepository.findById(id).orElse(null);
    }

    public Supplier updateSupplier(Long id, UpdateSupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id).orElse(null);

        if (supplier == null) {
            return null;
        }

        if (supplierDTO.getName() != null) {
            supplier.setName(supplierDTO.getName());
        }

        if (supplierDTO.getPhone() != null) {
            supplier.setPhone(supplierDTO.getPhone());
        }

        if (supplierDTO.getAddress() != null) {
            supplier.setAddress(supplierDTO.getAddress());
        }

        return supplierRepository.save(supplier);
    }

    public void deleteSupplier(Supplier supplier) {
        supplierRepository.delete(supplier);
    }

    public void deleteSupplierById(Long id) {
        supplierRepository.deleteById(id);
    }
}
