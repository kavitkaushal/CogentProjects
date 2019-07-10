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

import com.kavit.dao.TrainingRoomRepo;
import com.kavit.model.TrainingRoom;

@RestController
public class EmployeeController {

	@Autowired
	private JavaMailSender sender;
	@Autowired
	private TrainingRoomRepo tRoom;
	
	@GetMapping("/emp")
	public String empLogin() {
		return "Welcome Employee. Go to \"localhost:8080/emp/bookTrainingRoom\" to view and book a training room.";
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
}
