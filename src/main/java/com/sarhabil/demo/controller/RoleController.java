package com.sarhabil.demo.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sarhabil.demo.entity.Role;
import com.sarhabil.demo.service.RoleService;




@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api")
public class RoleController {
@Autowired
private RoleService controllerRole;

   // @RolesAllowed("admin")
	@GetMapping("/roles")
	public List<Role> selectTousRole(){
		return controllerRole.sellectAllRoles();
	}
	//selection d'un role par ID
	@GetMapping(value = "/roles/{id}")
	public Role sel(@PathVariable("id") long id) {
		return controllerRole.selectOneRole(id).map(role -> {
			return controllerRole.selectOneRole(id).get();
		}).orElseThrow(()-> new Error());
	}
    
	//ajoutation d'un role 
    @RolesAllowed("admin")
	@PostMapping(value="/roles")
	public Role enregistreRole(@RequestBody Role r) {
		controllerRole.addModifierRoles(r);
		return r;
	}
    
	//supprission role 
	@DeleteMapping( value = "/roles/{id}")
	public void suprimeRole(@PathVariable("id") int id) {
		controllerRole.deletRole(id);
	}
	//modifier role 
	@PutMapping("/roles/{id}")
	public Role modifierRole(@RequestBody Role r , @PathVariable("id") long id) {
		r.setId(id);
		controllerRole.addModifierRoles(r);
		return r;
	//	Roles role = controllerRole.selectByIdRole(id).get();
	//	controllerRole.addModifierRoles(role);;
	//	return r;
	}
}
