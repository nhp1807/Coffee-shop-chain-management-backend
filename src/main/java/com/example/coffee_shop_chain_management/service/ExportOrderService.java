package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateExportOrderDTO;
import com.example.coffee_shop_chain_management.entity.Branch;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.ExportOrder;
import com.example.coffee_shop_chain_management.repository.BranchRepository;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.repository.ExportOrderRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.EmployeeResponse;
import com.example.coffee_shop_chain_management.response.ExportOrderResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExportOrderService {
    @Autowired
    private ExportOrderRepository exportOrderRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private BranchRepository branchRepository;

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
        ExportOrder exportOrder = new ExportOrder();
        exportOrder.setDate(exportOrderDTO.getDate());
        exportOrder.setPaymentMethod(exportOrderDTO.getPaymentMethod());
        exportOrder.setTotal(exportOrderDTO.getTotal());

        Employee employee = employeeRepository.findById(exportOrderDTO.getEmployeeID()).orElseThrow(() -> new RuntimeException("Employee not found"));
        exportOrder.setEmployee(employee);

        Branch branch = branchRepository.findById(exportOrderDTO.getBranchID()).orElseThrow(() -> new RuntimeException("Branch not found"));
        exportOrder.setBranch(branch);

        ExportOrder newExportOrder = exportOrderRepository.save(exportOrder);

        return new APIResponse<>(toExportOrderResponse(newExportOrder), "Export order created successfully", true);
    }


    public APIResponse<ExportOrderResponse> updateExportOrder(Long id, CreateExportOrderDTO exportOrderDTO) {
        ExportOrder exportOrder = exportOrderRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Export order not found"));

        if (exportOrderDTO.getDate() != null) {
            exportOrder.setDate(exportOrderDTO.getDate());
        }
        if (exportOrderDTO.getPaymentMethod() != null) {
            exportOrder.setPaymentMethod(exportOrderDTO.getPaymentMethod());
        }

        if (exportOrderDTO.getTotal() != null) {
            exportOrder.setTotal(exportOrderDTO.getTotal());
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
