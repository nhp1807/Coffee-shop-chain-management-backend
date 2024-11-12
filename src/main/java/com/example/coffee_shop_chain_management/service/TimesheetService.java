package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateTimesheetDTO;
import com.example.coffee_shop_chain_management.dto.UpdateTimesheetDTO;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.repository.TimesheetRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.TimesheetResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public APIResponse<List<TimesheetResponse>> getAllTimesheets() {
        List<Timesheet> timesheets = timesheetRepository.findAll();
         return new APIResponse<>(timesheets.stream().map(this::toTimesheetResponse).toList(), "Timesheets retrieved successfully", true);
    }

    public APIResponse<TimesheetResponse> createTimesheet(CreateTimesheetDTO timesheetDTO) {

        if (timesheetRepository.existsByDate(timesheetDTO.getDate())) {
            return new APIResponse<>(null, "Timesheet already exists", false);
        }


        Timesheet timesheet = new Timesheet();
        timesheet.setDate(timesheetDTO.getDate());
        timesheet.setShift(timesheetDTO.getShift());

        // Tìm employee theo id
        Employee employee = employeeRepository.findById(timesheetDTO.getEmployeeId()).orElse(null);
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
        timesheetResponse.setEmployeeId(newTimesheet.getEmployee().getEmployeeID());

        return new APIResponse<>(timesheetResponse, "Timesheet created successfully", true);

    }

    public APIResponse<TimesheetResponse> getTimesheetById(Long id) {
        Timesheet timesheet = timesheetRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Timesheet not found!"));

        return new APIResponse<>(toTimesheetResponse(timesheet), "Timesheet retrieved successfully", true);
    }

    public APIResponse<TimesheetResponse> updateTimesheet(Long timesheetId, UpdateTimesheetDTO timesheetDTO) {
        Timesheet timesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found!"));

        if (timesheetDTO.getDate() != null) {
            timesheet.setDate(timesheetDTO.getDate());
        }

        if (timesheetDTO.getShift() != null) {
            timesheet.setShift(timesheetDTO.getShift());
        }

        timesheetRepository.save(timesheet);
        return new APIResponse<>(toTimesheetResponse(timesheet), "Timesheet updated successfully", true);
    }

    public APIResponse<TimesheetResponse> deleteTimesheet(Timesheet timesheet) {
        if (!timesheetRepository.existsById(timesheet.getTimesheetID())) {
            return new APIResponse<>(null, "Timesheet not found", false);
        }
        timesheetRepository.delete(timesheet);
        return new APIResponse<>(null, "Timesheet deleted successfully", true);
    }

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
        timesheetResponse.setEmployeeId(timesheet.getEmployee().getEmployeeID());
        return timesheetResponse;
    }
}
