package com.example.demo.services;

import java.beans.Transient;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
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
	
	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
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
		// 1. Istanzia la nuova Entity User
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setEmail(obj.getEmail());
        
        // 2. Password (se hai PasswordEncoder fai: passwordEncoder.encode(requestDTO.getPassword()))
        user.setPassword(obj.getPassword());
        
        // 3. Genera UUID univoco lato Java
        user.setUuid(UUID.randomUUID().toString());
        user.setEnabled(true);

        // 4. Mappa i ruoli: da Set<String> (nel UserRequestDTO) a Set<Role> (nel DB)
        Set<Role> roles = new HashSet<>();
        if (obj.getRoles() != null && !obj.getRoles().isEmpty()) {
            for (String roleName : obj.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Ruolo non trovato: " + roleName));
                roles.add(role);
            }
        } else {
            // Assegna il ruolo predefinito ROLE_USER se nessun ruolo è stato specificato nel payload
            Role defaultRole = roleRepository.findByName("ROLE_USER")
                    .orElseThrow(() -> new RuntimeException("Ruolo predefinito ROLE_USER non presente nel DB"));
            roles.add(defaultRole);
        }
        user.setRuolo(roles);

        // 5. Esegue la INSERT sul database tramite save()
        User savedUser = userRepository.save(user);

        // 6. Converte l'utente appena salvato in UserResponseDTO e lo restituisce
        return toResponseDTO(savedUser);
	}

	@Override
	public boolean Update(UserRequestDTO obj) {
		// TODO Auto-generated method stub
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
	public boolean delteByUsername(String username) {
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
        // 1. Cerca l'utente esistente tramite UUID
        Optional<User> userOptional = userRepository.findByUuid(uuid);
        if (userOptional.isEmpty()) {
            return null; // Utente non trovato
        }

        User existingUser = userOptional.get();

        // 2. Aggiorna i campi consentiti
        if (requestDTO.getUsername() != null && !requestDTO.getUsername().isBlank()) {
            existingUser.setUsername(requestDTO.getUsername());
        }
        if (requestDTO.getEmail() != null && !requestDTO.getEmail().isBlank()) {
            existingUser.setEmail(requestDTO.getEmail());
        }
        if (requestDTO.getPassword() != null && !requestDTO.getPassword().isBlank()) {
            // Se usi PasswordEncoder: existingUser.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
            existingUser.setPassword(requestDTO.getPassword());
        }

        // 3. Aggiorna i ruoli se specificati nel payload
        if (requestDTO.getRoles() != null && !requestDTO.getRoles().isEmpty()) {
            Set<Role> updatedRoles = new HashSet<>();
            for (String roleName : requestDTO.getRoles()) {
                Role role = roleRepository.findByName(roleName)
                        .orElseThrow(() -> new RuntimeException("Ruolo non trovato: " + roleName));
                updatedRoles.add(role);
            }
            existingUser.setRuolo(updatedRoles);
        }

        // 4. Salva le modifiche (esegue UPDATE sul database)
        User savedUser = userRepository.save(existingUser);

        // 5. Converte in ResponseDTO
        return toResponseDTO(savedUser);
    }


}
