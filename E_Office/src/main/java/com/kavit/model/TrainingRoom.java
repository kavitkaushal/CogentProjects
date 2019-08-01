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
	private String room_no;
	@Column(nullable = false)
	private String room_avl;
	@Column (nullable = true)
	private Long booking_day;
	@Column(nullable = true)
	private Long emp_id;
	@Column (nullable = true)
	private Date booked_date;
	public TrainingRoom() {
		super();
	}
	
	public TrainingRoom(Long room_id, String room_no, String room_avl, Long booking_day, Long emp_id, Date booked_date) {
		super();
		this.room_id = room_id;
		this.room_no = room_no;
		this.room_avl = room_avl;
		this.booking_day = booking_day;
		this.emp_id = emp_id;
		this.booked_date = booked_date;
	}

	public TrainingRoom(TrainingRoom tr) {
		this.room_id = tr.room_id;
		this.room_no = tr.room_no;
		this.room_avl = tr.room_avl;
		this.booking_day = tr.booking_day;
		this.emp_id = tr.emp_id;
		this.booked_date = tr.booked_date;
	}

	public Long getRoom_id() {
		return room_id;
	}
	public void setRoom_id(Long room_id) {
		this.room_id = room_id;
	}
	public String getRoom_no() {
		return room_no;
	}
	public void setRoom_no(String room_no) {
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

	public Long getBooking_day() {
		return booking_day;
	}

	public void setBooking_day(Long booking_day) {
		this.booking_day = booking_day;
	}

	public Date getBooked_date() {
		return booked_date;
	}

	public void setBooked_date(Date booked_date) {
		this.booked_date = booked_date;
	}
	
}
