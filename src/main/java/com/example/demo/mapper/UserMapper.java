package com.example.demo.mapper;

import java.util.Set;
import java.util.stream.Collectors;

import com.example.demo.dto.UserResponseDTO;
import com.example.demo.models.Role;
import com.example.demo.models.User;

public class UserMapper {
	
	public UserMapper() {
		
	}
	
	public static UserResponseDTO toDTO(User user) {
	    Set<String> roleNames = user.getRuolo() != null
	            ? user.getRuolo().stream().map(Role::getName).collect(Collectors.toSet())
	            : Set.of();

	    return new UserResponseDTO(
	            user.getUuid(),
	            user.getUsername(),
	            user.getEmail(),
	            user.getEnabled(),
	            user.getCreated_at(),
	            roleNames
	    );
	}
}
