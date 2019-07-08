package com.kavit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.kavit.model.Employee;

public interface EmployeeRepo extends JpaRepository<Employee, Long>{

}
