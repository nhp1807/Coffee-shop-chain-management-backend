package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.DetailImportOrderDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.*;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailImportOrderResponse;
import com.example.coffee_shop_chain_management.response.ImportOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DetailImportOrderService {
    @Autowired
    private DetailImportOrderRepository detailImportOrderRepository;

    @Autowired
    private ImportOrderRepository importOrderRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private BranchRepository branchRepository;

    public List<DetailImportOrder> getAllDetailImportOrders(){
        return detailImportOrderRepository.findAll();
    }

    public DetailImportOrder createDetailImportOrder(DetailImportOrder detailImportOrder){
        return detailImportOrderRepository.save(detailImportOrder);
    }

    @Transactional
    public APIResponse<ImportOrderResponse> addDetailImportOrder(Long id, DetailImportOrderDTO detailImportOrderDTO){
        Optional<ImportOrder> importOrderExisted = importOrderRepository.findById(id);

        if (importOrderExisted.isEmpty()) {
            return new APIResponse<>(null, "Import order not found", false);
        }

        ImportOrder importOrder = importOrderExisted.get();

        double total = importOrder.getTotal();

        Material material = materialRepository.findByName(detailImportOrderDTO.getName());

        if (material == null) {
            material = new Material();
            material.setName(detailImportOrderDTO.getName());
            materialRepository.save(material);

            List<Branch> branches = branchRepository.findAll();
            for (Branch b : branches) {
                Storage newStorage = new Storage();
                newStorage.setMaterial(material);
                newStorage.setBranch(b);

                if (Objects.equals(b.getBranchID(), importOrder.getBranch().getBranchID())) {
                    newStorage.setQuantity(detailImportOrderDTO.getQuantity());
                    storageRepository.save(newStorage);
                    continue;
                }

                newStorage.setQuantity(0d);

                storageRepository.save(newStorage);
            }
        }

        DetailImportOrder detailImportOrder = detailImportOrderRepository.findDetailImportOrderByImportOrder_ImportIDAndMaterial_MaterialID(importOrder.getImportID(), material.getMaterialID());

        if (detailImportOrder != null) {
            return new APIResponse<>(null, "Detail import order already existed", false);
        }

        detailImportOrder = new DetailImportOrder();
        DetailImportOrderId detailImportOrderId = new DetailImportOrderId();
        detailImportOrderId.setImportOrderId(importOrder.getImportID());
        detailImportOrderId.setMaterialId(material.getMaterialID());
        detailImportOrder.setId(detailImportOrderId);

        detailImportOrder.setMaterial(material);
        detailImportOrder.setQuantity(detailImportOrderDTO.getQuantity());
        detailImportOrder.setImportOrder(importOrder);
        detailImportOrder.setPrice(detailImportOrderDTO.getPrice());
        detailImportOrder.setDescription(detailImportOrderDTO.getDescription());

        total += detailImportOrderDTO.getQuantity() * detailImportOrderDTO.getPrice();
        importOrder.setTotal(total);
        importOrder.getDetailImportOrders().add(detailImportOrder);
        importOrderRepository.save(importOrder);

        Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), importOrder.getBranch().getBranchID());
        storage.setQuantity(storage.getQuantity() + detailImportOrderDTO.getQuantity());
        storageRepository.save(storage);

        return new APIResponse<>(null, "Detail import order added successfully", true);
    }

    @Transactional
    public APIResponse<ImportOrderResponse> deleteDetailImportOrder(Long OrderId, Long MaterialId) {
        DetailImportOrder detailImportOrder = detailImportOrderRepository.findDetailImportOrderByImportOrder_ImportIDAndMaterial_MaterialID(OrderId, MaterialId);

        if (detailImportOrder == null) {
            return new APIResponse<>(null, "Detail import order not found", false);
        }

        ImportOrder importOrder = detailImportOrder.getImportOrder();
        importOrder.setTotal(importOrder.getTotal() - detailImportOrder.getQuantity() * detailImportOrder.getPrice());
        Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(MaterialId, importOrder.getBranch().getBranchID());
        storage.setQuantity(storage.getQuantity() - detailImportOrder.getQuantity());

        List<DetailImportOrder> detailImportOrders = importOrder.getDetailImportOrders();
        detailImportOrders.remove(detailImportOrder);
        importOrder.setDetailImportOrders(detailImportOrders);

        importOrderRepository.save(importOrder);
        detailImportOrderRepository.delete(detailImportOrder);
        storageRepository.save(storage);

        return new APIResponse<>(null, "Detail import order deleted successfully", true);
    }

    public APIResponse<DetailImportOrderResponse> getDetailImportOrderById(Long importOrderId, Long materialId){
        DetailImportOrderId id = new DetailImportOrderId(importOrderId, materialId);

        DetailImportOrder detailImportOrder = detailImportOrderRepository.findById(id).orElse(null);

        if (detailImportOrder == null){
            return new APIResponse<>(null, "Detail import order not found", false);
        }

        return new APIResponse<>(toDetailImportOrderResponse(detailImportOrder), "Get detail import order successfully", true);
    }

    @Transactional
    public APIResponse<DetailImportOrderResponse> updateDetailImportOrder(Long importOrderId, Long materialId, DetailImportOrder detailImportOrder){
        DetailImportOrderId id = new DetailImportOrderId(importOrderId, materialId);

        DetailImportOrder detailImportOrderExisted = detailImportOrderRepository.findById(id).orElse(null);

        if (detailImportOrderExisted == null){
            return new APIResponse<>(null, "Detail import order not found", false);
        }

        ImportOrder importOrder = detailImportOrderExisted.getImportOrder();
//        importOrder.setTotal(importOrder.getTotal() + detailImportOrder.getPrice() * (detailImportOrder.getQuantity() - detailImportOrderExisted.getQuantity()));
        double total = 0;
        for (DetailImportOrder d : importOrder.getDetailImportOrders()) {
            if (d.getId().getMaterialId().equals(materialId)) {
                total += detailImportOrder.getPrice() * detailImportOrder.getQuantity();
            } else {
                total += d.getPrice() * d.getQuantity();
            }
        }
        importOrder.setTotal(total);

        importOrderRepository.save(importOrder);

        Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(materialId, importOrder.getBranch().getBranchID());

        if (storage == null){
            storage = new Storage();
            storage.setMaterial(detailImportOrderExisted.getMaterial());
            storage.setBranch(importOrder.getBranch());
            storage.setQuantity(detailImportOrderExisted.getQuantity());
        } else {
            storage.setQuantity(storage.getQuantity() + (detailImportOrder.getQuantity() - detailImportOrderExisted.getQuantity()));
        }

        detailImportOrderExisted.setQuantity(detailImportOrder.getQuantity());
        detailImportOrderExisted.setPrice(detailImportOrder.getPrice());
        detailImportOrderExisted.setDescription(detailImportOrder.getDescription());

        detailImportOrderRepository.save(detailImportOrderExisted);

        return new APIResponse<>(toDetailImportOrderResponse(detailImportOrderExisted), "Update detail import order successfully", true);
    }

    public void deleteDetailImportOrder(DetailImportOrder detailImportOrder){
        detailImportOrderRepository.delete(detailImportOrder);
    }

    public void deleteDetailImportOrderById(DetailImportOrderId id){
        detailImportOrderRepository.deleteById(id);
    }

    public DetailImportOrderResponse toDetailImportOrderResponse(DetailImportOrder detailImportOrder){
        DetailImportOrderResponse detailImportOrderResponse = new DetailImportOrderResponse();
        detailImportOrderResponse.setMaterialID(detailImportOrder.getId().getMaterialId());
        detailImportOrderResponse.setName(detailImportOrder.getMaterial().getName());
        detailImportOrderResponse.setQuantity(detailImportOrder.getQuantity());
        detailImportOrderResponse.setPrice(detailImportOrder.getPrice());
        detailImportOrderResponse.setDescription(detailImportOrder.getDescription());

        return detailImportOrderResponse;
    }
}
