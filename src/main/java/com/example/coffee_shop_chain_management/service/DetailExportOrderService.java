package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.DetailExportOrderDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.*;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailExportOrderResponse;
import com.example.coffee_shop_chain_management.response.ExportOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DetailExportOrderService {
    @Autowired
    private DetailExportOrderRepository detailExportOrderRepository;

    @Autowired
    private ExportOrderRepository exportOrderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMaterialRepository productMaterialRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Transactional
    public APIResponse<DetailExportOrderResponse> addDetailExportOrder(Long id, DetailExportOrderDTO detailExportOrderDTO) {
        ExportOrder exportOrder = exportOrderRepository.findById(id).orElse(null);

        DetailExportOrder detailExportOrderExisted = detailExportOrderRepository.findDetailExportOrderByExportOrder_ExportIDAndProduct_ProductID(id, productRepository.findByName(detailExportOrderDTO.getProductName()).getProductID());

        if (exportOrder == null) {
            return new APIResponse<>(null, "Export order not found", false);
        }

        if (detailExportOrderExisted != null) {
            return new APIResponse<>(null, "Detail export order existed", false);
        }

        DetailExportOrder detailExportOrder = new DetailExportOrder();

        Product product = productRepository.findByName(detailExportOrderDTO.getProductName());

        if (product == null) {
            return new APIResponse<>(null, "Product not found", false);
        }

        List<ProductMaterial> productMaterials = productMaterialRepository.findByProduct_ProductID(product.getProductID());

        // Kiểm tra và trừ số lượng nguyên liệu trong kho
        for (ProductMaterial productMaterial : productMaterials) {
            Material material = productMaterial.getMaterial();
            Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), exportOrder.getBranch().getBranchID());
            // Kiểm tra số lượng nguyên liệu trong kho
            if (storage == null || storage.getQuantity() < productMaterial.getQuantity() * detailExportOrderDTO.getQuantity()) {
                return new APIResponse<>(null, "Not enough material in storage for product: " + product.getName(), false);
            }
            storage.setQuantity(storage.getQuantity() - productMaterial.getQuantity() * detailExportOrderDTO.getQuantity());
            storageRepository.save(storage);
        }

        DetailExportOrderId detailExportOrderId = new DetailExportOrderId();
        detailExportOrderId.setExportOrderId(exportOrder.getExportID());
        detailExportOrderId.setProductId(product.getProductID());
        detailExportOrder.setId(detailExportOrderId);
        detailExportOrder.setExportOrder(exportOrder);
        detailExportOrder.setProduct(product);
        detailExportOrder.setQuantity(detailExportOrderDTO.getQuantity());
        detailExportOrder.setDescription(detailExportOrderDTO.getDescription());

        exportOrder.getDetailExportOrders().add(detailExportOrder);
        exportOrder.setTotal(exportOrder.getTotal() + product.getPrice() * detailExportOrderDTO.getQuantity());

        exportOrderRepository.save(exportOrder);


        return new APIResponse<>(toDetailExportOrderResponse(detailExportOrder), "Add detail export order successfully", true);
    }

    public APIResponse<DetailExportOrderResponse> getDetailExportOrderById(Long exportOrderId, Long productId){
        DetailExportOrderId id = new DetailExportOrderId(exportOrderId, productId);

        DetailExportOrder detailExportOrder = detailExportOrderRepository.findById(id).orElse(null);

        if (detailExportOrder == null){
            return new APIResponse<>(null, "Detail export order not found", false);
        }

        return new APIResponse<>(toDetailExportOrderResponse(detailExportOrder), "Get detail export order successfully", true);
    }

    @Transactional
    public APIResponse<DetailExportOrderResponse> deleteDetailExportOrder(Long orderID, Long productID) {
        DetailExportOrder detailExportOrder = detailExportOrderRepository.findDetailExportOrderByExportOrder_ExportIDAndProduct_ProductID(orderID, productID);

        if (detailExportOrder == null) {
            return new APIResponse<>(null, "Detail export order not found", false);
        }

        ExportOrder exportOrder = detailExportOrder.getExportOrder();
        exportOrder.setTotal(exportOrder.getTotal() - detailExportOrder.getPrice()*detailExportOrder.getQuantity());

        List<ProductMaterial> productMaterials = productMaterialRepository.findByProduct_ProductID(productID);

        // Kiểm tra và cộng số lượng nguyên liệu trong kho
        for (ProductMaterial productMaterial : productMaterials) {
            Material material = productMaterial.getMaterial();
            Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), exportOrder.getBranch().getBranchID());
            storage.setQuantity(storage.getQuantity() + productMaterial.getQuantity() * detailExportOrder.getQuantity());
            storageRepository.save(storage);
        }

        exportOrder.getDetailExportOrders().remove(detailExportOrder);

        exportOrderRepository.save(exportOrder);
        detailExportOrderRepository.delete(detailExportOrder);

        return new APIResponse<>(toDetailExportOrderResponse(detailExportOrder), "Detail export order deleted successfully", true);
    }

    @Transactional
    public APIResponse<DetailExportOrderResponse> updateDetailExportOrder(Long exportOrderId, Long productId, DetailExportOrder detailExportOrder) {
        DetailExportOrderId id = new DetailExportOrderId(exportOrderId, productId);

        DetailExportOrder detailExportOrderExisted = detailExportOrderRepository.findById(id).orElse(null);

        if (detailExportOrderExisted == null) {
            return new APIResponse<>(null, "Detail export order not found", false);
        }

        ExportOrder exportOrder = detailExportOrder.getExportOrder();
        exportOrder.setTotal(exportOrder.getTotal() + detailExportOrderExisted.getProduct().getPrice() * (detailExportOrderExisted.getQuantity() - detailExportOrder.getQuantity()));

        detailExportOrder.setQuantity(detailExportOrder.getQuantity());
        detailExportOrder.setDescription(detailExportOrder.getDescription());

        exportOrder.setTotal(exportOrder.getTotal() + detailExportOrder.getProduct().getPrice() * detailExportOrder.getQuantity());

        exportOrderRepository.save(exportOrder);

        List<ProductMaterial> productMaterials = productMaterialRepository.findByProduct_ProductID(productId);

        // Kiểm tra và trừ số lượng nguyên liệu trong kho
        for (ProductMaterial productMaterial : productMaterials) {
            Material material = productMaterial.getMaterial();
            Storage storageMaterial = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), exportOrder.getBranch().getBranchID());
            // Kiểm tra số lượng nguyên liệu trong kho
            if (storageMaterial == null || storageMaterial.getQuantity() < productMaterial.getQuantity() * detailExportOrder.getQuantity()) {
                return new APIResponse<>(null, "Not enough material in storage for product: " + detailExportOrder.getProduct().getName(), false);
            }
            storageMaterial.setQuantity(storageMaterial.getQuantity() + productMaterial.getQuantity() * (detailExportOrderExisted.getQuantity() - detailExportOrder.getQuantity()));
            storageRepository.save(storageMaterial);
        }

        return new APIResponse<>(toDetailExportOrderResponse(detailExportOrder), "Update detail export order successfully", true);
    }

    public DetailExportOrderResponse toDetailExportOrderResponse(DetailExportOrder detailExportOrder) {
        DetailExportOrderResponse detailExportOrderResponse = new DetailExportOrderResponse();
        detailExportOrderResponse.setProductID(detailExportOrder.getProduct().getProductID());
        detailExportOrderResponse.setName(detailExportOrder.getProduct().getName());
        detailExportOrderResponse.setPrice(detailExportOrder.getProduct().getPrice());
        detailExportOrderResponse.setQuantity(detailExportOrder.getQuantity());
        detailExportOrderResponse.setDescription(detailExportOrder.getDescription());

        return detailExportOrderResponse;
    }
}
