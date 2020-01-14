package com.vcti.ct.AAServices.dao;

import java.util.List;
import java.util.Optional;

import com.vcti.ct.AAServices.model.User;

public interface UserDataService {
	User addUser(User newUser);

	String deleteUser(String uName);

	Optional<User> updateUser(User newUser, String id);

	Optional<User> getUser(String id);
	
	User getUserByUserName(String uName);

	List<User> getUsers();
	
	List<String> getPermissionsById(String userId);
	
	List<String> getPermissionsByUserName(String userName);
	
	boolean validateLogin(String userId, String password);

	User updateUserUsingUserName(User newUser, String uName);
}
