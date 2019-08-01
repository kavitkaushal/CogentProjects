package com.kavit.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

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
import com.kavit.dao.EmpPasswordRepo;
import com.kavit.dao.EmployeeRepo;
import com.kavit.dao.EmployeeTaskRepo;
import com.kavit.dao.LeaveRequestRepo;
import com.kavit.dao.TrainingRoomRepo;
import com.kavit.model.Department;
import com.kavit.model.EmpPassword;
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
	@Autowired
	private EmpPasswordRepo empPassword;
	
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	public String adminWelcomeMessage() {
		return "Welcome Admin. Go to \\\"localhost:8080/addDept\\\" to add an department, "
				+ "or \"localhost:8080/addEmp\" to add an employee, "
				+ "or \"localhost:8080/addTask\" to add a task, "
				+ "or \"localhost:8080/addTrainingRoom\" to add a training room, "
				+ "or \"localhost:8080/approveLeave\" to view and approve and approve leave request.";
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
		sendEmail("ofcadmncog@gmail.com",
					"New Department Added",
					"Hello Admin, \n\n"+newDept.getDept_name()+" has been added. \n\n Thank you");
	}
	
	public void updateDept(Department newDept, Long id) {
		newDept.setDept_id(id);
		department.save(newDept);
		sendEmail("ofcadmncog@gmail.com",
				"Department Updated",
				"Hello Admin, \n\nDept Id: "+id+
				" has been updated to "+newDept.getDept_name()+" \n\n Thank you");
	}
	
	public List<Department> viewDepartments(){
		return department.findAll();
	}
	
	public Optional<Department> viewDepartment(Long id) {
		return department.findById(id);
	}
	
	public String deleteDepartment(Long id) {
		String retVal = null;
		if(!checkIfDeptAssignedToAnyEmployee(id)) {
			department.deleteById(id);
			retVal="Department Deleted";
			sendEmail("ofcadmncog@gmail.com",
					"New Department Added",
					"Hello Admin, \n\nDept Id: "+id+" has been deleted. \n\n Thank you");
		}else {
			retVal="Department is assigned. Cannot be deleted";
		}
		return retVal;
	}

	public String setNewEmpPassword(Long emp_id) {
		EmpPassword newPwd = new EmpPassword();
		newPwd.setEmp_id(emp_id);
		newPwd.setPassword("123456");
		empPassword.save(newPwd);
		return "123456";
	}
	
	public List<Employee> viewEmployees(){
		return employee.findAll();
	}
	
	public void addNewEmployee(Employee newEmp) {	
		newEmp = employee.save(newEmp);
		setNewEmpPassword(newEmp.getEmp_id());
		sendEmail(newEmp.getEmail_id(),
				"Profile Created",
				"Welcome "+newEmp.getFirst_name()+" "+newEmp.getLast_name()+" to the e-Office management.\n\n"+
				"Your details are below. \n Employee ID: "+newEmp.getEmp_id()+
				"\nPassword: 123456\n\n"+
				"Please change your password after logging in"+
				"\n\nThank you");
	}
	
	public String deleteEmployee(Long id) {
		String retVal=null;
		if(!checkIfEmployeeHasAnythingAssigned(id)) {
			employee.deleteById(id);
			retVal="Employee Deleted";
			sendEmail("ofcadmncog@gmail.com",
					"Profile Deleted",
					"Employee Id: "+id+" has been deleted."+
					"\n\nThank you");
		}else {
			retVal="Employee is assigned. Cannot be deleted";
		}
		return retVal;
	}
	
	public Optional<Employee> viewEmployee(Long id) {
		return employee.findById(id);
	}
	
	public void updateEmployee(Employee newEmp,Long id) {
		newEmp.setEmp_id(id);
		newEmp=employee.save(newEmp);
		sendEmail(newEmp.getEmail_id(),
				"Profile Updated",
				"Your profile has been updated \n\nEmployee Id: "+newEmp.getEmp_id()+
				"\n\nPlease login to see the update profile."+
				"\n\nThank you");
	}
	
	public List<EmployeeTask> viewEmpTasks(){
		return task.findAll();
	}
	
	public String addNewTask(EmployeeTask newTask) {
		
		Date currentDate = new Date();
		String retVal= null;
		Long currentMS = currentDate.getTime() - TimeUnit.DAYS.toMillis(1);
		Long startMS = newTask.getStart_date().getTime();
		Long endMS = newTask.getEnd_date().getTime();
		
		if((newTask.getEnd_date().compareTo(newTask.getStart_date()) >= 0)
			&& ((startMS-currentMS) >= 0)
			&& ((endMS-currentMS) >= 0)) {
			
			task.save(newTask);
			Optional<Employee> empO = employee.findById(newTask.getEmp_id());
			Employee emp = empO.get();
			sendEmail(emp.getEmail_id(),
					"New Task Assigned",
					"Please see the task details below.\n\n"+
					"Task Name: "+newTask.getTask_name()+
					"\nTask Description: "+newTask.getTask_desc()+
					"\nStart Date: "+newTask.getStart_date()+
					"\nEnd Date: "+newTask.getEnd_date()+"\n\nThank you");
			retVal = "Task Added";
		}else {
			retVal="End Date & Start Date cannot be before today's, "
					+ "& End Date cannot be before Start Date";
		}
		return retVal;
	}
	
	public void deleteTask(Long id) {
		task.deleteById(id);
		sendEmail("ofcadmncog@gmail.com", 
				"Task Deleted",
				"Task has been deleted with Task Id: "+id+"\n\nThank you");
	}
	
	public Optional<EmployeeTask> viewEmpTask(Long id) {
		return task.findById(id);
	}
	
	public String updateEmpTask(EmployeeTask newTask,Long id) {

		Date currentDate = new Date();
		String retVal= null;
		Long currentMS = currentDate.getTime() - TimeUnit.DAYS.toMillis(1);
		Long startMS = newTask.getStart_date().getTime();
		Long endMS = newTask.getEnd_date().getTime();
		
		if((newTask.getEnd_date().compareTo(newTask.getStart_date()) >= 0)
			&& ((startMS-currentMS) >= 0)
			&& ((endMS-currentMS) >= 0)) {
			
			newTask.setTask_id(id);
			task.save(newTask);
			retVal="Task Updated";
			Optional<Employee> empO = employee.findById(newTask.getEmp_id());
			Employee emp = empO.get();
			sendEmail(emp.getEmail_id(),
					"Task Updated",
					"Please see the updated task details below.\n\n"+
					"Task Id: "+id+
					"\nTask Name: "+newTask.getTask_name()+
					"\nTask Description: "+newTask.getTask_desc()+
					"\nStart Date: "+newTask.getStart_date()+
					"\nEnd Date: "+newTask.getEnd_date()+"\n\nThank you");
			
		}else {
			retVal="End Date & Start Date cannot be before today's, "
					+ "& End Date cannot be before Start Date";
		}
		
		return retVal;
	}
	

	public String addNewTrainingRoom(TrainingRoom newRoom) {
		
		String retVal = "Room Added";
		List<TrainingRoom> allTR = tRoom.findAll();
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_no().equals(newRoom.getRoom_no())) {
				retVal = "Room No. already exists";
				break;
			}
		}
		if(retVal.equals("Room Added")) {

			Calendar calendar = Calendar.getInstance();
			calendar.setTime(new Date());
			calendar.set(Calendar.MILLISECOND, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.HOUR, 0);
			Date day1 = calendar.getTime(); calendar.add(Calendar.DATE, 1);
			Date day2 = calendar.getTime(); calendar.add(Calendar.DATE, 1);
			Date day3 = calendar.getTime(); calendar.add(Calendar.DATE, 1);
			Date day4 = calendar.getTime(); calendar.add(Calendar.DATE, 1);
			Date day5 = calendar.getTime(); calendar.add(Calendar.DATE, 1);
			Date day6 = calendar.getTime(); calendar.add(Calendar.DATE, 1);
			Date day7 = calendar.getTime(); 
			TrainingRoom tr1 = new TrainingRoom(); tr1.setRoom_no(newRoom.getRoom_no()); tr1.setBooked_date(day1);
			tr1.setRoom_avl(newRoom.getRoom_avl()); tr1.setBooking_day((long) 1); tRoom.save(tr1);
			TrainingRoom tr2 = new TrainingRoom(); tr2.setRoom_no(newRoom.getRoom_no()); tr2.setBooked_date(day2);
			tr2.setRoom_avl(newRoom.getRoom_avl());	tr2.setBooking_day((long) 2); tRoom.save(tr2);
			TrainingRoom tr3 = new TrainingRoom(); tr3.setRoom_no(newRoom.getRoom_no()); tr3.setBooked_date(day3);
			tr3.setRoom_avl(newRoom.getRoom_avl()); tr3.setBooking_day((long) 3); tRoom.save(tr3);
			TrainingRoom tr4 = new TrainingRoom(); tr4.setRoom_no(newRoom.getRoom_no()); tr4.setBooked_date(day4);
			tr4.setRoom_avl(newRoom.getRoom_avl()); tr4.setBooking_day((long) 4); tRoom.save(tr4);
			TrainingRoom tr5 = new TrainingRoom(); tr5.setRoom_no(newRoom.getRoom_no()); tr5.setBooked_date(day5);
			tr5.setRoom_avl(newRoom.getRoom_avl());	tr5.setBooking_day((long) 5); tRoom.save(tr5);
			TrainingRoom tr6 = new TrainingRoom(); tr6.setRoom_no(newRoom.getRoom_no()); tr6.setBooked_date(day6);
			tr6.setRoom_avl(newRoom.getRoom_avl());	tr6.setBooking_day((long) 6); tRoom.save(tr6);
			TrainingRoom tr7 = new TrainingRoom(); tr7.setRoom_no(newRoom.getRoom_no()); tr7.setBooked_date(day7);
			tr7.setRoom_avl(newRoom.getRoom_avl());	tr7.setBooking_day((long) 7); tRoom.save(tr7);
			
			sendEmail("ofcadmncog@gmail.com",
					"Training Room Added", 
					"A training Room has been added with Room No: "+newRoom.getRoom_no()+
					"\n\nStatus: "+newRoom.getRoom_avl()+
					"\n\nThank you");
		}
		return retVal;
	}
	
	public String deleteTrainingRoom(Long id) {
		String retVal = "Room Deleted";
		String roomToDel = null;
		List<TrainingRoom> allTR=tRoom.findAll();
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_id().equals(id)) {
				roomToDel = tr.getRoom_no();
				break;
			}
		}
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_no().equals(roomToDel) && tr.getRoom_avl().equals("Not Available")) {
				retVal = "Room is booked in next 7 days. Cannot be deleted";
				break;
			}
		}
		
		if(retVal.equals("Room Deleted")) {
			
			for(TrainingRoom tr : allTR) {
				if(tr.getRoom_no().equals(roomToDel)) {
					tRoom.deleteById(tr.getRoom_id());
				}
			}
			sendEmail("ofcadmncog@gmail.com",
					"Training Room Deleted", 
					"A training Room has been deleted with Room No: "+roomToDel+
					"\n\nThank you");
		}
		return retVal;
	}
	
	public List<TrainingRoom> viewBookedRooms(){
		List<TrainingRoom> allTR = tRoom.findAll();
		List<TrainingRoom> tosendTR = new ArrayList<TrainingRoom>();
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_avl().equals("Not Available")) {
				tosendTR.add(tr);
			}
		}
		return tosendTR;
	}
	
	public String updateTrainingRoom(TrainingRoom newRoom, Long id) {
		String retVal = "Room Updated";
		List<TrainingRoom> allTR=tRoom.findAll();
		List<TrainingRoom> existingTR=new ArrayList<TrainingRoom>();
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_no().equals(newRoom.getRoom_no())) {
				retVal = "Room No. already exists";
				break;
			}
		}
		if(retVal.equals("Room Updated")) {
			
			String oldRoomNo = null;
			for(TrainingRoom tr : allTR) {
				if(tr.getRoom_id().equals(id)) {
					oldRoomNo = tr.getRoom_no();
					break;
				}
			}
			
			for(TrainingRoom tr : allTR) {
				if(tr.getRoom_no().equals(oldRoomNo)) {
					existingTR.add(tr);
				}
			}
			
			for(TrainingRoom tr : existingTR) {
				TrainingRoom newTR = new TrainingRoom(tr);
				newTR.setRoom_no(newRoom.getRoom_no());
				tRoom.save(newTR);
			}
			
			sendEmail("ofcadmncog@gmail.com",
					"Training Room Updated", 
					"A training Room has been updated with Room No: "+newRoom.getRoom_no()+
					"\n\nStatus: "+newRoom.getRoom_avl()+
					"\n\nThank you");
			
		}
		return retVal;

	}
	
	public List<TrainingRoom> viewTrainingRooms(){
		List<TrainingRoom> allTR = tRoom.findAll();
		List<TrainingRoom> tosendTR = new ArrayList<TrainingRoom>();
		String compareRoomNo = null;
		for(TrainingRoom tr : allTR) {
			if(tr.getRoom_no().equals(compareRoomNo)==false) {
				compareRoomNo = tr.getRoom_no();
				tosendTR.add(tr);
			}
		}
		return tosendTR;
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
	
	public String approveLeaveRequest(LeaveRequest newLeaveRequest, Long id) {
		String retVal=null;
		newLeaveRequest.setLeave_id(id);
		leaveRequest.save(newLeaveRequest);
		Optional<Employee> empO = employee.findById(newLeaveRequest.getEmp_id());
		Employee emp = empO.get();
		String empEmail = emp.getEmail_id();
		MimeMessage message=sender.createMimeMessage();
		MimeMessageHelper helper=new MimeMessageHelper(message);
		try {
			helper.setTo(empEmail);
				if(newLeaveRequest.getStatus().equals("Approved")) {
					retVal="Leave Request Approved";
					helper.setText("Your leave request has been approved. Details below:\n\n"
									+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
									+"\nStart date: "+newLeaveRequest.getStart_date()
									+"\nEnd date: "+newLeaveRequest.getEnd_date()
									+"\nReason: "+newLeaveRequest.getReason()
									+"\n\nRegards\nAdmin");
					helper.setSubject("Leave Request Approved");
				}else if(newLeaveRequest.getStatus().equals("Pending")) {
					retVal="Leave Request Pending";
					helper.setText("Your leave request is pending. Details below:\n\n"
							+ "Leave Id: "+newLeaveRequest.getLeave_id()+"\nEmployee ID: "+newLeaveRequest.getEmp_id()
							+"\nStart date: "+newLeaveRequest.getStart_date()
							+"\nEnd date: "+newLeaveRequest.getEnd_date()
							+"\nReason: "+newLeaveRequest.getReason()
							+"\n\nRegards\nAdmin");
					helper.setSubject("Leave Request Pending");
				}else if(newLeaveRequest.getStatus().equals("Denied")) {
					retVal= "Leave Request Denied";
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
		return retVal;
	}
	
}
