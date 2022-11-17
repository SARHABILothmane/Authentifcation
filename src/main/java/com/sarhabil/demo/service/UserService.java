package com.sarhabil.demo.service;

import java.util.Optional;

import com.sarhabil.demo.entity.User;

public interface UserService {
	 
	public Optional<User> findByUsername(String username);
	public Boolean existsByUsername(String username);
	public Boolean existsByEmail(String email);
	public Optional<User> findByEmail(String email);
	public  User saveUser(User user);
}
