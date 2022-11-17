package com.sarhabil.demo.controller;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sarhabil.demo.config.JwtProvider;
import com.sarhabil.demo.entity.RoleName;
import com.sarhabil.demo.entity.Role;
import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.exception.ApiResponse;
import com.sarhabil.demo.request.LoginForm;
import com.sarhabil.demo.request.SignUpForm;
import com.sarhabil.demo.response.JwtResponse;
import com.sarhabil.demo.service.RoleService;
import com.sarhabil.demo.service.UserService;
import com.sarhabil.demo.repository.UserRepository;
import com.sarhabil.demo.service.I_verificationTokenSRV;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthRestAPI {
	
    @Autowired private AuthenticationManager authenticationManager;
	@Autowired private UserService userService;
	@Autowired private RoleService roleService;
    @Autowired private PasswordEncoder encoder;
    @Autowired private JwtProvider jwtProvider;
    @Autowired private  I_verificationTokenSRV TokenSRV;
    
   // @Autowired private UserRepository userRepository;     
		// Add 
		@PostMapping(value = "/inscription")
		private ResponseEntity<String>  addOrUpdateUser(@RequestBody User user) {
			if(user.getPassword().equals(user.getConfirmPassword())) {
				user.setPassword(encoder.encode(user.getPassword()));
				userService.saveUser(user);
				TokenSRV.createVerification(user);
			}else {
			       return new ResponseEntity<String>("Fail -> password is already in use!",
		                    HttpStatus.BAD_REQUEST);
			}
			//user.setPassword(encoder.encode(user.getPassword()));

			  return new ResponseEntity<String>("niceeeee",
					  HttpStatus.OK);	
		}
		
	    @PostMapping(value = "/registration")
	    public ResponseEntity<ApiResponse> registration(@Valid @RequestBody User user) {
	    	user.setPassword(encoder.encode(user.getPassword()));
		//	user.setConfirmPassword(encoder.encode(user.getConfirmPassword()));
	    // 	BCryptPasswordEncoder bcryptEncoder = new BCryptPasswordEncoder();
			userService.saveUser(user);
			TokenSRV.createVerification(user);
	        return new ResponseEntity<>(
	                new ApiResponse(user, "success", false), HttpStatus.OK);

	    }
    
    
    
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginForm loginRequest) {
 
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );
 
        SecurityContextHolder.getContext().setAuthentication(authentication);
 
        String jwt = jwtProvider.generateJwtToken(authentication);
        return ResponseEntity.ok(new JwtResponse(jwt));
    }
 
    @PostMapping("/signup")
    public ResponseEntity<String> registerUser(@Valid @RequestBody SignUpForm signUpRequest) {
        if(userService.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<String>("Fail -> Username is already taken!",
                    HttpStatus.BAD_REQUEST);
        }
 
        if(userService.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<String>("Fail -> Email is already in use!",
                    HttpStatus.BAD_REQUEST);
        }
 
        // Creating user's account
        User user = new User(signUpRequest.getUsername(),signUpRequest.getFirstName(), 
                signUpRequest.getEmail(), encoder.encode(signUpRequest.getPassword()));
 
        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();
 
        strRoles.forEach(role -> {
          switch(role) {
          case "admin":
            Role adminRole = roleService.findByName(RoleName.ROLE_ADMIN)
                  .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
            roles.add(adminRole);
            
            break;
          case "pm":
                Role pmRole = roleService.findByName(RoleName.ROLE_PM)
                  .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
                roles.add(pmRole);
                
            break;
          default:
              Role userRole = roleService.findByName(RoleName.ROLE_USER)
                  .orElseThrow(() -> new RuntimeException("Fail! -> Cause: User Role not find."));
              roles.add(userRole);              
          }
        });
        
        user.setRoles(roles);
        userService.saveUser(user);
 
        return ResponseEntity.ok().body("User registered successfully!");
    }
}
