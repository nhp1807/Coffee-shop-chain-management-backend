package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.DetailImportOrder;
import com.example.coffee_shop_chain_management.entity.DetailImportOrderId;
import com.example.coffee_shop_chain_management.entity.ImportOrder;
import com.example.coffee_shop_chain_management.entity.Storage;
import com.example.coffee_shop_chain_management.repository.DetailImportOrderRepository;
import com.example.coffee_shop_chain_management.repository.ImportOrderRepository;
import com.example.coffee_shop_chain_management.repository.StorageRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailImportOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetailImportOrderService {
    @Autowired
    private DetailImportOrderRepository detailImportOrderRepository;

    @Autowired
    private ImportOrderRepository importOrderRepository;

    @Autowired
    private StorageRepository storageRepository;

    public List<DetailImportOrder> getAllDetailImportOrders(){
        return detailImportOrderRepository.findAll();
    }

    public DetailImportOrder createDetailImportOrder(DetailImportOrder detailImportOrder){
        return detailImportOrderRepository.save(detailImportOrder);
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
        importOrder.setTotal(detailImportOrderExisted.getPrice() * detailImportOrderExisted.getQuantity());
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
