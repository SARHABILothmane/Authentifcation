package com.sarhabil.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


import com.sarhabil.demo.entity.RoleName;
import com.sarhabil.demo.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long>{
	Optional<Role> findByName(RoleName authorityName);
}
