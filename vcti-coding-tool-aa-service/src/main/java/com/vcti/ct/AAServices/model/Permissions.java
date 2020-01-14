package com.vcti.ct.AAServices.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@EnableConfigurationProperties
@Component
@ConfigurationProperties
@PropertySource("classpath:permission.properties")
public class Permissions {
	String[] perm;

	public String[] getPerm() {
		return perm;
	}

	public void setPerm(String[] perm) {
		this.perm = perm;
	}

}
