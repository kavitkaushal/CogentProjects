package com.kavit.model;

import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name="training_room")
public class TrainingRoom {
	
	@Id
	@GeneratedValue
	private Long room_id;
	@Column(nullable = false)
	private String room_avl;
	@Column(nullable = true)
	private int booked_emp_id;
	@Column(nullable = true)
	private Date booked_date;
	public Long getRoom_id() {
		return room_id;
	}
	public void setRoom_id(Long room_id) {
		this.room_id = room_id;
	}
	
	public String getRoom_avl() {
		return room_avl;
	}
	public void setRoom_avl(String room_avl) {
		this.room_avl = room_avl;
	}
	public int getBooked_emp_id() {
		return booked_emp_id;
	}
	public void setBooked_emp_id(int booked_emp_id) {
		this.booked_emp_id = booked_emp_id;
	}
	public Date getBooked_date() {
		return booked_date;
	}
	public void setBooked_date(Date booked_date) {
		this.booked_date = booked_date;
	}
	public TrainingRoom() {
		super();
	}
	public TrainingRoom(Long room_id, String room_avl, int booked_emp_id, Date booked_date) {
		super();
		this.room_id = room_id;
		this.room_avl = room_avl;
		this.booked_emp_id = booked_emp_id;
		this.booked_date = booked_date;
	}
}
