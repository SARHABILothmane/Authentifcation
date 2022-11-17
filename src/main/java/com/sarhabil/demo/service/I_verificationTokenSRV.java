package com.sarhabil.demo.service;

import org.springframework.http.ResponseEntity;

import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.entity.VerificationEmailByToken;



public interface I_verificationTokenSRV {
	public VerificationEmailByToken createVerification(User user);
	public ResponseEntity<String> verifyEmail(String token);
}
