package com.sarhabil.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


import com.sarhabil.demo.entity.VerificationEmailByToken;

public interface VerificationTokenDAO extends JpaRepository<VerificationEmailByToken, Integer>{
	List<VerificationEmailByToken> findByUserEmail(String email);
//	@Query("SELECT p FROM VerificationToken p WHERE p.token LIKE %?1%")
//    List<VerificationEmailByToken> findOneToken(String token);
	List<VerificationEmailByToken> findByToken(String token);
    
    
}
