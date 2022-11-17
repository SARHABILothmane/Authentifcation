package com.sarhabil.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sarhabil.demo.entity.ResetPasswords;

public interface ResetPasswordRepository extends JpaRepository<ResetPasswords, Integer>{
	public List<ResetPasswords> findByUserEmail(String email);
	public List<ResetPasswords> findByToken(String token);

}
