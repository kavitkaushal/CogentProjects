package com.kavit.model;

public class TempPassword {

	private String emp_id;
	private String password;
	public TempPassword() {
		super();
	}
	public TempPassword(String emp_id, String password) {
		super();
		this.emp_id = emp_id;
		this.password = password;
	}
	public String getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(String emp_id) {
		this.emp_id = emp_id;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
