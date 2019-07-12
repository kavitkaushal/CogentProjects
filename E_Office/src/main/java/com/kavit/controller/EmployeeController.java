package com.kavit.controller;


import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.kavit.dao.EmployeeTaskRepo;
import com.kavit.dao.LeaveRequestRepo;
import com.kavit.dao.TrainingRoomRepo;
import com.kavit.model.EmployeeTask;
import com.kavit.model.LeaveRequest;
import com.kavit.model.TrainingRoom;
import com.kavit.model.status;


@RestController
public class EmployeeController {

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TrainingRoomRepo tRoom;
	@Autowired
	private LeaveRequestRepo leaveRequest;
	@Autowired
	private EmployeeTaskRepo task;
	
	@GetMapping("/emp")
	public String empLogin() {
		return "Welcome Employee. Go to \"localhost:8080/emp/bookTrainingRoom\" to view and book a training room, "
				+ "or \"localhost:8080/emp/requestLeave\" to request for leave "
				+ "or \"localhost:8080/emp/finishTask\" to change task status.";
	}
	
	@GetMapping("/emp/bookTrainingRoom")
	public List<TrainingRoom> empTrainingAvl(){
		return tRoom.findAll();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/bookTrainingRoom")
	public String empBookTraining(@RequestBody TrainingRoom newRoom){
		newRoom.setRoom_avl("na");
		tRoom.save(newRoom);
		String empEmail= "19kavit@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(empEmail);
			helper.setText("You have booked a training room. Details below:\n\n"
							+ "Room Number: "+newRoom.getRoom_id()+"\nBooked by Employee ID: "+newRoom.getBooked_emp_id()
							+"\nFor date: "+newRoom.getBooked_date()
							+"\n\nRegards\nAdmin");
			helper.setSubject("Training Room Successfully Booked");
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Exception while sending email";
		}
		sender.send(message);
		return "Training Room Booked";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/requestLeave")
	public String empRequestLeave(@RequestBody LeaveRequest newLeaveRequest){
		newLeaveRequest.setStatus("p");
		leaveRequest.save(newLeaveRequest);
		String adminEmail= "ofcadmncog@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(adminEmail);
			helper.setText("Please aprove the leave request. Details below:\n\n"
							+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
							+"\nStart date: "+newLeaveRequest.getStart_date()
							+"\nEnd date: "+newLeaveRequest.getEnd_date()
							+"\nReason: "+newLeaveRequest.getReason()
							+"\n\nRegards");
			helper.setSubject("Leave Request Application");
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Exception while sending email";
		}
		sender.send(message);
		return "Leave Request Sent";
	}
	
	@GetMapping("/emp/finishTask")
	public List<EmployeeTask> viewEmpTask(){
		return task.findAll();
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/emp/finishTask")
	public String finishEmpTask(@RequestBody EmployeeTask empTask) {
		task.save(empTask);
		String adminEmail= "ofcadmncog@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(adminEmail);
			if(empTask.getTask_status().equals(status.Progressing)) {
			
				helper.setText("The assigned task has started. See details below \n\n"
					+"Task ID: "+empTask.getTask_id()
					+"\nEmployee Id: "+empTask.getTask_id()
					+"\nTask Description: "+empTask.getTask_desc()
					+"\nStart Date: "+empTask.getStart_date()+"\nEnd Date: "+empTask.getEnd_date()
					+"\n\nRegards");
				helper.setSubject("Task Started");
			}else if(empTask.getTask_status().equals(status.Completed)) {
				helper.setText("The assigned task has completed. See details below \n\n"
						+"Task ID: "+empTask.getTask_id()
						+"\nEmployee Id: "+empTask.getTask_id()
						+"\nTask Description: "+empTask.getTask_desc()
						+"\nStart Date: "+empTask.getStart_date()+"\nEnd Date: "+empTask.getEnd_date()
						+"\n\nRegards");
				helper.setSubject("Task Completed");
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			return "Exception while sending email";
		}
		sender.send(message);
		return "Your assigned has been changed.";
	}
}
