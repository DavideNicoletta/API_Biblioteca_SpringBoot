package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.UserRequestDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.response.ResponseAPI;
import com.example.demo.services.UserService;

@RestController
@RequestMapping("/api/user")
public class UserController {

	private UserService service;

	public UserController(UserService service) {
		this.service = service;
	}

	@GetMapping
	public ResponseEntity<ResponseAPI<List<UserResponseDTO>>> findAll() {
		return ResponseEntity.ok(ResponseAPI.success(service.findAll()));
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<ResponseAPI<UserResponseDTO>> findByUuid(@PathVariable String uuid) {
		UserResponseDTO data = service.findByUuid(uuid)
				.orElseThrow(() -> new NotFoundException("Utente non trovato"));
		return ResponseEntity.ok(ResponseAPI.success(data));
	}

	@GetMapping("/username/{username}")
	public ResponseEntity<ResponseAPI<UserResponseDTO>> findByUsername(@PathVariable String username) {
		UserResponseDTO data = service.findByUsername(username)
				.orElseThrow(() -> new NotFoundException("Utente non trovato"));
		return ResponseEntity.ok(ResponseAPI.success(data));
	}

	@PostMapping
	public ResponseEntity<ResponseAPI<UserResponseDTO>> addUser(@RequestBody UserRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseAPI.success(service.Insert(dto)));
	}

	@PutMapping("/{uuid}")
	public ResponseEntity<ResponseAPI<UserResponseDTO>> updateUser(
			@PathVariable String uuid,
			@RequestBody UserRequestDTO request) {
		return ResponseEntity.ok(ResponseAPI.success(service.updateUser(uuid, request)));
	}

	@DeleteMapping("/{uuid}")
	public ResponseEntity<Void> deleteUserByUuid(@PathVariable String uuid) {
		if (!service.Delete(uuid)) {
			throw new NotFoundException("Utente non trovato");
		}
		return ResponseEntity.noContent().build();
	}

	@DeleteMapping("/username/delete/{username}")
	public ResponseEntity<Void> deleteUserByUsername(@PathVariable String username) {
		if (!service.deleteByUsername(username)) {
			throw new NotFoundException("Utente non trovato");
		}
		return ResponseEntity.noContent().build();
	}
}