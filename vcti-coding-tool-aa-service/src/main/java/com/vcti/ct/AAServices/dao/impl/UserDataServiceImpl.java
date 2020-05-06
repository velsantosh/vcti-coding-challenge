package com.vcti.ct.AAServices.dao.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.vcti.ct.AAServices.dao.UserDataService;
import com.vcti.ct.AAServices.model.Permissions;
import com.vcti.ct.AAServices.model.Role;
import com.vcti.ct.AAServices.model.Rolepermapping;
import com.vcti.ct.AAServices.model.User;
import com.vcti.ct.AAServices.repository.RolePerMappingRepository;
import com.vcti.ct.AAServices.repository.RoleRepository;
import com.vcti.ct.AAServices.repository.UserRepository;

@Component
@Service
public class UserDataServiceImpl implements UserDataService {
	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Autowired
	RolePerMappingRepository rolePerMappingRepository;

	@Autowired
	Permissions perm;

	@Override
	public User addUser(User newUser) {
		// Fetch User Role Id from Role table
		Role role = roleRepository.findDistinctByRoleName(newUser.getRoleId()).get(0);
		newUser.setRoleId(role.getId());
		if (newUser.getId() == null) {
//			String id = String.valueOf(new Random().nextInt());
			newUser.setId(getId("User"));
		}
		userRepository.save(newUser);
		return newUser;
	}

	@Override
	public String deleteUser(String userId) {
		Boolean result = userRepository.existsByUserId(userId);
		userRepository.deleteByUserId(userId);
		return "{ \"success\" : " + (result ? "true" : "false") + " }";
	}

	@Override
	public Optional<User> updateUser(User newUser, String id) {

		Optional<User> optionalUser = userRepository.findById(id);

		if (optionalUser.isPresent()) {
			User user = optionalUser.get();
			user.setId(id);
			user.setName(newUser.getName());
			user.setUserId(newUser.getUserId());
			user.setPassword(newUser.getPassword());
			user.setExperience(newUser.getExperience());
			// Fetch User Role Id from Role table
			Role role = roleRepository.findDistinctByRoleName(newUser.getRoleId()).get(0);
			user.setRoleId(role.getId());
			userRepository.save(user);
		}
		return optionalUser;

	}

	@Override
	public User updateUserUsingUserId(User newUser, String uId) {

		List<User> userList = userRepository.findByUserId(uId);
		User user;
		if (userList.size() == 1) {
			user = userList.get(0);
		} else {
			System.out.println("No Unique User found with the provided User Id");
			return null;
		}
		user.setName(newUser.getName());
		user.setId(uId);
		user.setUserId(uId);
		user.setPassword(newUser.getPassword());
		user.setExperience(newUser.getExperience());
		// Fetch User Role Id from Role table
		Role role = roleRepository.findDistinctByRoleName(newUser.getRoleId()).get(0);
		user.setRoleId(role.getId());
		userRepository.save(user);
		return user;

	}

	@Override
	public Optional<User> getUser(String id) {
		Optional<User> user = userRepository.findById(id);
		if (user.isPresent())
			setRoleName(user.get());
		return user;
	}

	@Override
	public User getUserByUserId(String uName) {
		List<User> user = userRepository.findByUserId(uName);
		if (user.size() == 1) {
			setRoleName(user.get(0));
			return user.get(0);
		} else {
			System.out.println("No Unique User found with the provided User Id");
			return null;
		}

	}

	public Optional<Role> getRole(String id) {
		Optional<Role> role = roleRepository.findById(id);
		return role;
	}

	@Override
	public List<User> getUsers() {
		Iterable<User> result = userRepository.findAll();
		List<User> usersList = new ArrayList<User>();
		for (User user : result) {
			setRoleName(user);
			usersList.add(user);
		}
		return usersList;
	}

	private void setRoleName(User user) {
		Optional<Role> role = getRole(user.getRoleId());
		if (role.isPresent()) {
			user.setRoleId(role.get().getRoleName());
		}
	}

	@Override
	public List<String> getPermissionsById(String userId) {
		List<String> permList = null;
		// Fetch Role Id from the user table
		Optional<User> user = userRepository.findById(userId);
		String roleId = null;
		if (user.isPresent()) {
			roleId = user.get().getRoleId();
		}
		// Fetch Permission Id Set for roleId from RolePerMapping table
		Set<String> permIdSet = getPermissionIdSet(roleId);

		// Fetch list of permission from properties file
		if (perm == null || perm.getPerm() == null || perm.getPerm().length == 0) {
			System.out.println("Permission Properties not loaded properly");
			return null;
		} else {
			System.out.println("Permission Properties loaded successfully");
			permList = new ArrayList<String>();
			for (String permId : permIdSet) {
				int permIdInt = Integer.parseInt(permId);
				permList.add(perm.getPerm()[permIdInt]);
			}
		}
		return permList;
	}

	private Set<String> getPermissionIdSet(String roleId) {
		List<Rolepermapping> rolePerMappingList = rolePerMappingRepository.findByRoleId(roleId);
		Set<String> permIdSet = new HashSet<String>();
		for (Rolepermapping mapping : rolePerMappingList) {
			if (roleId.equals(mapping.getRoleId())) {
				permIdSet.add(mapping.getPermissionId());
			}
		}
		return permIdSet;
	}

	@Override
	public List<String> getPermissionsByUserId(String userId) {
		List<String> permList = null;
		// Fetch Role Id from the user table
		List<User> userList = userRepository.findByUserId(userId);
		if (userList == null || userList.size() <= 0) {
			System.out.println("User Name not found in the User Table");
			return null;
		}
		String roleId = userList.get(0).getRoleId();
		// Fetch Permission Id Set for roleId from RolePerMapping table
		Set<String> permIdSet = getPermissionIdSet(roleId);

		// Fetch list of permission from properties file
		if (perm == null || perm.getPerm() == null || perm.getPerm().length == 0) {
			System.out.println("Permission Properties not loaded properly");
			return null;
		} else {
			System.out.println("Permission Properties loaded successfully");
			permList = new ArrayList<String>();
			for (String permId : permIdSet) {
				int permIdInt = Integer.parseInt(permId);
				permList.add(perm.getPerm()[permIdInt]);
			}
		}
		return permList;
	}

	@Override
	public boolean validateLogin(String userId, String password) {
		List<User> user = userRepository.findByUserIdAndPassword(userId, password);
		return user != null && user.size() > 0;
	}

	@Override
	public List<User> getUsersByRole(String role) {
		role = role.toUpperCase();
		Role roleObj = roleRepository.findDistinctByRoleName(role).get(0);
		if (roleObj == null) {
			System.out.println("No Role found");
			return null;
		}
		List<User> userList = userRepository.findByRoleId(roleObj.getId());
		return userList;
	}

	private String getId(String questionType) {
		String id = questionType + "X" + new Random().nextInt(100000) + "X" + System.currentTimeMillis();
		return id;
	}
}
