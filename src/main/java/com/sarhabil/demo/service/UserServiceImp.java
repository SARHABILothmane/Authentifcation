package com.sarhabil.demo.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.repository.UserRepository;

@Service
public class UserServiceImp implements UserService{

	@Autowired private UserRepository userRepository;
	public Optional<User>  findByEmail(String email) {
		return userRepository.findByEmail(email);
	}
	public Optional<User>  findByUsername(String username) {
		return userRepository.findByUsername(username);
	}
	public Boolean existsByUsername(String username) {
		return userRepository.existsByUsername(username);
	}
	public Boolean existsByEmail(String email) {
		return userRepository.existsByEmail(email);
	}
	
	public User saveUser(User user) {
		return userRepository.save(user);
	}
}
