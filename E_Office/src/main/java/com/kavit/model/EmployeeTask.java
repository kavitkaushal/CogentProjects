package com.kavit.model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.*;


@Entity
@Table(name="task")
public class EmployeeTask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long task_id;
	@Column(nullable = false)
	private Long emp_id;
	@Column(nullable = false)
	private String task_name;
	@Column(nullable = false)
	private String task_desc;
	@Column(nullable = false)
	private Date start_date;
	@Column(nullable = false)
	private Date end_date;
	@Column(nullable = false)
	private String task_status;

	public EmployeeTask() {
		super();
	}
	public EmployeeTask(Long task_id, Long emp_id, String task_name, String task_desc, Date start_date,
			Date end_date, String task_status) {
		super();
		this.task_id = task_id;
		this.emp_id = emp_id;
		this.task_name = task_name;
		this.task_desc = task_desc;
		this.start_date = start_date;
		this.end_date = end_date;
		this.task_status = "Incomplete";
	}
	public Long getTask_id() {
		return task_id;
	}
	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}
	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	public String getTask_name() {
		return task_name;
	}
	public void setTask_name(String task_name) {
		this.task_name = task_name;
	}
	public String getTask_desc() {
		return task_desc;
	}
	public void setTask_desc(String task_desc) {
		this.task_desc = task_desc;
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
	public String getTask_status() {
		return task_status;
	}
	public void setTask_status(String task_status) {
		this.task_status = task_status;
	}
}


