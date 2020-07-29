package com.vcti.ct.AAServices.dao;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.vcti.ct.AAServices.model.PermissionDTO;
import com.vcti.ct.AAServices.model.User;

public interface UserDataService {
	User addUser(User newUser);

	String deleteUser(String uName);

	Optional<User> updateUser(User newUser, String id);

	Optional<User> getUser(String id);

	User getUserByUserId(String uName);

	List<User> getUsers();

	List<String> getPermissionsById(String userId);

	List<String> getPermissionsByUserId(String userName);

	boolean validateLogin(String userId, String password);

	User updateUserUsingUserId(User newUser, String uName);

	List<User> getUsersByRole(String role);

	User updateUserPassword(User newUser, String uId);

	boolean updateRolePermissions(PermissionDTO permissionList, String roleId);

	List<String> getPermissionIdByRole(String roleId);
}
