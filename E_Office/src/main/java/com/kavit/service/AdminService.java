package com.kavit.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.kavit.dao.DepartmentRepo;
import com.kavit.dao.EmployeeRepo;
import com.kavit.dao.EmployeeTaskRepo;
import com.kavit.dao.LeaveRequestRepo;
import com.kavit.dao.TrainingRoomRepo;
import com.kavit.model.Department;
import com.kavit.model.Employee;
import com.kavit.model.EmployeeTask;
import com.kavit.model.LeaveRequest;
import com.kavit.model.TrainingRoom;

@Configuration
public class AdminService {
	
	@Autowired
	private EmployeeRepo employee;
	@Autowired
	private DepartmentRepo department;
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private EmployeeTaskRepo task;
	@Autowired
	private TrainingRoomRepo tRoom;
	@Autowired
	private LeaveRequestRepo leaveRequest;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public String adminWelcomeMessage() {
		return "Welcome Admin. Go to \\\"localhost:8080/addDept\\\" to add an department, "
				+ "or \"localhost:8080/addEmp\" to add an employee, "
				+ "or \"localhost:8080/addTask\" to add a task, "
				+ "or \"localhost:8080/addTrainingRoom\" to add a training room, "
				+ "or \"localhost:8080/approveLeave\" to view and approve and approve leave request.";
	}
	
	public List<Long> findAllEmployeeId(){
		List<Long> L = new ArrayList<Long>();
		List<Employee> temp= employee.findAll();
		for(Employee e : temp) {
			L.add(e.getEmp_id());
		}
		return L;
	}
	
	public List<Long> findAllDeptId(){
		List<Long> L = new ArrayList<Long>();
		List<Department> temp= department.findAll();
		for(Department d : temp) {
			L.add(d.getDept_id());
		}
		return L;
	}
	
	public List<Long> findAllTaskId(){
		List<Long> L = new ArrayList<Long>();
		List<EmployeeTask> temp = task.findAll();
		for(EmployeeTask t : temp) {
			L.add(t.getTask_id());
		}
		return L;
	}
	
	public List<Long> findAllLeaveId(){
		List<Long> L = new ArrayList<Long>();
		List<LeaveRequest> temp = leaveRequest.findAll();
		for( LeaveRequest l : temp) {
			L.add(l.getLeave_id());
		}
		return L;
	}
	
	public List<Long> findAllRoomId(){
		List<Long> L = new ArrayList<Long>();
		List<TrainingRoom> temp = tRoom.findAll();
		for(TrainingRoom r : temp) {
			L.add(r.getRoom_id());
		}
		return L;
	}
	
	public boolean checkIfDeptAssignedToAnyEmployee(Long id) {
		List<Employee> emp = employee.findAll();
		boolean retVal = false;
		for(Employee e:emp){
			if(e.getDept_id()==id){
				retVal=true;
				break;
			}
		}
		return retVal;
	}
	
	public boolean checkIfEmployeeHasAnythingAssigned(Long id) {
		List<EmployeeTask> task = viewEmpTasks();
		List<LeaveRequest> leave = viewLeaveRequests();
		List<TrainingRoom> room = viewTrainingRooms();
		boolean retVal = false;
		for(EmployeeTask t : task) {
			if(t.getEmp_id()==id) {
				retVal=true;
				break;
			}
		}
		for(LeaveRequest l : leave) {
			if(l.getEmp_id()==id) {
				retVal=true;
				break;
			}
		}
		for(TrainingRoom r : room) {
			if(r.getEmp_id()==id) {
				retVal=true;
				break;
			}
		}
		return retVal;
	}
	
	public void addnewDepartment(Department newDept) {
		department.save(newDept);
	}
	
	public void updateDept(Department newDept, Long id) {
		newDept.setDept_id(id);
		department.save(newDept);
	}
	
	public List<Department> viewDepartments(){
		return department.findAll();
	}
	
	public Optional<Department> viewDepartment(Long id) {
		return department.findById(id);
	}
	
	public void deleteDepartment(Long id) {
		if(!checkIfDeptAssignedToAnyEmployee(id)) {
			department.deleteById(id);
		}
	}
	
	public List<Employee> viewEmployees(){
		return employee.findAll();
	}
	
