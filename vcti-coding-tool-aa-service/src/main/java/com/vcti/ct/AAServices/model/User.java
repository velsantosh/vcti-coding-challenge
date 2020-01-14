package com.vcti.ct.AAServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@Table
public class User {
 
	private @NonNull String id;
	private @NonNull String name;
	@PrimaryKey
	private @NonNull String userName;
	private @NonNull String password;
	private @NonNull String role_id;

	public User() {
		super();
	}

	/*
	 * public User(@NonNull String id, @NonNull String name, @NonNull String
	 * userName, @NonNull String password) { super(); this.id = id; this.name =
	 * name; this.userName = userName; this.password = password; }
	 * 
	 * public User(@NonNull String id, @NonNull String name, @NonNull String
	 * userName, @NonNull String password,
	 * 
	 * @NonNull String role_id) { super(); this.id = id; this.name = name;
	 * this.userName = userName; this.password = password; this.role_id = role_id; }
	 */

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole_id() {
		return role_id;
	}

	public void setRole_id(String role_id) {
		this.role_id = role_id;
	}

}