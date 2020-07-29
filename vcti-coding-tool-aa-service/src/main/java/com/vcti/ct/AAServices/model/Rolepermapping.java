package com.vcti.ct.AAServices.model;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@Table
public class Rolepermapping {
	@PrimaryKey
	private @NonNull String id;
	private @NonNull String roleId;
	private @NonNull String permissionId;

	public Rolepermapping(@NonNull String id, @NonNull String roleId, @NonNull String permissionId) {
		super();
		this.id = id;
		this.roleId = roleId;
		this.permissionId = permissionId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleId() {
		return roleId;
	}

	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}

	public String getPermissionId() {
		return permissionId;
	}

	public void setPermissionId(String permissionId) {
		this.permissionId = permissionId;
	}
}