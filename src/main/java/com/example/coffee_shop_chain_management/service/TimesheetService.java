package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.dto.CreateTimesheetDTO;
import com.example.coffee_shop_chain_management.dto.UpdateTimesheetDTO;
import com.example.coffee_shop_chain_management.emails.SendOTP;
import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import com.example.coffee_shop_chain_management.repository.EmployeeRepository;
import com.example.coffee_shop_chain_management.repository.TimesheetRepository;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.SalaryResponse;
import com.example.coffee_shop_chain_management.response.TimesheetResponse;
import jakarta.transaction.Transactional;
import com.example.coffee_shop_chain_management.telegram_bot.NotificationBot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    @Autowired
    private SendOTP sendOTP;

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

    public APIResponse<List<SalaryResponse>> calculateSalaryByMonth(int month, int year) {
        List<Employee> employees = employeeRepository.findAll();
        List<SalaryResponse> salaryResponses = new ArrayList<>();

        for (Employee employee : employees) {
            List<Timesheet> timesheets = timesheetRepository.findTimesheetByMonthAndYearAndEmployeeID(month, year, employee.getEmployeeID());
            SalaryResponse salaryResponse = new SalaryResponse();
            salaryResponse.setEmployeeID(employee.getEmployeeID());
            salaryResponse.setChatID(employee.getChatID());
            salaryResponse.setEmail(employee.getEmail());
            salaryResponse.setShiftSalary(employee.getShiftSalary());
            salaryResponse.setTotalShifts((double) timesheets.size());
            salaryResponse.setTotalSalary(employee.getShiftSalary() * timesheets.size());
            salaryResponse.setTimesheets(timesheets.stream().map(this::toTimesheetResponse).toList());

            salaryResponses.add(salaryResponse);

            StringBuilder message = new StringBuilder();
            message.append("Salary for " + month + "/" + year + ":\n");
            message.append("Total shifts: " + salaryResponse.getTotalShifts() + "\n");
            message.append("Shift salary: " + salaryResponse.getShiftSalary() + "\n");
            message.append("Total salary: " + salaryResponse.getTotalSalary() + "\n");
            message.append("We will send the detailed timesheet to your email: " + salaryResponse.getEmail());

            // Create excel file from timesheets
            String filePath = employee.getName() + "_" + employee.getEmployeeID() + "_" + month + "_" + year + "_timesheet.xlsx";
            try {
                File excelFile = ExcelExporter.exportTimesheetsToExcel(timesheets, filePath);

                // Send email
                sendOTP.sendEmailWithAttachment(salaryResponse.getEmail(), "Timesheet for " + month + "/" + year, "Here is your timesheets for this month", excelFile);

                // Send telegram message
                notificationBot.sendMessage(message.toString(), employee.getChatID());

                // Remove file after sending
                excelFile.delete();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return new APIResponse<>(salaryResponses, "Salary calculated successfully", true);
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
