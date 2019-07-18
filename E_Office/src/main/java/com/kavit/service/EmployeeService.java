package com.kavit.service;

import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.kavit.dao.EmployeeTaskRepo;
import com.kavit.dao.LeaveRequestRepo;
import com.kavit.dao.TrainingRoomRepo;
import com.kavit.model.*;

@Configuration
public class EmployeeService {

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TrainingRoomRepo tRoom;
	@Autowired
	private LeaveRequestRepo leaveRequest;
	@Autowired
	private EmployeeTaskRepo task;
	
	public String welcomeMessage() {
		return "Welcome Employee. Go to \"localhost:8080/emp/bookTrainingRoom\" to view and book a training room, "
				+ "or \"localhost:8080/emp/requestLeave\" to request for leave "
				+ "or \"localhost:8080/emp/finishTask\" to change task status.";
	}
	
	public List<TrainingRoom> sendAvlTrainingRoom(){
		return tRoom.findAll();
	}
	
	public void bookTrainingRoom(TrainingRoom newRoom) {
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
		}
		sender.send(message);
	}
	
	public void applyLeaveRequest(LeaveRequest newLeaveRequest) {
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
		}
		sender.send(message);
	}
	
	public List<EmployeeTask> sendEmployeeTask(){
		return task.findAll();
	}
	
	public void completeEmployeeTask(EmployeeTask empTask) {
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
		}
		sender.send(message);
	}
}
