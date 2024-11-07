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

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

    public List<TimesheetResponse> getAllTimesheets() {
        List<Timesheet> timesheets = timesheetRepository.findAll();

        return timesheets.stream().map(timesheet -> {
            TimesheetResponse timesheetResponse = new TimesheetResponse();
            timesheetResponse.setTimesheetID(timesheet.getTimesheetID());
            timesheetResponse.setDate(timesheet.getDate());
            timesheetResponse.setShift(timesheet.getShift());
            timesheetResponse.setEmployeeID(timesheet.getEmployee().getEmployeeID());
            return timesheetResponse;
        }).collect(Collectors.toList());
    }

    public APIResponse<TimesheetResponse> createTimesheet(CreateTimesheetDTO timesheetDTO) {

        if (timesheetDTO.getEmployeeId() == null) {
            return new APIResponse<>(null, "Employee ID must not be null", false);
        }

//        if (timesheetRepository.existsByDate(timesheetDTO.getDate())) {
//            return null;
//        }

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
        timesheetResponse.setEmployeeID(newTimesheet.getEmployee().getEmployeeID());

        return new APIResponse<>(timesheetResponse, "Timesheet created successfully", true);

    }

    public TimesheetResponse getTimesheetById(Long id) {
        Timesheet timesheet = timesheetRepository.findById(id).orElse(null);
        if (timesheet == null) {
            return null;
        }

        TimesheetResponse timesheetResponse = new TimesheetResponse();
        timesheetResponse.setTimesheetID(timesheet.getTimesheetID());
        timesheetResponse.setDate(timesheet.getDate());
        timesheetResponse.setShift(timesheet.getShift());
        timesheetResponse.setEmployeeID(timesheet.getEmployee().getEmployeeID());

        return timesheetResponse;
    }

    public Timesheet updateTimesheet(Long timesheetId, UpdateTimesheetDTO timesheetDTO) {
        Timesheet timesheet = timesheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found!"));

        timesheet.setDate(timesheetDTO.getDate());
        timesheet.setShift(timesheetDTO.getShift());

        if (timesheetDTO.getDate() != null) {
            timesheet.setDate(timesheetDTO.getDate());
        }

        if (timesheetDTO.getShift() != null) {
            timesheet.setShift(timesheetDTO.getShift());
        }

        timesheetRepository.save(timesheet);
        return timesheet;
    }

    public boolean deleteTimesheetById(Long id) {
        if (!timesheetRepository.existsById(id)) {
            return false;
        }
        timesheetRepository.deleteById(id);
        return true;
    }
}
