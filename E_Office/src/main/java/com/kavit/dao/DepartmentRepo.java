package com.kavit.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kavit.model.Department;

public interface DepartmentRepo extends JpaRepository<Department, Integer>{

}
