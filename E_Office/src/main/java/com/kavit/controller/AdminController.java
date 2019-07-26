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

import com.kavit.model.Department;
import com.kavit.model.Employee;
import com.kavit.model.EmployeeTask;
import com.kavit.model.LeaveRequest;
import com.kavit.model.TrainingRoom;
import com.kavit.service.AdminService;

@RestController
@CrossOrigin (origins = "http://localhost:4200")
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
	@PostMapping("/addDept/{id}")
	public String updateDept(@RequestBody Department newDept, @PathVariable Long id) {
		admin.updateDept(newDept, id);
		return "Department Updated";
	}
	
	@GetMapping("/addDept/{id}")
	public Optional<Department> viewDept(@PathVariable Long id) {
		return admin.viewDepartment(id);
	}
	
	@GetMapping("/addDept")
	public List<Department> viewDepts() {
		return admin.viewDepartments();
	}
	
	@DeleteMapping("/addDept/{id}")
	public void deleteDept(@PathVariable Long id) {
		admin.deleteDepartment(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addEmp")
	public String addNewEmp(@RequestBody Employee newEmp) {
		admin.addNewEmployee(newEmp);
		return "New employee added";
	}
	
	@GetMapping("/addEmp")
	public List<Employee> viewEmployees(){
		return admin.viewEmployees();
	}
	
	@DeleteMapping("/addEmp/{id}")
	public void deleteProduct(@PathVariable Long id) {
		 admin.deleteEmployee(id);
	}
	
	@GetMapping("/updateEmp/{id}")
	public Optional<Employee> viewEmployee(@PathVariable Long id){
		return admin.viewEmployee(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/updateEmp/{id}")
	public String updateEmployee(@RequestBody Employee newEmp, @PathVariable Long id) {
		admin.updateEmployee(newEmp,id);
		return "New Employee Updated";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addTask")
	public String addEmpTask(@RequestBody EmployeeTask newTask) {
		admin.addNewTask(newTask);
		return "New Task added for employee";
	}
	
	@GetMapping("/addTask")
	public List<EmployeeTask> viewEmpTasks(){
		return admin.viewEmpTasks();
	}
	
	@DeleteMapping("/addTask/{id}")
	public void deleteTask(@PathVariable Long id) {
		 admin.deleteTask(id);
	}
	
	@GetMapping("/updateTask/{id}")
	public Optional<EmployeeTask> viewTasks(@PathVariable Long id){
		return admin.viewEmpTask(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/updateTask/{id}")
	public String updateTask(@RequestBody EmployeeTask empTask, @PathVariable Long id) {
		admin.updateEmpTask(empTask,id);
		return "New Task Updated";
	}
	
	@GetMapping("/approveLeave")
	public List<LeaveRequest> viewLeaveRequests(){
		return admin.viewLeaveRequests();
	}
	
	@GetMapping("/approveLeave/{id}")
	public Optional<LeaveRequest> viewLeaveRequest(@PathVariable Long id){
		return admin.viewLeaveRequest(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/approveLeave/{id}")
	public String approveLeaveRequest(@RequestBody LeaveRequest newLeaveRequest, @PathVariable Long id){
		admin.approveLeaveRequest(newLeaveRequest, id);
		return "Leave Request Changed";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addTrainingRoom")
	public String addTrainingRoom(@RequestBody TrainingRoom newRoom) {
		admin.addNewTrainingRoom(newRoom);
		return "New Room added";
	}
	
	@GetMapping("/addTrainingRoom")
	public List<TrainingRoom> viewTrainingRooms() {
		return admin.viewTrainingRooms();
	}
	
	@GetMapping("/addTrainingRoom/{id}")
	public Optional<TrainingRoom> viewTrainingRooms(@PathVariable Long id) {
		return admin.viewTrainingRoom(id);
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addTrainingRoom/{id}")
	public String addTrainingRoom(@RequestBody TrainingRoom newRoom, @PathVariable Long id) {
		admin.updateTrainingRoom(newRoom, id);
		return "Room updated";
	}
	
	@DeleteMapping("/addTrainingRoom/{id}")
	public String deleteTrainingRoom(@PathVariable Long id) {
		admin.deleteTrainingRoom(id);
		return "Room Deleted";
	}
}
