package com.sarhabil.demo.service;

import org.springframework.http.ResponseEntity;

import com.sarhabil.demo.entity.User;

public interface ResetPasswordService {
	public ResponseEntity<?> createVerification(User user);
	public ResponseEntity<String> newPassword(String token,String password,String confirm_password);
}
