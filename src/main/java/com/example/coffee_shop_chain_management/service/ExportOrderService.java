package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateExportOrderDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.*;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.DetailExportOrderResponse;
import com.example.coffee_shop_chain_management.response.ExportOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ExportOrderService {
    @Autowired
    private ExportOrderRepository exportOrderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BranchRepository branchRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private StorageRepository storageRepository;
    @Autowired
    private ProductMaterialRepository productMaterialRepository;
    @Autowired
    private DetailExportOrderRepository detailExportOrderRepository;

    // GET
    public APIResponse<List<ExportOrderResponse>> getAllExportOrders() {
        List<ExportOrder> exportOrders = exportOrderRepository.findAll();
        return new APIResponse<>(exportOrders.stream().map(this::toExportOrderResponse).toList(), "Export orders retrieved successfully", true);
    }

    public APIResponse<ExportOrderResponse> getExportOrderById(Long id) {
        Optional<ExportOrder> exportOrder = exportOrderRepository.findById(id);
        if (!exportOrder.isPresent())
            return new APIResponse<>(null, "Export order not found", false);

        return new APIResponse<>(toExportOrderResponse(exportOrder.get()), "Export order retrieved successfully", true);
    }

    public APIResponse<List<ExportOrderResponse>> getExportOrderByBranchId(Long branchID) {
        List<ExportOrder> exportOrders = exportOrderRepository.findByBranch_BranchID(branchID);

        return new APIResponse<>(exportOrders.stream().map(this::toExportOrderResponse).toList(), "Export orders retrieved successfully", true);
    }

    @Transactional
    public APIResponse<ExportOrderResponse> createExportOrder(CreateExportOrderDTO exportOrderDTO) {
        // Tạo đối tượng exportOrder mới
        ExportOrder exportOrder = new ExportOrder();

        // Thiết lập thông tin
        exportOrder.setPaymentMethod(exportOrderDTO.getPaymentMethod());
        exportOrder.setDate(LocalDateTime.now());

        double total = 0;

        // Tìm nhân viên theo id
        Optional<Employee> employee = employeeRepository.findById(exportOrderDTO.getEmployeeID());
        if (employee.isEmpty()){
            return new APIResponse<>(null, "Employee not found", false);
        }
        exportOrder.setEmployee(employee.get());

        // Tìm chi nhánh theo id
        Optional<Branch> branch = branchRepository.findById(exportOrderDTO.getBranchID());
        if (branch.isEmpty()){
            return new APIResponse<>(null, "Branch not found", false);
        }
        exportOrder.setBranch(branch.get());

        // Khởi tạo danh sách ExportOrderDetail
        List<DetailExportOrder> detailExportOrders = new ArrayList<>();

//        for (DetailExportOrderDTO detailExportOrderDTO : exportOrderDTO.getDetailExportOrders()) {
//            DetailExportOrder detailExportOrder = new DetailExportOrder();
//
//            // Tìm sản phẩm theo tên
//            Product product = productRepository.findByName(detailExportOrderDTO.getProductName());
//
//            // Lấy danh sách product_material
//            List<ProductMaterial> productMaterials = productMaterialRepository.findByProduct_ProductID(product.getProductID());
//
//            // Kiểm tra và trừ số lượng nguyên liệu trong kho
//            for (ProductMaterial productMaterial : productMaterials) {
//                Material material = productMaterial.getMaterial();
//                Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), branch.get().getBranchID());
//                // Kiểm tra số lượng nguyên liệu trong kho
//                if (storage == null || storage.getQuantity() < productMaterial.getQuantity() * detailExportOrderDTO.getQuantity()) {
//                    return new APIResponse<>(null, "Not enough material in storage for product: " + product.getName(), false);
//                }
//            }
//
//            // Trừ số lượng nguyên liệu trong kho
//            for (ProductMaterial productMaterial : productMaterials) {
//                Material material = productMaterial.getMaterial();
//                Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), branch.get().getBranchID());
//                storage.setQuantity(storage.getQuantity() - productMaterial.getQuantity() * detailExportOrderDTO.getQuantity());
//                storageRepository.save(storage);
//            }
//
//
//            DetailExportOrderId detailExportOrderId = new DetailExportOrderId();
//            detailExportOrderId.setExportOrderId(exportOrder.getExportID());
//            detailExportOrderId.setExportOrderId(product.getProductID());
//
//            detailExportOrder.setId(detailExportOrderId);
//            detailExportOrder.setProduct(product);
//            detailExportOrder.setQuantity(detailExportOrderDTO.getQuantity());
//            detailExportOrder.setExportOrder(exportOrder);
//
//            detailExportOrder.setDescription("Default description");
//
//            //detailExportOrder.setPrice(product.getPrice());
//
//            // Tính tổng tiền
//            total += product.getPrice() * detailExportOrderDTO.getQuantity();
//
//            detailExportOrder.setPrice(total);
//
//            detailExportOrders.add(detailExportOrder);
//        }

        exportOrder.setTotal(total);
        exportOrder.setDetailExportOrders(detailExportOrders);

        // luu vao database
        ExportOrder newExportOrder = exportOrderRepository.save(exportOrder);

        // tao response
        ExportOrderResponse exportOrderResponse = toExportOrderResponse(newExportOrder);

        return new APIResponse<>(exportOrderResponse, "Export order created successfully", true);
    }

    @Transactional
    public APIResponse<ExportOrderResponse> updateExportOrder(Long id, CreateExportOrderDTO exportOrderDTO) {
        Optional<ExportOrder> exportOrderExited = exportOrderRepository.findById(id);
        if(!exportOrderExited.isPresent()){
            return new APIResponse<>(null, "Export order not found", false);
        }

        ExportOrder exportOrder = exportOrderExited.get();

        if (exportOrderDTO.getPaymentMethod() != null) {
            exportOrder.setPaymentMethod(exportOrderDTO.getPaymentMethod());
        }


        if (exportOrderDTO.getEmployeeID() != null) {
            Employee employee = employeeRepository.findById(exportOrderDTO.getEmployeeID()).orElseThrow(() -> new RuntimeException("Employee not found"));
            exportOrder.setEmployee(employee);
        }

        if (exportOrderDTO.getBranchID() != null) {
            Branch branch = branchRepository.findById(exportOrderDTO.getBranchID()).orElseThrow(() -> new RuntimeException("Branch not found"));
            exportOrder.setBranch(branch);
        }

        exportOrderRepository.save(exportOrder);
        return new APIResponse<>(toExportOrderResponse(exportOrder), "Export order updated successfully", true);

    }

    @Transactional
    public APIResponse<ExportOrderResponse> deleteExportOrder(ExportOrder exportOrder) {
        if (!exportOrderRepository.existsById(exportOrder.getExportID())) {
            return new APIResponse<>(null, "Export order not found", false);
        }

        exportOrderRepository.delete(exportOrder);
        return new APIResponse<>(null, "Export order deleted successfully", true);
    }

    @Transactional
    public APIResponse<ExportOrderResponse> deleteExportOrderById(Long id) {
        if (!exportOrderRepository.existsById(id)) {
            return new APIResponse<>(null, "Export order not found", false);
        }
        exportOrderRepository.deleteById(id);
        return new APIResponse<>(null, "Export order deleted successfully", true);
    }

    @Transactional
    public APIResponse<ExportOrderResponse> deleteDetailExportOrder(Long orderID, Long productID) {
        DetailExportOrder detailExportOrder = detailExportOrderRepository.findDetailExportOrderByExportOrder_ExportIDAndProduct_ProductID(orderID, productID);

        if (detailExportOrder == null) {
            return new APIResponse<>(null, "Detail export order not found", false);
        }

        ExportOrder exportOrder = detailExportOrder.getExportOrder();
        exportOrder.setTotal(exportOrder.getTotal() - detailExportOrder.getPrice()*detailExportOrder.getQuantity());
        Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(detailExportOrder.getProduct().getProductID(), exportOrder.getBranch().getBranchID());
        storage.setQuantity(storage.getQuantity() + detailExportOrder.getQuantity());

        List<DetailExportOrder> detailExportOrders = exportOrder.getDetailExportOrders();
        detailExportOrders.remove(detailExportOrder);
        exportOrder.setDetailExportOrders(detailExportOrders);

        exportOrderRepository.save(exportOrder);
        detailExportOrderRepository.delete(detailExportOrder);
        storageRepository.save(storage);

        return new APIResponse<>(toExportOrderResponse(exportOrder), "Detail export order deleted successfully", true);
    }

    public ExportOrderResponse toExportOrderResponse(ExportOrder exportOrder) {
        ExportOrderResponse exportOrderResponse = new ExportOrderResponse();
        exportOrderResponse.setExportID(exportOrder.getExportID());
        exportOrderResponse.setDate(exportOrder.getDate());
        exportOrderResponse.setPaymentMethod(exportOrder.getPaymentMethod());
        exportOrderResponse.setTotal(exportOrder.getTotal());
        exportOrderResponse.setEmployeeID(exportOrder.getEmployee().getEmployeeID());
        exportOrderResponse.setBranchID(exportOrder.getBranch().getBranchID());
        exportOrderResponse.setDetailExportOrders(exportOrder.getDetailExportOrders().stream().map(detailExportOrder -> {
            DetailExportOrderResponse detailExportOrderResponse = new DetailExportOrderResponse();
            detailExportOrderResponse.setProductID(detailExportOrder.getProduct().getProductID());
            detailExportOrderResponse.setName(detailExportOrder.getProduct().getName());
            detailExportOrderResponse.setQuantity(detailExportOrder.getQuantity());
            detailExportOrderResponse.setDescription(detailExportOrder.getDescription());
            detailExportOrderResponse.setPrice(detailExportOrder.getPrice());
            return detailExportOrderResponse;
        }).toList());

        return exportOrderResponse;
    }
}
