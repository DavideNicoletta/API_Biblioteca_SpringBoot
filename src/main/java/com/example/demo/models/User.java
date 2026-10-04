package com.example.demo.models;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name="Users")
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	@Column(nullable = false, unique = true, length = 36, insertable = false, updatable = false)
	private String 	uuid;
	@Column(nullable = false, unique = true, length = 50)
	private String 	username;
	@Column(nullable = false, unique = true, length = 100)
	private String 	email;
	@Column(nullable = false)
	private String 	password;
	@Column(nullable = false)
	private Boolean enabled = true;
	@Column(name = "created_at", updatable = false)
	private Date 	created_at;
	
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(
			name="user_roles",
			joinColumns = @JoinColumn(name="user_id"),
			inverseJoinColumns = @JoinColumn(name="role_id"))
	private Set<Role> ruolo = new HashSet<>();
	
		
	public User() {
		super();
		// TODO Auto-generated constructor stub
	}

	public User(int id, String uuid, String username, String email, String password, Boolean enabled, Date created_at) {
		super();
		this.username = username;
		this.email = email;
		this.password = password;
		this.enabled = enabled;
		this.created_at = created_at;
	}
	
	
	
	
	public Set<Role> getRuolo() {
		return ruolo;
	}

	public void setRuolo(Set<Role> ruolo) {
		this.ruolo = ruolo;
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
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public Date getCreated_at() {
		return created_at;
	}
	public void setCreated_at(Date created_at) {
		this.created_at = created_at;
	}
	
	
	
}
