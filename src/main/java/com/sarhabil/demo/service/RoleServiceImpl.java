package com.sarhabil.demo.service;



import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sarhabil.demo.entity.RoleName;
import com.sarhabil.demo.entity.Role;
import com.sarhabil.demo.repository.RoleRepository;
@Service
public class RoleServiceImpl implements RoleService{
	
	@Autowired private RoleRepository roleRepository;
	
	public Optional<Role> findByName(RoleName authorityName) {
		return roleRepository.findByName(authorityName);
	}
	
    public Role addModifierRoles(Role role) {
   	 return roleRepository.save(role);
    }
    
    public List<Role> sellectAllRoles(){
   	 return roleRepository.findAll();
    }
    
    public Optional<Role> selectOneRole(long id) {
   	 return roleRepository.findById(id);
    }
    
    public void deletRole(long id){
    	roleRepository.deleteById(id);
    }
}
