package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateTimesheetDTO;
import com.example.coffee_shop_chain_management.dto.UpdateTimesheetDTO;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.repository.TimesheetRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.TimesheetResponse;
import jakarta.transaction.Transactional;
import com.example.coffee_shop_chain_management.telegram_bot.NotificationBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private NotificationBot notificationBot;

    public APIResponse<List<TimesheetResponse>> getAllTimesheets() {
        List<Timesheet> timesheets = timesheetRepository.findAll();
         return new APIResponse<>(timesheets.stream().map(this::toTimesheetResponse).toList(), "Timesheets retrieved successfully", true);
    }

    @Transactional
    public APIResponse<TimesheetResponse> createTimesheet(CreateTimesheetDTO timesheetDTO) {
        LocalDateTime date = LocalDateTime.now();
        // chuyen sang dinh dang dd/MM/yyyy HH:mm:ss
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDate = date.format(formatter);

        Timesheet timesheet = new Timesheet();
        timesheet.setDate(date);
        timesheet.setShift(timesheetDTO.getShift());

        // Tìm employee theo id
        Employee employee = employeeRepository.findById(timesheetDTO.getEmployeeID()).orElse(null);
        if (employee == null) {
            return new APIResponse<>(null, "Employee not found", false);
        }

        timesheet.setEmployee(employee);

        // Lưu timesheet vào database
        Timesheet newTimesheet = timesheetRepository.save(timesheet);

        // Tao response
        TimesheetResponse timesheetResponse = new TimesheetResponse();
        timesheetResponse.setTimesheetID(newTimesheet.getTimesheetID());
        timesheetResponse.setDate(newTimesheet.getDate());
        timesheetResponse.setShift(newTimesheet.getShift());
        timesheetResponse.setEmployeeID(newTimesheet.getEmployee().getEmployeeID());

        // Chuyen

        notificationBot.sendMessage("You have checked in at " + formattedDate + " with shift " + timesheet.getShift(), employee.getChatID());

        return new APIResponse<>(timesheetResponse, "Timesheet created successfully", true);
    }

    public APIResponse<TimesheetResponse> getTimesheetById(Long id) {
        Optional<Timesheet> timesheet = timesheetRepository.findById(id);

        if (!timesheet.isPresent()) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }

        return new APIResponse<>(toTimesheetResponse(timesheet.get()), "Timesheet retrieved successfully", true);
    }

    public APIResponse<List<TimesheetResponse>> getTimesheetByBranchId(Long branchId) {
        List<Timesheet> timesheets = timesheetRepository.findByEmployee_Branch_BranchID(branchId);

        if (timesheets.isEmpty()) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }

        return new APIResponse<>(timesheets.stream().map(this::toTimesheetResponse).toList(), "Timesheet retrieved successfully", true);
    }

    public APIResponse<List<TimesheetResponse>> getTimesheetsByEmployeeId(Long employeeId) {
        List<Timesheet> timesheets = timesheetRepository.findByEmployee_EmployeeID(employeeId);

        if (timesheets.isEmpty()) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }

        return new APIResponse<>(timesheets.stream().map(this::toTimesheetResponse).toList(), "Timesheet retrieved successfully", true);
    }

    @Transactional
    public APIResponse<TimesheetResponse> updateTimesheet(Long timesheetId, UpdateTimesheetDTO timesheetDTO) {
        Optional<Timesheet> timesheetExisted = timesheetRepository.findById(timesheetId);

        if (!timesheetExisted.isPresent()) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }

        Timesheet timesheet = timesheetExisted.get();

        if (timesheetDTO.getDate() != null) {
            timesheet.setDate(timesheetDTO.getDate());
        }

        if (timesheetDTO.getShift() != null) {
            timesheet.setShift(timesheetDTO.getShift());
        }

        timesheetRepository.save(timesheet);
        return new APIResponse<>(toTimesheetResponse(timesheet), "Timesheet updated successfully", true);
    }

    @Transactional
    public APIResponse<TimesheetResponse> deleteTimesheet(Timesheet timesheet) {
        if (!timesheetRepository.existsById(timesheet.getTimesheetID())) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }
        timesheetRepository.delete(timesheet);
        return new APIResponse<>(null, "Timesheet deleted successfully", true);
    }

    @Transactional
    public APIResponse<TimesheetResponse> deleteTimesheetById(Long id) {
        if (!timesheetRepository.existsById(id)) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }
        timesheetRepository.deleteById(id);
        return new APIResponse<>(null, "Timesheet deleted successfully", true);
    }

    public TimesheetResponse toTimesheetResponse(Timesheet timesheet) {
        TimesheetResponse timesheetResponse = new TimesheetResponse();
        timesheetResponse.setTimesheetID(timesheet.getTimesheetID());
        timesheetResponse.setDate(timesheet.getDate());
        timesheetResponse.setShift(timesheet.getShift());
        timesheetResponse.setEmployeeID(timesheet.getEmployee().getEmployeeID());
        timesheetResponse.setBranchID(timesheet.getEmployee().getBranch().getBranchID());
        return timesheetResponse;
    }
}
