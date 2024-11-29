package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateImportOrderDTO;
import com.example.coffee_shop_chain_management.dto.DetailImportOrderDTO;
import com.example.coffee_shop_chain_management.entity.*;
import com.example.coffee_shop_chain_management.repository.*;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.ImportOrderResponse;
import com.example.coffee_shop_chain_management.telegram_bot.NotificationBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class ImportOrderService {
    @Autowired
    private ImportOrderRepository importOrderRepository;

    @Autowired
    private DetailImportOrderRepository detailImportOrderRepository;

    @Autowired
    private SupplierRepository supplierRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private NotificationBot notificationBot;
    @Autowired
    private AccountRepository accountRepository;

    public List<ImportOrder> getAllImportOrders() {
        return importOrderRepository.findAll();
    }

    @Transactional
    public APIResponse<ImportOrderResponse> createImportOrder(CreateImportOrderDTO importOrderDTO) {
        // Tạo đối tượng ImportOrder mới
        ImportOrder importOrder = new ImportOrder();

        // Thiết lập các thuộc tính
        importOrder.setPaymentMethod(importOrderDTO.getPaymentMethod());
        importOrder.setDate(LocalDateTime.now());

        double total = 0d;

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

                    if (Objects.equals(b.getBranchID(), branch.getBranchID())) {
                        newStorage.setQuantity(detailDTO.getQuantity());
                        storageRepository.save(newStorage); // Lưu Storage mới
                        continue;
                    }

                    newStorage.setQuantity(0d);
                    storageRepository.save(newStorage); // Lưu Storage mới
                }
            } else {
                // Nếu nguyên liệu đã tồn tại, kiểm tra kho
                Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), importOrderDTO.getBranchId());

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
            DetailImportOrderId detailImportOrderId = new DetailImportOrderId();
            detailImportOrderId.setImportOrderId(importOrder.getImportID());
            detailImportOrderId.setMaterialId(material.getMaterialID());
            detailImportOrder.setId(detailImportOrderId);

            detailImportOrder.setMaterial(material); // Sử dụng nguyên liệu đã tìm thấy hoặc vừa tạo
            detailImportOrder.setQuantity(detailDTO.getQuantity());
            detailImportOrder.setImportOrder(importOrder);
            detailImportOrder.setPrice(detailDTO.getPrice());
            detailImportOrder.setDescription(detailDTO.getDescription());

            total += detailDTO.getQuantity() * detailDTO.getPrice();

            detailImportOrders.add(detailImportOrder);
        }

        // Thiết lập danh sách chi tiết vào đơn nhập
        importOrder.setDetailImportOrders(detailImportOrders);
        importOrder.setTotal(total);

        ImportOrder newImportOrder = importOrderRepository.save(importOrder);

        // Tạo đối tượng ImportOrderResponse để trả về
        ImportOrderResponse importOrderResponse = new ImportOrderResponse();
        importOrderResponse.setImportID(newImportOrder.getImportID());
        importOrderResponse.setTotal(newImportOrder.getTotal());
        importOrderResponse.setPaymentMethod(newImportOrder.getPaymentMethod());
        importOrderResponse.setDate(newImportOrder.getDate().toString());
        importOrderResponse.setSupplierId(newImportOrder.getSupplier().getSupplierID());
        importOrderResponse.setBranchId(newImportOrder.getBranch().getBranchID());

        // Gửi thông báo tới Telegram
        StringBuilder message = new StringBuilder();
        message.append("New import order created\n");
        message.append("Import ID: ").append(importOrder.getImportID()).append("\n");
        message.append("Branch ID: ").append(importOrder.getBranch().getBranchID()).append("\n");
        message.append("Supplier ID: ").append(importOrder.getSupplier().getSupplierID()).append("\n");
        message.append("Total: ").append(importOrder.getTotal()).append("\n");

        Optional<Account> adminAccount = accountRepository.findByRole("ADMIN");

        if (adminAccount.isEmpty()) {
            return new APIResponse<>(null, "Admin account not found", false);
        }

        String adminTelegramId = adminAccount.get().getChatID();
        notificationBot.sendMessage(message.toString(), adminTelegramId);

        return new APIResponse<>(importOrderResponse, "Import order created successfully", true);
    }

    @Transactional
    public APIResponse<ImportOrderResponse> confirmImportOrder(Long id) {
        ImportOrder importOrder = importOrderRepository.findById(id).orElse(null);

        if (importOrder == null) {
            return new APIResponse<>(null, "Import order not found", false);
        }

        importOrder.setStatus(true);
        importOrderRepository.save(importOrder);

        StringBuilder message = new StringBuilder();
        message.append("Import order confirmed\n");
        message.append("Import ID: ").append(importOrder.getImportID()).append("\n");
        message.append("Branch ID: ").append(importOrder.getBranch().getBranchID()).append("\n");
        message.append("Supplier ID: ").append(importOrder.getSupplier().getSupplierID()).append("\n");
        message.append("Total: ").append(importOrder.getTotal()).append("\n");

        Optional<Account> brandAccount = accountRepository.findByBranch_BranchID(importOrder.getBranch().getBranchID());

        if (brandAccount.isEmpty()) {
            return new APIResponse<>(null, "Brand account not found", false);
        }

        String brandTelegramId = brandAccount.get().getChatID();
        notificationBot.sendMessage(message.toString(), brandTelegramId);

        return new APIResponse<>(null, "Import order confirmed successfully", true);
    }

    public APIResponse<List<ImportOrderResponse>> getImportOrderByStatus(Boolean status) {
        List<ImportOrder> importOrders = importOrderRepository.findByStatus(status);
        List<ImportOrderResponse> importOrderResponses = new ArrayList<>();

        for (ImportOrder importOrder : importOrders) {
            importOrderResponses.add(toImportOrderRespone(importOrder));
        }

        return new APIResponse<>(importOrderResponses, "Import orders not found", false);
    }

    public APIResponse<ImportOrderResponse> getImportOrderById(Long id) {
        ImportOrder importOrder = importOrderRepository.findById(id).orElse(null);

        return new APIResponse<>(toImportOrderRespone(importOrder), "Import order not found", false);
    }

    @Transactional
    public APIResponse<ImportOrderResponse> addDetailImportOrder(Long id, DetailImportOrderDTO detailImportOrderDTO){
        ImportOrder importOrder = importOrderRepository.findById(id).orElse(null);

        if (importOrder == null) {
            return new APIResponse<>(null, "Import order not found", false);
        }

        double total = importOrder.getTotal();

        Material material = materialRepository.findByName(detailImportOrderDTO.getMaterialName());

        if (material == null) {
            material = new Material();
            material.setName(detailImportOrderDTO.getMaterialName());
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
        } else {
            Storage storage = storageRepository.findByMaterial_MaterialIDAndBranch_BranchID(material.getMaterialID(), importOrder.getBranch().getBranchID());

            if (storage != null) {
                storage.setQuantity(storage.getQuantity() + detailImportOrderDTO.getQuantity());
                storageRepository.save(storage);
            } else {
                Storage newStorage = new Storage();
                newStorage.setMaterial(material);
                newStorage.setQuantity(detailImportOrderDTO.getQuantity());
                newStorage.setBranch(importOrder.getBranch());
                storageRepository.save(newStorage);
            }
        }

        DetailImportOrder detailImportOrder = detailImportOrderRepository.findDetailImportOrderByImportOrder_ImportIDAndMaterial_MaterialID(importOrder.getImportID(), material.getMaterialID());

        if (detailImportOrder != null) {
            detailImportOrder.setQuantity(detailImportOrder.getQuantity() + detailImportOrderDTO.getQuantity());
            detailImportOrderRepository.save(detailImportOrder);
        } else {
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

            List<DetailImportOrder> detailImportOrders = importOrder.getDetailImportOrders();
            detailImportOrders.add(detailImportOrder);
            importOrder.setDetailImportOrders(detailImportOrders);

            importOrderRepository.save(importOrder);
        }

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

        List<DetailImportOrder> detailImportOrders = importOrder.getDetailImportOrders();
        detailImportOrders.remove(detailImportOrder);
        importOrder.setDetailImportOrders(detailImportOrders);

        importOrderRepository.save(importOrder);
        detailImportOrderRepository.delete(detailImportOrder);

        return new APIResponse<>(null, "Detail import order deleted successfully", true);
    }

    @Transactional
    public APIResponse<ImportOrderResponse> deleteImportOrder(ImportOrder importOrder) {
        if (!importOrderRepository.existsById(importOrder.getImportID())) {
            return new APIResponse<>(null, "Import order not found", false);
        }

        importOrderRepository.delete(importOrder);
        return new APIResponse<>(null, "Import order deleted successfully", true);
    }

    @Transactional
    public APIResponse<ImportOrderResponse> deleteImportOrderById(Long id) {
        if (!importOrderRepository.existsById(id)) {
            return new APIResponse<>(null, "Import order not found", false);
        }

        importOrderRepository.deleteById(id);
        return new APIResponse<>(null, "Import order deleted successfully", true);
    }

    public ImportOrderResponse toImportOrderRespone(ImportOrder importOrder){
        ImportOrderResponse importOrderResponse = new ImportOrderResponse();
        importOrderResponse.setImportID(importOrder.getImportID());
        importOrderResponse.setTotal(importOrder.getTotal());
        importOrderResponse.setPaymentMethod(importOrder.getPaymentMethod());
        importOrderResponse.setDate(importOrder.getDate().toString());
        importOrderResponse.setSupplierId(importOrder.getSupplier().getSupplierID());
        importOrderResponse.setBranchId(importOrder.getBranch().getBranchID());

        return importOrderResponse;
    }
}
