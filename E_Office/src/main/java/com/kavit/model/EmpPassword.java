package com.kavit.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="emp_pwd")
public class EmpPassword {
	
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long pwd_id;
	@Column(nullable = false)
	private Long emp_id;
	@Column(nullable = false)
	private String password;
	public EmpPassword() {
		super();
	}
	
	public EmpPassword(Long pwd_id, Long emp_id, String password) {
		super();
		this.pwd_id = pwd_id;
		this.emp_id = emp_id;
		this.password = password;
	}

	public Long getPwd_id() {
		return pwd_id;
	}
	public void setPwd_id(Long pwd_id) {
		this.pwd_id = pwd_id;
	}
	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

}
