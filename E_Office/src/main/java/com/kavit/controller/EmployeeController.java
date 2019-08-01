package com.kavit.controller;


import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kavit.model.EmpPassword;
import com.kavit.model.EmployeeTask;
import com.kavit.model.LeaveRequest;
import com.kavit.model.TempPassword;
import com.kavit.model.TrainingRoom;
import com.kavit.service.EmployeeService;


@RestController
@CrossOrigin (origins = "http://localhost:4200")
public class EmployeeController {
	
	@Autowired
	private EmployeeService emp;
	
	@PostMapping("/emp/login")
	public String empLogin(@RequestBody TempPassword newEmpLogin) {
		return emp.checkEmpLogin(newEmpLogin);
	}
	
	@PostMapping("/emp/login/{id}")
	public String updatePassword(@RequestBody TempPassword newEmpLogin, @PathVariable Long id) {
		return emp.updatePassword(newEmpLogin, id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/requestLeave/{id}")
	public String empRequestLeave(@RequestBody LeaveRequest newLeaveRequest, @PathVariable Long id){
		return emp.applyLeaveRequest(newLeaveRequest, id);
	}
	
	@GetMapping("/emp/requestLeave/{id}")
	public List<LeaveRequest> empGetRequestLeave(@PathVariable Long id){
		return emp.getLeaveRequest(id);
	}
	
	@DeleteMapping("/emp/requestLeave/{id}")
	public String empDelRequestLeave(@PathVariable Long id){
		return emp.deleteLeaveRequest(id);
	}
	
	@GetMapping("/emp/bookTrainingRoom")
	public List<TrainingRoom> empTrainingAvl(){
		return emp.sendAvlTrainingRoom();
	}
	
	@GetMapping("/emp/bookTrainingRoom/{id}")
	public List<TrainingRoom> empBookedTrainingRooms(@PathVariable Long id){
		return emp.sendBookedTrainingRoom(id);
	}
	
	@GetMapping("/emp/getTrainingRoom/{id}")
	public Optional<TrainingRoom> empBookedTrainingRoom(@PathVariable Long id){
		return emp.sendRoomDetails(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/bookTrainingRoom/{id}")
	public String empBookTraining(@RequestBody TrainingRoom newRoom, @PathVariable Long id){
		return emp.bookTrainingRoom(newRoom, id);
	
	}
	
	
	@GetMapping("/emp/finishTasks/{id}")
	public List<EmployeeTask> viewEmpTask(@PathVariable Long id){
		return emp.sendEmployeeTasks(id);
	}
	
	@GetMapping("/emp/finishTask/{id}")
	public Optional<EmployeeTask> updateEmpTask(@PathVariable Long id){
		return emp.sendEmployeeTask(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/finishTask/{id}")
	public String finishEmpTask(@RequestBody EmployeeTask empTask, @PathVariable Long id) {
		return emp.completeEmployeeTask(empTask, id);
	}
}
