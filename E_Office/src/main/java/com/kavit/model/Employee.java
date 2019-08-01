package com.kavit.model;

import javax.persistence.*;
import javax.validation.constraints.Email;

@Entity
@Table(name="emp")
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long emp_id;
	@Column(nullable = false)
	private String first_name;
	@Column(nullable = false)
	private String last_name;
	@Email
	@Column(nullable = false)
	private String email_id;
	@Column(nullable = false)
	private Long contact_no;
	@Column(nullable = false)
	private Long dept_id;

	public Employee() {
		super();
	}
	
	public Employee(Long emp_id, String first_name, String last_name, @Email String email_id, Long contact_no,
			Long dept_id) {
		super();
		this.emp_id = emp_id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email_id = email_id;
		this.contact_no = contact_no;
		this.dept_id = dept_id;
	}

	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	public String getFirst_name() {
		return first_name;
	}
	public void setFirst_name(String first_name) {
		this.first_name = first_name;
	}
	public String getLast_name() {
		return last_name;
	}
	public void setLast_name(String last_name) {
		this.last_name = last_name;
	}
	public String getEmail_id() {
		return email_id;
	}
	public void setEmail_id(String email_id) {
		this.email_id = email_id;
	}
	public Long getContact_no() {
		return contact_no;
	}
	public void setContact_no(Long contact_no) {
		this.contact_no = contact_no;
	}

	public Long getDept_id() {
		return dept_id;
	}

	public void setDept_id(Long dept_id) {
		this.dept_id = dept_id;
	}

}
