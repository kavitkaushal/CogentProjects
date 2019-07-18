package com.kavit.controller;

import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kavit.model.Department;
import com.kavit.model.Employee;
import com.kavit.model.EmployeeTask;
import com.kavit.model.LeaveRequest;
import com.kavit.model.TrainingRoom;
import com.kavit.service.AdminService;

@RestController
public class AdminController {

	@Autowired
	private AdminService admin;
	
	@GetMapping("/")
	String adminLogin() {
		return admin.adminWelcomeMessage();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addDept")
	public String addDept(@RequestBody Department newDept) {
		admin.addnewDepartment(newDept);
		return "New department added";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addEmp")
	public String addNewEmp(@RequestBody Employee newEmp) {
		admin.addNewEmployee(newEmp);
		return "New employee added";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addTask")
	public String addEmpTask(@RequestBody EmployeeTask newTask) {
		admin.addNewTask(newTask);
		return "New Task added for employee";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addTrainingRoom")
	public String addTrainingRoom(@RequestBody TrainingRoom newRoom) {
		admin.addNewTrainingRoom(newRoom);
		return "New Training Room added";
	}
	
	@GetMapping("/approveLeave")
	public List<LeaveRequest> viewLeaveRequest(){
		return admin.viewLeaveRequest();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/approveLeave")
	public String approveLeaveRequest(@RequestBody LeaveRequest newLeaveRequest) {
		admin.approveLeaveRequest(newLeaveRequest);
		return "Leave Request Approved";
	}
}
