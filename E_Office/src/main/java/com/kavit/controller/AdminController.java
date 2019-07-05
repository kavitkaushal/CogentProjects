package com.kavit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kavit.dao.DepartmentRepo;
import com.kavit.dao.EmployeeRepo;
import com.kavit.model.Department;
import com.kavit.model.Employee;

@RestController
public class AdminController {

	@Autowired
	private EmployeeRepo employee;
	@Autowired
	private DepartmentRepo department;
	
	@GetMapping("/")
	String adminLogin() {
		return "Welcome Admin. Go to \\\"localhost:8080/addDept\\\" to add an department, "
				+ "or \"localhost:8080/addEmp\" to add an employee";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addDept")
	String add(@RequestBody Department newDept) {
		department.save(newDept);
		return "New department added";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addEmp")
	String add(@RequestBody Employee newEmp) {
		employee.save(newEmp);
		return "New employee added";
	}
}
