package com.sarhabil.demo.repository;

import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sarhabil.demo.entity.User;
@Repository
public interface UserRepository extends JpaRepository<User, Long>{
	 
     String USERS_BY_LOGIN_CACHE = "usersByLogin";

     String USERS_BY_EMAIL_CACHE = "usersByEmail";
	
     Optional<User> findByUsername(String username);
     public Optional<User>  findByEmail(String email);
     Boolean existsByUsername(String username);
     Boolean existsByEmail(String email);
     
     
     
	 Optional<User> findOneByEmailIgnoreCase(String email);
	 
	 Optional<User> findOneByUsername(String username);
//	 
//	 @EntityGraph(attributePaths = "authorities")
//	 Optional<User> findOneWithAuthoritiesById(Long id);
//	 
//	 @EntityGraph(attributePaths = "authorities")
//	 @Cacheable(cacheNames = USERS_BY_LOGIN_CACHE)
//	 Optional<User> findOneWithAuthoritiesByUsername(String username);
//
//	 @EntityGraph(attributePaths = "authorities")
//	 @Cacheable(cacheNames = USERS_BY_EMAIL_CACHE)
//	 Optional<User> findOneWithAuthoritiesByEmailIgnoreCase(String email);
//	 
	 
	 Page<User> findAllByUsernameNot(Pageable pageable, String username);
}
