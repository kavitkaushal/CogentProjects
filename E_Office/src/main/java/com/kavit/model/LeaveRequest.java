package com.kavit.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="leave_request")
public class LeaveRequest {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long leave_id;
	@Column(nullable = false)
	private Long emp_id;
	@Column(nullable = false)
	private Date start_date;
	@Column(nullable = false)
	private Date end_date;
	@Column(nullable = false)
	private String reason;
	@Column(nullable = false)
	private String status;
	public LeaveRequest() {
		super();
	}
	public LeaveRequest(Long leave_id, Long emp_id, Date start_date, Date end_date, String reason, String status) {
		super();
		this.leave_id = leave_id;
		this.emp_id = emp_id;
		this.start_date = start_date;
		this.end_date = end_date;
		this.reason = reason;
		this.status = status;
	}
	public Long getLeave_id() {
		return leave_id;
	}
	public void setLeave_id(Long leave_id) {
		this.leave_id = leave_id;
	}
	public Date getStart_date() {
		return start_date;
	}
	public void setStart_date(Date start_date) {
		this.start_date = start_date;
	}
	public Date getEnd_date() {
		return end_date;
	}
	public void setEnd_date(Date end_date) {
		this.end_date = end_date;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	
}
