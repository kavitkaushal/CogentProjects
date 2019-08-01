package com.kavit.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.kavit.dao.EmpPasswordRepo;
import com.kavit.dao.EmployeeRepo;
import com.kavit.dao.EmployeeTaskRepo;
import com.kavit.dao.LeaveRequestRepo;
import com.kavit.dao.TrainingRoomRepo;
import com.kavit.model.*;

@Configuration
public class EmployeeService {

	@Autowired
	private EmployeeRepo employee;
	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TrainingRoomRepo tRoom;
	@Autowired
	private LeaveRequestRepo leaveRequest;
	@Autowired
	private EmployeeTaskRepo task;
	@Autowired
	private EmpPasswordRepo password;
	
	public String welcomeMessage() {
		return "Welcome Employee. Go to \"localhost:8080/emp/bookTrainingRoom\" to view and book a training room, "
				+ "or \"localhost:8080/emp/requestLeave\" to request for leave "
				+ "or \"localhost:8080/emp/finishTask\" to change task status.";
	}

	void sendEmail(String address, String subject, String text) {
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(address);
			helper.setText(text);
			helper.setSubject(subject);
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(message);
	}

	public String checkEmpLogin(TempPassword tempLogin) {
		String retVal = "Failed";
		EmpPassword newEmpLogin = new EmpPassword();
		if(tempLogin.getEmp_id().equals("admin")) {
			newEmpLogin.setEmp_id(Long.parseLong("0"));
			newEmpLogin.setPassword(tempLogin.getPassword());
			List<EmpPassword> allEmpLogin=password.findAll();
			for(EmpPassword empLogin: allEmpLogin) {
				if((empLogin.getEmp_id()==newEmpLogin.getEmp_id())&&
						(empLogin.getPassword().equals(newEmpLogin.getPassword()))) {
					retVal = "AdminSuccess";
				}
			}
		}
		else {
			try {
				newEmpLogin.setEmp_id(Long.parseLong(tempLogin.getEmp_id()));
				newEmpLogin.setPassword(tempLogin.getPassword());
				List<EmpPassword> allEmpLogin=password.findAll();
				for(EmpPassword empLogin: allEmpLogin) {
					if((empLogin.getEmp_id()==newEmpLogin.getEmp_id())&&
							(empLogin.getPassword().equals(newEmpLogin.getPassword()))) {
						retVal = "Success";
					}
				}
			}catch (NumberFormatException e) {
				
			}
		}
		return retVal;
	}
	
	public String updatePassword(TempPassword tempLogin, Long id) {
		String retVal= "Password cannot be updated";
		EmpPassword newEmpLogin = new EmpPassword();
		if(id==0){
			newEmpLogin.setEmp_id(id);
			newEmpLogin.setPassword(tempLogin.getPassword());
			List<EmpPassword> allEmpLogin=password.findAll();
			for(EmpPassword empLogin: allEmpLogin) {
				if((empLogin.getEmp_id()==newEmpLogin.getEmp_id())) {
					newEmpLogin.setPwd_id(empLogin.getPwd_id());
					password.save(newEmpLogin);
					retVal="Password updated successfully";
					sendEmail("ofcadmncog@gmail.com",
							"Password Updated", 
							"You have successfully updated your password\n\nThank you");
					break;
				}
			}
		}
		else {
			newEmpLogin.setEmp_id(id);
			newEmpLogin.setPassword(tempLogin.getPassword());
			List<EmpPassword> allEmpLogin=password.findAll();
			for(EmpPassword empLogin: allEmpLogin) {
				if((empLogin.getEmp_id()==newEmpLogin.getEmp_id())) {
					newEmpLogin.setPwd_id(empLogin.getPwd_id());
					password.save(newEmpLogin);
					retVal="Password updated successfully";
					Optional<Employee> empO = employee.findById(id);
					Employee emp = empO.get();
					sendEmail(emp.getEmail_id(),
							"Password Updated", 
							"You have successfully updated your password. \n\nThank you");
					break;
				}
			}
		}
		return retVal;
	}
	
