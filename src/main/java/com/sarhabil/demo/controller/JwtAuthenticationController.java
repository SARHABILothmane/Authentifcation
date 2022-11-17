package com.sarhabil.demo.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarhabil.demo.config.JwtProvider;
import com.sarhabil.demo.email.I_emailSRV;
import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.entity.VerificationEmailByToken;
import com.sarhabil.demo.request.LoginForm;
import com.sarhabil.demo.response.JwtResponse;
import com.sarhabil.demo.service.I_verificationTokenSRV;
import com.sarhabil.demo.service.ResetPasswordService;
import com.sarhabil.demo.service.UserDetailsServiceImpl;
import com.sarhabil.demo.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/public")
public class JwtAuthenticationController {
	@Autowired
	private AuthenticationManager authenticationManager;
	@Autowired
	private JwtProvider jwtTokenUtil;
	@Autowired
	private UserDetailsServiceImpl userDetailsService;	
	@Autowired 
	private I_emailSRV emailSRV;
	@Autowired
	private I_verificationTokenSRV verificationTokenSRV;
	@Autowired
	private UserService usersSRV;
	@Autowired
	private ResetPasswordService resetPasswordService;

	
	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody User user) throws Exception {
		//authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());	
		//final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                		user.getEmail(),
                		user.getPassword()
                )
        );
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
	final String token = jwtTokenUtil.generateJwtToken(authentication);	
	return ResponseEntity.ok(new JwtResponse(token));
	}	
	
//	private void authenticate(String username, String password) throws Exception {
//		try {
//			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
//		} catch (DisabledException e) {
//			
//			throw new Exception("USER_DISABLED", e);
//			
//		} catch (BadCredentialsException e) {
//			throw new Exception("INVALID_CREDENTIALS", e);
//		}
//	}
	@PostMapping(value = "/send-active-email/{email}")
	public ResponseEntity<?> testmail(@PathVariable("email") String email) {
		
		User GU = usersSRV.findByEmail(email).get();
		//Professionnels pro = professionnelsSRV.selectByIdProfessionnels(GU.getId()).get();
		VerificationEmailByToken verificationToken = verificationTokenSRV.createVerification(GU);
		//emailSRV.sendVerificationMail(email, verificationToken.getToken());
		return ResponseEntity.ok(verificationToken);
	}
	
	@GetMapping(value = "/active-email/{token}")
	public ResponseEntity<?> activeEmailByToken(@PathVariable("token") String token) {
		return ResponseEntity.ok(verificationTokenSRV.verifyEmail(token));
	}
	
	@GetMapping(value = "/reset-passaword/{email}")
	public ResponseEntity<?> resetpassword(@PathVariable("email") String email) {
		if(usersSRV.existsByEmail(email)) {
			User GU = usersSRV.findByEmail(email).get();
			if(GU.isActivated() == false) {
				return ResponseEntity.ok("this User not active");
			}
			
			return ResponseEntity.ok(resetPasswordService.createVerification(GU));
		}else {
			return ResponseEntity.ok("not exist");
		}
		
		
		}
	
	@PostMapping(value = "/new-password/{token}/{password}/{confirme}")
	public ResponseEntity<?> resetpasswordByToken(@PathVariable("token") String token,@PathVariable("password") String password,
			@PathVariable("confirme") String confirme) {
		
		return ResponseEntity.ok(resetPasswordService.newPassword(token, password, confirme));
	}
}
