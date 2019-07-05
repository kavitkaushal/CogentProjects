package com.kavit.controller;

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

import com.kavit.dao.DepartmentRepo;
import com.kavit.dao.EmployeeRepo;
import com.kavit.model.Department;
import com.kavit.model.Employee;

@RestController
public class AdminController {

	@Autowired
	private EmployeeRepo employee;
	@Autowired
	private DepartmentRepo department;
	@Autowired
	private JavaMailSender sender;
	
	@GetMapping("/")
	String adminLogin() {
		return "Welcome Admin. Go to \\\"localhost:8080/addDept\\\" to add an department, "
				+ "or \"localhost:8080/addEmp\" to add an employee";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addDept")
	String add(@RequestBody Department newDept) {
		department.save(newDept);
		return "New department added";
	}
	
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping("/addEmp")
	String add(@RequestBody Employee newEmp) {
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
			return "Exception while sending email";
		}
		sender.send(message);
		return "New employee added";
	}
}
