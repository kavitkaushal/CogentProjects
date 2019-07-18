package com.kavit.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

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
	
	public String adminWelcomeMessage() {
		return "Welcome Admin. Go to \\\"localhost:8080/addDept\\\" to add an department, "
				+ "or \"localhost:8080/addEmp\" to add an employee, "
				+ "or \"localhost:8080/addTask\" to add a task, "
				+ "or \"localhost:8080/addTrainingRoom\" to add a training room, "
				+ "or \"localhost:8080/approveLeave\" to view and approve and approve leave request.";
	}
	
	public void addnewDepartment(Department newDept) {
		department.save(newDept);
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
	
	public void addNewTrainingRoom(TrainingRoom newRoom) {
		tRoom.save(newRoom);
	}
	
	public List<LeaveRequest> viewLeaveRequest(){
		return leaveRequest.findAll();
	}
	
	public void approveLeaveRequest(LeaveRequest newLeaveRequest) {
		newLeaveRequest.setStatus("a");
		leaveRequest.save(newLeaveRequest);
		String empEmail= "19kavit@gmail.com";	//"ofcadmncog@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(empEmail);
			helper.setText("Your leave request has been approved. Details below:\n\n"
							+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
							+"\nStart date: "+newLeaveRequest.getStart_date()
							+"\nEnd date: "+newLeaveRequest.getEnd_date()
							+"\nReason: "+newLeaveRequest.getReason()
							+"\n\nRegards\nAdmin");
			helper.setSubject("Leave Request Approved");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(message);
	}
}
