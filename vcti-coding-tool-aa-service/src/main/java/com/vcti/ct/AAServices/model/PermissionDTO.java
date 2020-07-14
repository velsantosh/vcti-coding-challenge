package com.vcti.ct.AAServices.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class PermissionDTO {
	
	private @NonNull List<String> permissions;
	
	public PermissionDTO() {
		
	}

	public List<String> getPermissions() {
		return permissions;
	}
	
	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}
	

}
