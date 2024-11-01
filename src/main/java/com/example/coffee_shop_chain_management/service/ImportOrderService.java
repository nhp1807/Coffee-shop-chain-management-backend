package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateImportOrderDTO;
import com.example.coffee_shop_chain_management.dto.DetailImportOrderDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ImportOrderService {
    @Autowired
    private ImportOrderRepository importOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private BranchRepository branchRepository;

    public List<ImportOrder> getAllImportOrders() {
        return importOrderRepository.findAll();
    }

    public ImportOrder createImportOrder(CreateImportOrderDTO importOrderDTO) {
        // Tạo đối tượng ImportOrder mới
        ImportOrder importOrder = new ImportOrder();

        // Thiết lập các thuộc tính
        importOrder.setTotal(importOrderDTO.getTotal());
        importOrder.setPaymentMethod(importOrderDTO.getPaymentMethod());
        importOrder.setDate(importOrderDTO.getDate());

        // Tìm nhà cung cấp theo ID
        Supplier supplier = supplierRepository.findById(importOrderDTO.getSupplierId())
                .orElseThrow(() -> new RuntimeException("Nhà cung cấp không tồn tại"));
        importOrder.setSupplier(supplier);

        // Tìm chi nhánh theo ID
        Branch branch = branchRepository.findById(importOrderDTO.getBranchId())
                .orElseThrow(() -> new RuntimeException("Chi nhánh không tồn tại"));
        importOrder.setBranch(branch);

        // Khởi tạo danh sách DetailImportOrder
        List<DetailImportOrder> detailImportOrders = new ArrayList<>();

        for (DetailImportOrderDTO detailDTO : importOrderDTO.getDetailImportOrders()) {
            // Tìm nguyên liệu theo tên
            Material material = materialRepository.findByName(detailDTO.getMaterialName());

            if (material == null) {
                // Nếu nguyên liệu không tồn tại, tạo mới
                material = new Material();
                material.setName(detailDTO.getMaterialName());
                materialRepository.save(material); // Lưu nguyên liệu mới

                // Tạo Storage mới cho nguyên liệu vừa tạo đối với tất cả các chi nhánh, nhưng chỉ tăng quantity cho chi nhánh hiện tại
                List<Branch> branches = branchRepository.findAll();
                for (Branch b : branches) {
                    Storage newStorage = new Storage();
                    newStorage.setMaterial(material);
                    newStorage.setBranch(b);

                    if (b.getBranchID() == branch.getBranchID()) {
                        newStorage.setQuantity(detailDTO.getQuantity());
                        storageRepository.save(newStorage); // Lưu Storage mới
                        continue;
                    }

                    newStorage.setQuantity(0d);
                    storageRepository.save(newStorage); // Lưu Storage mới
                }
            } else {
                // Nếu nguyên liệu đã tồn tại, kiểm tra kho
                Storage storage = storageRepository.findByMaterial_MaterialID(material.getMaterialID());

                if (storage != null) {
                    // Nếu Material đã tồn tại trong kho, tăng quantity
                    storage.setQuantity(storage.getQuantity() + detailDTO.getQuantity());
                    storageRepository.save(storage);
                } else {
                    // Nếu chưa có trong kho, tạo mới Storage
                    Storage newStorage = new Storage();
                    newStorage.setMaterial(material);
                    newStorage.setQuantity(detailDTO.getQuantity());
                    newStorage.setBranch(branch);
                    storageRepository.save(newStorage); // Lưu Storage mới
                }
            }

            // Tạo DetailImportOrder và thêm vào danh sách
            DetailImportOrder detailImportOrder = new DetailImportOrder();
            detailImportOrder.setMaterial(material); // Sử dụng nguyên liệu đã tìm thấy hoặc vừa tạo
            detailImportOrder.setQuantity(detailDTO.getQuantity());
            detailImportOrder.setImportOrder(importOrder);

            detailImportOrders.add(detailImportOrder);
        }

        // Thiết lập danh sách chi tiết vào đơn nhập
        importOrder.setDetailImportOrders(detailImportOrders);

        return importOrderRepository.save(importOrder);
    }


    public ImportOrder getImportOrderById(Long id) {
        return importOrderRepository.findById(id).orElse(null);
    }

    public ImportOrder updateImportOrder(ImportOrder importOrder) {
        return importOrderRepository.save(importOrder);
    }

    public void deleteImportOrder(ImportOrder importOrder) {
        importOrderRepository.delete(importOrder);
    }

    public void deleteImportOrderByID(Long id) {
        importOrderRepository.deleteById(id);
    }
}
