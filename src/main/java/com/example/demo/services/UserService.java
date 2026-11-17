package com.example.demo.services;


import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.UserMapper;
import com.example.demo.models.User;
import com.example.demo.models.Role;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;


@Service
public class UserService implements IReadService<UserResponseDTO>, IWriteService<UserRequestDTO>{


	private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
	
	public UserService(UserRepository userRepository, 
			RoleRepository roleRepository,
			PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Transactional
	@Override
	public List<UserResponseDTO> findAll() {
		return userRepository.findAll().stream().map(UserMapper::toDTO).toList();
	}

	@Transactional
	@Override
	public Optional<UserResponseDTO> findByUuid(String uuid) {
		return userRepository.findByUuid(uuid).map(UserMapper::toDTO);
	}
	
	@Transactional
	public Optional<UserResponseDTO> findByUsername(String username){
		return userRepository.findByUsername(username).map(UserMapper::toDTO);
	}

	@Transactional
	public UserResponseDTO Insert(UserRequestDTO obj) {
		
		if (userRepository.existsByUsername(obj.getUsername())) {
		    throw new ConflictException("Username già in uso");
		}
		if (userRepository.existsByEmail(obj.getEmail())) {
		    throw new ConflictException("Email già in uso");
		}
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setEmail(obj.getEmail());
        
        user.setPassword(passwordEncoder.encode(obj.getPassword()));
        
        user.setUuid(UUID.randomUUID().toString());
        user.setEnabled(true);

        Role defaultRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new IllegalStateException("Ruolo ROLE_USER non presente nel DB"));

        Set<Role> roles = new HashSet<>();
        roles.add(defaultRole);
        user.setRuolo(roles);

        User savedUser = userRepository.save(user);

        return toResponseDTO(savedUser);
	}

	@Override
	public boolean Update(UserRequestDTO obj) {
		return false;
	}

	@Override
	public boolean Delete(String uuid) {
		Optional<User> user = userRepository.findByUuid(uuid);
		if(user.isPresent()) {
			userRepository.delete(user.get());
			return true;
		}
		return false;
	}
	
	@Transactional
	public boolean deleteByUsername(String username) {
		Optional<User> user = userRepository.findByUsername(username);
		if(user.isPresent()) {
			userRepository.delete(user.get());
			return true;
		}
		return false;
	}
	
	
	private UserResponseDTO toResponseDTO(User user) {
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
	
	@Transactional
    public UserResponseDTO updateUser(String uuid, UserRequestDTO requestDTO) {
		
        Optional<User> userOptional = userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) {
        	throw new NotFoundException("Utente non trovato");
        }

        User existingUser = userOptional.get();


        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            if (!requestDTO.getUsername().equals(existingUser.getUsername())
                    && userRepository.existsByUsername(requestDTO.getUsername())) {
                throw new ConflictException("Username già in uso");
            }
            existingUser.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getEmail() != null && !requestDTO.getEmail().isBlank()) {
            if (!requestDTO.getEmail().equals(existingUser.getEmail())
                    && userRepository.existsByEmail(requestDTO.getEmail())) {
                throw new ConflictException("Email già in uso");
            }
            existingUser.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {

            existingUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        }


        if (requestDTO.getRoles() != null && !requestDTO.getRoles().isEmpty()) {
            Set<Role> updatedRoles = new HashSet<>();
            for (String roleName : requestDTO.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Ruolo non trovato: " + roleName));
                updatedRoles.add(role);
            }
            existingUser.setRuolo(updatedRoles);
        }


        User savedUser = userRepository.save(existingUser);


        return toResponseDTO(savedUser);
    }


}
