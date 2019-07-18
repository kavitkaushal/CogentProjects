package com.kavit.controller;


import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kavit.model.EmployeeTask;
import com.kavit.model.LeaveRequest;
import com.kavit.model.TrainingRoom;
import com.kavit.service.EmployeeService;


@RestController
public class EmployeeController {
	
	@Autowired
	private EmployeeService emp;
	
	@GetMapping("/emp")
	public String empLogin() {
		return emp.welcomeMessage();
	}
	
	@GetMapping("/emp/bookTrainingRoom")
	public List<TrainingRoom> empTrainingAvl(){
		return emp.sendAvlTrainingRoom();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/bookTrainingRoom")
	public String empBookTraining(@RequestBody TrainingRoom newRoom){
		emp.bookTrainingRoom(newRoom);
		return "Training Room Booked";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/requestLeave")
	public String empRequestLeave(@RequestBody LeaveRequest newLeaveRequest){
		emp.applyLeaveRequest(newLeaveRequest);
		return "Leave Request Sent";
	}
	
	@GetMapping("/emp/finishTask")
	public List<EmployeeTask> viewEmpTask(){
		return emp.sendEmployeeTask();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/finishTask")
	public String finishEmpTask(@RequestBody EmployeeTask empTask) {
		emp.completeEmployeeTask(empTask);
		return "Your assigned has been changed.";
	}
}
