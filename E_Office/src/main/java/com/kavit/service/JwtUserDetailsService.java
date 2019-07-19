package com.kavit.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JwtUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		if ("kavit".equals(username)) {
			return new User("kavit", "$2a$10$mHOosbEPlGkO/eVr.U04Yes5ZLhzR3We5RVA71iCkXDVTv.U/hl1S",
		new ArrayList<>());
		} else {
		throw new UsernameNotFoundException("User not found with username: " + username);
		}
	}

}