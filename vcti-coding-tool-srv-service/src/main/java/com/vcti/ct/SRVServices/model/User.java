package com.vcti.ct.SRVServices.model;


import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Table
public class User {

	private @NonNull String id;
	private @NonNull String name;
	@PrimaryKey
	private @NonNull String userId;
	private @NonNull String password;
	private @NonNull String roleId;
	private @NonNull Integer experience;
	private byte[] byteAttachemenets;
	public User() {
		super();
	}

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

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public Integer getExperience() {
		return experience;
	}

	public void setExperience(Integer experience) {
		this.experience = experience;
	}

	public byte[] getByteAttachemenets() {
		return byteAttachemenets;
	}

	public void setByteAttachemenets(byte[] byteAttachemenets) {
		this.byteAttachemenets = byteAttachemenets;
	}
}
