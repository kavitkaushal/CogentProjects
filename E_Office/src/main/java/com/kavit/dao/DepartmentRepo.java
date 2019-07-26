package com.kavit.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import com.kavit.model.Department;

public interface DepartmentRepo extends JpaRepository<Department, Long>{

}
