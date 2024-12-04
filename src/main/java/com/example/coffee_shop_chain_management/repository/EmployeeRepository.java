package com.example.coffee_shop_chain_management.repository;

import com.example.coffee_shop_chain_management.entity.Employee;
import com.example.coffee_shop_chain_management.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByBranch_BranchID(Long branchID);
    Optional<Employee> findByEmail(String email);
}