	void callRefreshTRhelper(List<TrainingRoom> roomList) {
		
		Calendar calendar= Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        Date today = calendar.getTime();
		TrainingRoom latestBooked = new TrainingRoom();
		
		latestBooked.setBooked_date(today);	
		for(TrainingRoom tr : roomList) {
			if(tr.getBooked_date().after(latestBooked.getBooked_date())) {
				latestBooked = tr;
			}
		}

		calendar.setTime(latestBooked.getBooked_date());
		calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR, 0);
        calendar.add(Calendar.DATE, 1);
        
        for(TrainingRoom tr : roomList) {
        	if(tr.getBooked_date().before(today)) {
        		TrainingRoom newTR = new TrainingRoom();
        		newTR.setBooked_date(calendar.getTime());
        		newTR.setRoom_avl("Available");
        		newTR.setBooking_day(tr.getBooking_day());
        		newTR.setRoom_no(tr.getRoom_no());
        		newTR.setRoom_id(tr.getRoom_id());
        		tRoom.save(newTR);
        		calendar.add(Calendar.DATE, 1);
        	}
        }
	}
	
	void refreshTrainingRooms() {
		
		List<TrainingRoom> allTR = tRoom.findAll();
		List<TrainingRoom> roomList = new ArrayList<TrainingRoom>();
		List<String> roomNoList = new ArrayList<String>();
		int totalNoOfRooms = allTR.size()/7;
		
		for(int i = 1; i<=totalNoOfRooms; i++) {
			for(TrainingRoom tr : allTR) {	
				if(roomNoList.contains(tr.getRoom_no()) == false) {
					roomList.add(tr);
					if(roomList.size() == 7) {
						roomNoList.add(tr.getRoom_no());
						callRefreshTRhelper(roomList);
						roomList.clear();
					}
				}
			}
		}
	}
	
	public List<TrainingRoom> sendAvlTrainingRoom(){
		
		refreshTrainingRooms();
		List<TrainingRoom> allTR = tRoom.findAll();
		List<TrainingRoom> avlTR = new ArrayList<TrainingRoom>();
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_avl().equals("Available")) {
				avlTR.add(tr);
			}
		}
		return avlTR;
	}
	
	public List<TrainingRoom> sendBookedTrainingRoom(Long id){
		List<TrainingRoom> allTR = tRoom.findAll();
		List<TrainingRoom> bookedTR = new ArrayList<TrainingRoom>();
		for(TrainingRoom bTR : allTR) {
			if(bTR.getEmp_id()==id) {
				bookedTR.add(bTR);
			}
		}
		return bookedTR;
	}
	
	public Optional<TrainingRoom> sendRoomDetails(Long id){
		return tRoom.findById(id);
	}
	
	public String bookTrainingRoom(TrainingRoom newRoom, Long id) {
		String retVal = newRoom.getRoom_avl();
		newRoom.setRoom_id(id);
		tRoom.save(newRoom);
		if(retVal.equals("Not Available")) {
			retVal="Room Booked";
			sendEmail("ofcadmncog@gmail.com", 
					"Training Room Booked", 
					"A training room has been booked. Details below:\n\n"
					+ "Room Number: "+newRoom.getRoom_no()+"\nBooked by Employee ID: "+newRoom.getEmp_id()
					+"\nFor date: "+newRoom.getBooked_date()
					+"\n\nThank you");
			
		}else if(retVal.equals("Available")) {
			retVal="Room Cancelled";
			sendEmail("ofcadmncog@gmail.com", 
					"Training Room Cancelled", 
					"A training room booking has been cancelled. Details below:\n\n"
					+ "Room Number: "+newRoom.getRoom_no()+"\nCancelled by Employee ID: "+newRoom.getEmp_id()
					+"\nFor date: "+newRoom.getBooked_date()
					+"\n\nThank you");
			
		}
		
		return retVal;
	}
	
	public String applyLeaveRequest(LeaveRequest newLeaveRequest, Long id) {
		
		Date currentDate = new Date();
		String retVal= null;
		Long currentMS = currentDate.getTime() - TimeUnit.DAYS.toMillis(1);
		Long startMS = newLeaveRequest.getStart_date().getTime();
		Long endMS = newLeaveRequest.getEnd_date().getTime();
		
		if((newLeaveRequest.getEnd_date().compareTo(newLeaveRequest.getStart_date()) >= 0)
			&& ((startMS-currentMS) >= 0)
			&& ((endMS-currentMS) >= 0)) {
	
			retVal="Leave Requested";
			newLeaveRequest.setEmp_id(id);
			newLeaveRequest.setStatus("Pending");
			leaveRequest.save(newLeaveRequest);
			String adminEmail= "ofcadmncog@gmail.com";
			MimeMessage message=sender.createMimeMessage();
			MimeMessageHelper helper=new MimeMessageHelper(message);
			try {
				helper.setTo(adminEmail);
				helper.setText("A leave request has been posted. Details below:\n\n"
								+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
								+"\nStart date: "+newLeaveRequest.getStart_date()
								+"\nEnd date: "+newLeaveRequest.getEnd_date()
								+"\nReason: "+newLeaveRequest.getReason()
								+"\nStatus: Pending\n\nThank you");
				helper.setSubject("Leave Request Application");
			} catch (MessagingException e) {
				e.printStackTrace();
			}
			sender.send(message);
		}else {
			retVal="End Date & Start Date cannot be before today's, "
					+ "& End Date cannot be before Start Date";
		}
		return retVal;
	}
	
	public List<LeaveRequest> getLeaveRequest(Long id){
		List<LeaveRequest> allLR = leaveRequest.findAll();
		List<LeaveRequest> empLR = new ArrayList<LeaveRequest>();
		for(LeaveRequest lr: allLR) {
			if(lr.getEmp_id()==id) {
				empLR.add(lr);
			}
		}
		return empLR;
	}
	
	public String deleteLeaveRequest(Long id) {
		leaveRequest.deleteById(id);
		sendEmail("ofcadmncog@gmail.com",
				"Leave Request deleted",
				"A leave requested has been deleted with Leave Id: "+id+"\n\nThank you");
		return "Leave Request Deleted";
	}
	
	public List<EmployeeTask> sendEmployeeTasks(Long id){
		List<EmployeeTask> allET= task.findAll();
		List<EmployeeTask> thisEmpTask = new ArrayList<EmployeeTask>();
		for(EmployeeTask et: allET) {
			if(et.getEmp_id()==id) {
				thisEmpTask.add(et);
			}
		}
		return thisEmpTask;
	}
	
	public Optional<EmployeeTask> sendEmployeeTask(Long id) {
		return task.findById(id);
	}
	
	public String completeEmployeeTask(EmployeeTask empTask, Long id) {
		String retVal=null;
		empTask.setTask_id(id);
		task.save(empTask);
		String adminEmail= "ofcadmncog@gmail.com";
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(adminEmail);
			if(empTask.getTask_status().equals("In Progress")) {
				retVal="Task Status Updated: In Progress";
				helper.setText("The assigned task has started. See details below \n\n"
					+"Task ID: "+empTask.getTask_id()
					+"\nEmployee Id: "+empTask.getEmp_id()
					+"\nTask Description: "+empTask.getTask_desc()
					+"\nStart Date: "+empTask.getStart_date()+"\nEnd Date: "+empTask.getEnd_date()
					+"\n\nRegards");
				helper.setSubject("Task Started");
			}else if(empTask.getTask_status().equals("Completed")) {
				retVal="Task Status Updated: Completed";
				helper.setText("The assigned task has completed. See details below \n\n"
						+"Task ID: "+empTask.getTask_id()
						+"\nEmployee Id: "+empTask.getEmp_id()
						+"\nTask Description: "+empTask.getTask_desc()
						+"\nStart Date: "+empTask.getStart_date()+"\nEnd Date: "+empTask.getEnd_date()
						+"\n\nRegards");
				helper.setSubject("Task Completed");
			}
			else if(empTask.getTask_status().equals("Incomplete")) {
				retVal="Task Status Updated: Incomplete";
				helper.setText("The assigned task is incomplete. See details below \n\n"
						+"Task ID: "+empTask.getTask_id()
						+"\nEmployee Id: "+empTask.getEmp_id()
						+"\nTask Description: "+empTask.getTask_desc()
						+"\nStart Date: "+empTask.getStart_date()+"\nEnd Date: "+empTask.getEnd_date()
						+"\n\nRegards");
				helper.setSubject("Task Incomplete");
			}
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		sender.send(message);
		return retVal;
	}
}
