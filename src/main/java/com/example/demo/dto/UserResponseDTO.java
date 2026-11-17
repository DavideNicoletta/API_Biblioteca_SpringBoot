package com.example.demo.dto;

import java.util.Date;
import java.util.Set;

public class UserResponseDTO {

	private String uuid;
	private String username;
	private String email;
	private Boolean enabled;
	private Date createdAt;
	private Set<String> roles;

	public UserResponseDTO() {
	}

	public UserResponseDTO(String uuid, String username, String email,
			Boolean enabled, Date createdAt, Set<String> roles) {
		this.uuid = uuid;
		this.username = username;
		this.email = email;
		this.enabled = enabled;
		this.createdAt = createdAt;
		this.roles = roles;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Boolean getEnabled() {
		return enabled;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}
}