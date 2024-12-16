package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    boolean existsByDate(LocalDateTime date);
    boolean existsByEmployeeAndDate(Employee employee, LocalDateTime date);
    List<Timesheet> findByEmployee_Branch_BranchID(Long branchID);
    List<Timesheet> findByEmployee_EmployeeID(Long employeeID);

    // Get timesheet by month and year and employee id
    @Query(value = "SELECT * FROM timesheet t WHERE MONTH(t.date) = ?1 AND YEAR(t.date) = ?2 AND t.employee_id = ?3", nativeQuery = true)
    List<Timesheet> findTimesheetByMonthAndYearAndEmployeeID(int month, int year, Long employeeID);

}

