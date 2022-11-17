package com.sarhabil.demo.service;



import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.sarhabil.demo.email.I_emailSRV;
import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.entity.VerificationEmailByToken;
import com.sarhabil.demo.repository.UserRepository;
import com.sarhabil.demo.repository.VerificationTokenDAO;



@Service
public class VerificationTokenSRV implements I_verificationTokenSRV{
	@Autowired
	private VerificationTokenDAO verificationTokenDAO;
	@Autowired 
	private I_emailSRV emailSRV;
	
	public VerificationEmailByToken createVerification(User user){
        List<VerificationEmailByToken> verificationTokens = verificationTokenDAO.findByUserEmail(user.getEmail());
        VerificationEmailByToken verificationToken;
        if (verificationTokens.isEmpty()) {
            verificationToken = new VerificationEmailByToken();
            verificationToken.setUser(user);
            verificationTokenDAO.save(verificationToken);
        } else {
            verificationToken = verificationTokens.get(0);
            if(verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {
            	verificationToken = newVerificationToken(verificationToken);
            }
        }
        emailSRV.sendVerificationMail(user.getEmail(), verificationToken.getToken());
		return verificationToken;
	}
	public ResponseEntity<String> verifyEmail(String token){
		List<VerificationEmailByToken> verificationTokens = verificationTokenDAO.findByToken(token);
		System.out.println(token);
		System.out.println(verificationTokens);
		
	    if (verificationTokens.isEmpty()) {
	        return ResponseEntity.badRequest().body("Invalid token.");
	    }
	
	    VerificationEmailByToken verificationToken = verificationTokens.get(0);
	    if (verificationToken.getExpiredDateTime().isBefore(LocalDateTime.now())) {

	        return ResponseEntity.unprocessableEntity().body("Expired token.");
	    }
	
	    verificationToken.setConfirmedDateTime(LocalDateTime.now());
	    verificationToken.setStatus(VerificationEmailByToken.STATUS_VERIFIED);
	    verificationToken.getUser().setActivated(true);
	    verificationToken.getUser().setLangKey("teb");
	    verificationTokenDAO.save(verificationToken);
	
	    return ResponseEntity.ok("You have successfully verified your email address.");
	}
	public VerificationEmailByToken newVerificationToken(VerificationEmailByToken verificationToken) {
		VerificationEmailByToken newVerificationToken = new VerificationEmailByToken();
		verificationToken.setExpiredDateTime(newVerificationToken.getExpiredDateTime());
		verificationToken.setToken(newVerificationToken.getToken());
		verificationToken.setStatus(newVerificationToken.getStatus());
	
	return verificationTokenDAO.save(verificationToken);
}
}
