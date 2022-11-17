package com.sarhabil.demo.service;



import java.util.List;
import java.util.Optional;

import com.sarhabil.demo.entity.RoleName;
import com.sarhabil.demo.entity.Role;


public interface RoleService {
	
	public Optional<Role> findByName(RoleName authorityName);
    public Role addModifierRoles(Role role);
    public List<Role> sellectAllRoles();
    public Optional<Role> selectOneRole(long id);
    public void deletRole(long id);
}
