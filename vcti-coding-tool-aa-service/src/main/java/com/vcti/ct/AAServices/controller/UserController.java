package com.vcti.ct.AAServices.controller;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vcti.ct.AAServices.dao.UserDataService;
import com.vcti.ct.AAServices.model.User;

@RestController
public class UserController {
	
	private final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserDataService userDataService;

	@GetMapping(value = "/healthcheck", produces = "application/json; charset=utf-8")
	public String getHealthCheck() {
		return "{ \"isWorking\" : true }";
	}

	@GetMapping("/users")
	public List<User> getUsers() {
		return userDataService.getUsers();
	}

	@GetMapping("/user/{id}")
	public Optional<User> getUser(@PathVariable String id) {
		return userDataService.getUser(id);
	}

	@GetMapping("/user/uname/{uName}")
	public User getUserByUserName(@PathVariable String uName) {
		return userDataService.getUserByUserName(uName);
	}
	
	@PutMapping("/user/{id}")
	public Optional<User> updateUser(@RequestBody User newUser, @PathVariable String id) {
		return userDataService.updateUser(newUser, id);
	}
	
	@PutMapping("/user/uname/{uName}")
	public User updateUserUsingUserName(@RequestBody User newUser, @PathVariable String uName) {
		return userDataService.updateUserUsingUserName(newUser, uName);
	}

	@DeleteMapping(value = "/user/{id}", produces = "application/json; charset=utf-8")
	public String deleteUser(@PathVariable String id) {
		return userDataService.deleteUser(id);
	}

	@PostMapping("/user")
	public User addUser(@RequestBody User newUser) {
		return userDataService.addUser(newUser);
	}

	@GetMapping("/perm/{id}")
	public List<String> getPermissionsByUser(@PathVariable String id) {
		return userDataService.getPermissionsById(id);
	}

	@GetMapping("/permByUserName/{userName}")
	public List<String> getPermissionsByUserName(@PathVariable String userName) {
		return userDataService.getPermissionsByUserName(userName);
	}

	@GetMapping("/validateLogin/{userName}/{password}")
	public boolean validateLogin(@PathVariable String userName, @PathVariable String password) {
		return userDataService.validateLogin(userName, password);
	}

}