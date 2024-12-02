package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    boolean existsByDate(LocalDateTime date);
    boolean existsByEmployeeAndDate(Employee employee, LocalDateTime date);

}

