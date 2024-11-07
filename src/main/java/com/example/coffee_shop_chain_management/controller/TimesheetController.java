package com.example.coffee_shop_chain_management.controller;

import com.example.coffee_shop_chain_management.dto.CreateTimesheetDTO;
import com.example.coffee_shop_chain_management.dto.UpdateTimesheetDTO;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import com.example.coffee_shop_chain_management.response.APIResponse;
import com.example.coffee_shop_chain_management.response.TimesheetResponse;
import com.example.coffee_shop_chain_management.service.TimesheetService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/timesheet")
public class TimesheetController {
    private final TimesheetService timesheetService;

    @Autowired
    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

    @GetMapping("/get/all")
    public List<TimesheetResponse> getAllTimesheets() {
        return timesheetService.getAllTimesheets();
    }

    @GetMapping("/get/{id}")
    public TimesheetResponse getTimesheetById(@PathVariable Long id) {
        return timesheetService.getTimesheetById(id);
    }

    @PostMapping("/create")
    public APIResponse<TimesheetResponse> createTimesheet(@RequestBody CreateTimesheetDTO timesheetDTO) {
        return timesheetService.createTimesheet(timesheetDTO);
    }

    @PutMapping("/update/{id}")
    public Timesheet updateTimesheet(@RequestBody UpdateTimesheetDTO timesheet, @PathVariable Long id) {
        return timesheetService.updateTimesheet(id, timesheet);
    }

    @DeleteMapping("/delete/{id}")
    public boolean deleteTimesheet(@PathVariable Long id) {
        return timesheetService.deleteTimesheetById(id);
    }
}
