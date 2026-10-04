package com.example.demo.dto;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

import com.example.demo.models.Role;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

public class UserResponseDTO {


	private String 	uuid;
	private String 	user;
	private String 	emai;
	private Boolean enab;
	private Date 	crea;
	private Set<String> ruol;

	public UserResponseDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public UserResponseDTO(String uuid, String username, String email, Boolean enabled, Date createdAt, Set<String> roles) {
        this.uuid = uuid;
        this.user = username;
        this.emai = email;
        this.enab = enabled;
        this.crea = createdAt;
        this.ruol = roles;
    }
	
	
	public Set<String> getRuol() {
		return ruol;
	}



	public void setRuol(Set<String> ruol) {
		this.ruol = ruol;
	}



	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getEmai() {
		return emai;
	}

	public void setEmai(String emai) {
		this.emai = emai;
	}

	public Boolean getEnab() {
		return enab;
	}

	public void setEnab(Boolean enab) {
		this.enab = enab;
	}

	public Date getCrea() {
		return crea;
	}

	public void setCrea(Date crea) {
		this.crea = crea;
	}
	
	
	
}
