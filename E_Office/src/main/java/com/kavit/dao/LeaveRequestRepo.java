package com.kavit.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kavit.model.LeaveRequest;

public interface LeaveRequestRepo extends JpaRepository<LeaveRequest, Long>{

}
