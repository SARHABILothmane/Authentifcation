package com.sarhabil.demo.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sarhabil.demo.email.I_emailSRV;
import com.sarhabil.demo.entity.ResetPasswords;
import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.entity.VerificationEmailByToken;
import com.sarhabil.demo.repository.ResetPasswordRepository;


@Service
public class ResetPasswordServiceImp implements ResetPasswordService{

	@Autowired private ResetPasswordRepository resetPasswordRepo;
	@Autowired private I_emailSRV emailSRV;
	@Autowired private PasswordEncoder passwordEncoder;
	
	
	public ResponseEntity<?> createVerification(User user){
		 List<ResetPasswords> resetPasswords = resetPasswordRepo.findByUserEmail(user.getEmail());
		 ResetPasswords resetPassword;
		 if (resetPasswords.isEmpty()) {
			 resetPassword = new ResetPasswords();
			 resetPassword.setUser(user);
			 resetPasswordRepo.save(resetPassword);
	        } else {
	        	resetPassword = resetPasswords.get(0);
	        	if(resetPassword.getExpiredDateTime().isBefore(LocalDateTime.now())) {
	        		resetPassword = newExpiredDateAndToken(resetPassword);
	            }
	        }
		 emailSRV.sendRestPassawordEmail(user.getEmail(), resetPassword.getToken());
		return ResponseEntity.ok(resetPassword);
	}
	public ResponseEntity<String> newPassword(String token,String password,String confirm_password){
		List<ResetPasswords> resetPasswords = resetPasswordRepo.findByToken(token);
		 if (resetPasswords.isEmpty()) {
		        return ResponseEntity.badRequest().body("Invalid token.");
		    }
		 ResetPasswords resetPassword = resetPasswords.get(0);
	    if (resetPassword.getExpiredDateTime().isBefore(LocalDateTime.now())) {

	        return ResponseEntity.unprocessableEntity().body("Expired token.");
	    }
	    if (!password.equals(confirm_password)) {

	        return ResponseEntity.unprocessableEntity().body("password not confirmed.");
	    }
	    if (resetPassword.getStatus().equals(VerificationEmailByToken.STATUS_VERIFIED)) {

	        return ResponseEntity.unprocessableEntity().body("This link has been used before");
	    }
	    
	    resetPassword.setConfirmedDateTime(LocalDateTime.now());
	    resetPassword.setStatus(VerificationEmailByToken.STATUS_VERIFIED);
	    resetPassword.getUser().setPassword(passwordEncoder.encode(password));
	    resetPasswordRepo.save(resetPassword);
	
	    return ResponseEntity.ok("You have successfully change your password.");
	}

	public ResetPasswords newExpiredDateAndToken(ResetPasswords resetPassword) {
		ResetPasswords newResetPassword = new ResetPasswords();
		resetPassword.setExpiredDateTime(newResetPassword.getExpiredDateTime());
		resetPassword.setToken(newResetPassword.getToken());
		resetPassword.setStatus(newResetPassword.getStatus());
		
		return resetPasswordRepo.save(resetPassword);
	}
	
	
}
