package com.example.coffee_shop_chain_management.service;

import com.example.coffee_shop_chain_management.entity.Timesheet;
import com.example.coffee_shop_chain_management.repository.TimesheetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.util.List;

@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;

    public List<Timesheet> getAllTimesheets() {
        return timesheetRepository.findAll();
    }

    public Timesheet createTimesheet(Timesheet timesheet) {
        return timesheetRepository.save(timesheet);
    }

    public Timesheet getTimesheetById(Long id) {
        return timesheetRepository.findById(id).orElse(null);
    }

    public Timesheet updateTimesheet(Timesheet timesheet) {
        return timesheetRepository.save(timesheet);
    }

    public void deleteTimesheet(Timesheet timesheet) {
        timesheetRepository.delete(timesheet);
    }
}