	public void addNewEmployee(Employee newEmp) {
		employee.save(newEmp);
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo("19kavit@gmail.com");
			helper.setText("Welcome "+newEmp.getFirst_name()+" "+newEmp.getLast_name()+" to the e-Office management."
							+"\n\n Your details are below. \n Employee ID: "+newEmp.getEmp_id()+"\n Employee Login: "+newEmp.getEmail_id()
							+"\n\nRegards, \n Admin Team \n e-Office Management");
			helper.setSubject("Welcome On-board "+newEmp.getFirst_name()+" "+newEmp.getLast_name());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(message);
	}
	
	public void deleteEmployee(Long id) {
		if(!checkIfEmployeeHasAnythingAssigned(id)) {
			employee.deleteById(id);
		}
	}
	
	public Optional<Employee> viewEmployee(Long id) {
		return employee.findById(id);
	}
	
	public void updateEmployee(Employee newEmp,Long id) {
		newEmp.setEmp_id(id);
		employee.save(newEmp);
	}
	
	public List<EmployeeTask> viewEmpTasks(){
		return task.findAll();
	}
	
	public void addNewTask(EmployeeTask newTask) {
		task.save(newTask);
		String empEmail= "19kavit@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(empEmail);
			helper.setText("A new task has been assigned. See details below \n\nTask ID: "+newTask.getTask_id()
							+"\nTask Description: "+newTask.getTask_desc()
							+"\nStart Date: "+newTask.getStart_date()+"\nEnd Date: "+newTask.getEnd_date()
							+"\n\n Please complete before end date. \n\n\nRegards,\nAdmin");
			helper.setSubject("Task Assignment Notification");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(message);
	}
	
	public void deleteTask(Long id) {
		task.deleteById(id);
	}
	
	public Optional<EmployeeTask> viewEmpTask(Long id) {
		return task.findById(id);
	}
	
	public void updateEmpTask(EmployeeTask newTask,Long id) {
		newTask.setTask_id(id);
		task.save(newTask);
	}
	
	public void addNewTrainingRoom(TrainingRoom newRoom) {
		tRoom.save(newRoom);
	}
	
	public void deleteTrainingRoom(Long id) {
		tRoom.deleteById(id);
	}
	
	public void updateTrainingRoom(TrainingRoom newRoom, Long id) {
		newRoom.setRoom_id(id);
		tRoom.save(newRoom);
	}
	
	public List<TrainingRoom> viewTrainingRooms(){
		return tRoom.findAll();
	}
	
	public Optional<TrainingRoom> viewTrainingRoom(Long id) {
		return tRoom.findById(id);
	}
	
	public List<LeaveRequest> viewLeaveRequests(){
		return leaveRequest.findAll();
	}
	
	public Optional<LeaveRequest> viewLeaveRequest(Long id) {
		return leaveRequest.findById(id);
	}
	
	public void approveLeaveRequest(LeaveRequest newLeaveRequest, Long id) {
		newLeaveRequest.setLeave_id(id);
		leaveRequest.save(newLeaveRequest);
		String empEmail= "19kavit@gmail.com";	//"ofcadmncog@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(empEmail);
				if(newLeaveRequest.getStatus().equals("Approved")) {
					helper.setText("Your leave request has been approved. Details below:\n\n"
									+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
									+"\nStart date: "+newLeaveRequest.getStart_date()
									+"\nEnd date: "+newLeaveRequest.getEnd_date()
									+"\nReason: "+newLeaveRequest.getReason()
									+"\n\nRegards\nAdmin");
					helper.setSubject("Leave Request Approved");
				}else if(newLeaveRequest.getStatus().equals("Pending")) {
					helper.setText("Your leave request is pending. Details below:\n\n"
							+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
							+"\nStart date: "+newLeaveRequest.getStart_date()
							+"\nEnd date: "+newLeaveRequest.getEnd_date()
							+"\nReason: "+newLeaveRequest.getReason()
							+"\n\nRegards\nAdmin");
					helper.setSubject("Leave Request Pending");
				}else if(newLeaveRequest.getStatus().equals("Denied")) {
					helper.setText("Your leave request has been denied. Details below:\n\n"
							+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
							+"\nStart date: "+newLeaveRequest.getStart_date()
							+"\nEnd date: "+newLeaveRequest.getEnd_date()
							+"\nReason: "+newLeaveRequest.getReason()
							+"\n\nRegards\nAdmin");
					helper.setSubject("Leave Request Denied");
				}
			} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(message);
	}
	
}
