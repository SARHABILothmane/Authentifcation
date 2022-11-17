package com.sarhabil.demo.service;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.sarhabil.demo.entity.User;
import com.sarhabil.demo.repository.UserRepository;


@Service
public class UserDetailsServiceImpl implements UserDetailsService{
	@Autowired 
	private UserRepository userRepository;
	
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
    	
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> 
                      new UsernameNotFoundException("User Not Found with -> username or email : " + email)
      );
      
//        User user = userRepository.findByUsername(username)
//                  .orElseThrow(() -> 
//                        new UsernameNotFoundException("User Not Found with -> username or email : " + username)
//        );
 
        return UserPrinciple.build(user);
    }
	
	
	
	
	
//	@Override
//	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
//		com.sarhabil.demo.entity.User GU = seviceUser.findByEmail(email);
//		if (GU != null) {
//			return new User(GU.getEmail(),GU.getPassword(),getGrantedAuthorities(GU));
//		} 
//
//		else {
//			throw new UsernameNotFoundException("User not found with email: " + email);
//		}
//
//	}
//	private List<GrantedAuthority> getGrantedAuthorities(User user)
//	 {
//		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
//		Authority role = (Authority) user.getAuthorities();
//		
//			authorities.add(new SimpleGrantedAuthority(role.getName()));
//		System.out.println(authorities);
//		
//		return authorities;
//	}
}
