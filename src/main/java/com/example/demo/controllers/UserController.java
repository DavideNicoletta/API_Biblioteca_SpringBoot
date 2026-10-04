package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.example.demo.models.User;
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
	public ResponseEntity<ResponseAPI<List<UserResponseDTO>>> findAll(){
		List<UserResponseDTO> data = service.findAll();
		if(data!=null) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
		
		 
	}
	
	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<ResponseAPI<Optional<UserResponseDTO>>> findByUuid(@PathVariable String uuid) {
		Optional<UserResponseDTO> data = service.findByUuid(uuid);
		if(!data.isEmpty()) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/username/{username}")
	public ResponseEntity<ResponseAPI<Optional<UserResponseDTO>>> findByUsername(@PathVariable String username){
		Optional<UserResponseDTO> data = service.findByUsername(username);
		if(!data.isEmpty()) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.status(HttpStatus.NO_CONTENT).body(ResponseAPI.error("Errore", null));
	}
	
	@PostMapping
	public ResponseEntity addUser(@RequestBody UserRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(service.Insert(dto));
	}
	
	@DeleteMapping("/uuid/delete/{uuid}")
	public ResponseEntity deleteUserByUuid(@PathVariable String uuid) {
		boolean deleted = service.Delete(uuid);
		if(deleted) {
			return ResponseEntity.ok(ResponseAPI.success("Utente eliminato con successo"));
		}
		
		return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseAPI<>("NOT_FOUND", "Utente non trovato"));
	}
	
	@DeleteMapping("/username/delete/{username}")
	public ResponseEntity deleteUserByUsername(@PathVariable String username) {
		boolean deleted = service.delteByUsername(username);
		if(deleted) {
			return ResponseEntity.ok(ResponseAPI.success("Utente eliminato con successo"));
		}
		
		return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseAPI<>("NOT_FOUND", "Utente non trovato"));
	}
	
	@PutMapping("update/{uuid}")
	public ResponseEntity<ResponseAPI<UserResponseDTO>> updateUser(
			@PathVariable String uuid,
			@RequestBody UserRequestDTO request) {
		UserResponseDTO dto = service.updateUser(uuid, request);
		if( dto != null) {
			return ResponseEntity.ok(ResponseAPI.success(dto));
		}
		
		return ResponseEntity
                .status(HttpStatus.NO_CONTENT)
                .body(new ResponseAPI<>("NOT_FOUND", null));
	}
	
	

}
