package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateExportOrderDTO;
import com.example.coffee_shop_chain_management.dto.DetailExportOrderDTO;
import com.example.coffee_shop_chain_management.dto.ProductDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.repository.ExportOrderRepository;
import com.example.coffee_shop_chain_management.repository.ProductRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.EmployeeResponse;
import com.example.coffee_shop_chain_management.response.ExportOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // GET
    public APIResponse<List<ExportOrderResponse>> getAllExportOrders() {
        List<ExportOrder> exportOrders = exportOrderRepository.findAll();
        return new APIResponse<>(exportOrders.stream().map(this::toExportOrderResponse).toList(), "Export orders retrieved successfully", true);
    }

    public APIResponse<ExportOrderResponse> getExportOrderById(Long id) {
        ExportOrder exportOrder = exportOrderRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Export order not found"));
        return new APIResponse<>(toExportOrderResponse(exportOrder), "Export order retrieved successfully", true);
    }


    public APIResponse<ExportOrderResponse> createExportOrder(CreateExportOrderDTO exportOrderDTO) {
        // Tạo đối tượng exportOrder mới
        ExportOrder exportOrder = new ExportOrder();

        // Thiết lập thông tin
        exportOrder.setPaymentMethod(exportOrderDTO.getPaymentMethod());
        exportOrder.setDate(LocalDateTime.now());

        double total = 0;

        // Tìm nhân viên theo id
        Employee employee = employeeRepository.findById(exportOrderDTO.getEmployeeID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        exportOrder.setEmployee(employee);

        // Tìm chi nhánh theo id
        Branch branch = branchRepository.findById(exportOrderDTO.getBranchID())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh"));
        exportOrder.setBranch(branch);

        // Khởi tạo danh sách ExportOrderDetail
        List<DetailExportOrder> detailExportOrders = new ArrayList<>();

        for (DetailExportOrderDTO detailExportOrderDTO : exportOrderDTO.getDetailExportOrders()) {
            DetailExportOrder detailExportOrder = new DetailExportOrder();


            // Tìm sản phẩm theo ten
            Product product = productRepository.findByName(detailExportOrderDTO.getProductName());

            DetailExportOrderId detailExportOrderId = new DetailExportOrderId();
            detailExportOrderId.setExportOrderId(exportOrder.getExportID());
            detailExportOrderId.setExportOrderId(product.getProductID());
            
            detailExportOrder.setId(detailExportOrderId);
            detailExportOrder.setProduct(product);
            detailExportOrder.setQuantity(detailExportOrderDTO.getQuantity());
            detailExportOrder.setExportOrder(exportOrder);

            detailExportOrder.setDescription("Default description");
            
            //detailExportOrder.setPrice(product.getPrice());

            // Tính tổng tiền
            total += product.getPrice() * detailExportOrderDTO.getQuantity();

            detailExportOrder.setPrice(total);

            detailExportOrders.add(detailExportOrder);
        }


        exportOrder.setTotal(total);
        exportOrder.setDetailExportOrders(detailExportOrders);

        // luu vao database
        ExportOrder newExportOrder = exportOrderRepository.save(exportOrder);

        // tao response
        ExportOrderResponse exportOrderResponse = toExportOrderResponse(newExportOrder);

        return new APIResponse<>(exportOrderResponse, "Export order created successfully", true);
    }


    public APIResponse<ExportOrderResponse> updateExportOrder(Long id, CreateExportOrderDTO exportOrderDTO) {
        ExportOrder exportOrder = exportOrderRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Export order not found"));

//        if (exportOrderDTO.getDate() != null) {
//            exportOrder.setDate(exportOrderDTO.getDate());
//        }
        if (exportOrderDTO.getPaymentMethod() != null) {
            exportOrder.setPaymentMethod(exportOrderDTO.getPaymentMethod());
        }

//        if (exportOrderDTO.getTotal() != null) {
//            exportOrder.setTotal(exportOrderDTO.getTotal());
//        }

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

    public APIResponse<ExportOrderResponse> deleteExportOrder(ExportOrder exportOrder) {
        if (!exportOrderRepository.existsById(exportOrder.getExportID())) {
            return new APIResponse<>(null, "Export order not found", false);
        }

        exportOrderRepository.delete(exportOrder);
        return new APIResponse<>(null, "Export order deleted successfully", true);
    }

    public APIResponse<ExportOrderResponse> deleteExportOrderById(Long id) {
        if (!exportOrderRepository.existsById(id)) {
            return new APIResponse<>(null, "Export order not found", false);
        }
        exportOrderRepository.deleteById(id);
        return new APIResponse<>(null, "Export order deleted successfully", true);
    }

    public ExportOrderResponse toExportOrderResponse(ExportOrder exportOrder) {
        ExportOrderResponse exportOrderResponse = new ExportOrderResponse();
        exportOrderResponse.setExportID(exportOrder.getExportID());
        exportOrderResponse.setDate(exportOrder.getDate());
        exportOrderResponse.setPaymentMethod(exportOrder.getPaymentMethod());
        exportOrderResponse.setTotal(exportOrder.getTotal());
        exportOrderResponse.setEmployeeID(exportOrder.getEmployee().getEmployeeID());
        exportOrderResponse.setBranchID(exportOrder.getBranch().getBranchID());

        return exportOrderResponse;
    }
}
