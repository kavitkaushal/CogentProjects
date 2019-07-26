package com.kavit.model;

import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name="training_room")
public class TrainingRoom {
	
	@Id
	@GeneratedValue
	private Long room_id;
	@Column(nullable= false)
	private Long room_no;
	@Column(nullable = false)
	private String room_avl;
	@Column(nullable = true)
	private Long emp_id;
	@Column (nullable = true)
	private Date booked_from;
	@Column(nullable = true)
	private Date booked_until;
	public TrainingRoom() {
		super();
	}
	public TrainingRoom(Long room_id, Long room_no, String room_avl, Long emp_id, Date booked_from, Date booked_until) {
		super();
		this.room_id = room_id;
		this.room_no = room_no;
		this.room_avl = room_avl;
		this.emp_id = emp_id;
		this.booked_from = booked_from;
		this.booked_until = booked_until;
	}
	public Long getRoom_id() {
		return room_id;
	}
	public void setRoom_id(Long room_id) {
		this.room_id = room_id;
	}
	public Long getRoom_no() {
		return room_no;
	}
	public void setRoom_no(Long room_no) {
		this.room_no = room_no;
	}
	public String getRoom_avl() {
		return room_avl;
	}
	public void setRoom_avl(String room_avl) {
		this.room_avl = room_avl;
	}
	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	public Date getBooked_from() {
		return booked_from;
	}
	public void setBooked_from(Date booked_from) {
		this.booked_from = booked_from;
	}
	public Date getBooked_until() {
		return booked_until;
	}
	public void setBooked_until(Date booked_until) {
		this.booked_until = booked_until;
	}
	
}
