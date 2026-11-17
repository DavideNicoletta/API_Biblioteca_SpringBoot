package com.example.demo.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BorrowRequestDTO;
import com.example.demo.dto.BorrowResponseDTO;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.response.ResponseAPI;
import com.example.demo.services.BorrowService;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

	private BorrowService borrowService;

	public BorrowController(BorrowService borrowService) {
		this.borrowService = borrowService;
	}

	private boolean isAdmin(Authentication authentication) {
		return authentication.getAuthorities().stream()
				.anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
	}

	@GetMapping
	public ResponseEntity<ResponseAPI<List<BorrowResponseDTO>>> findAll(Authentication authentication) {
		List<BorrowResponseDTO> data =
				borrowService.findAll(authentication.getName(), isAdmin(authentication));
		return ResponseEntity.ok(ResponseAPI.success(data));
	}

	@GetMapping("/{uuid}")
	public ResponseEntity<ResponseAPI<BorrowResponseDTO>> findByUuid(
			@PathVariable String uuid, Authentication authentication) {
		BorrowResponseDTO data = borrowService
				.findByUuid(uuid, authentication.getName(), isAdmin(authentication))
				.orElseThrow(() -> new NotFoundException("Prestito non trovato"));
		return ResponseEntity.ok(ResponseAPI.success(data));
	}

	@PostMapping
	public ResponseEntity<ResponseAPI<BorrowResponseDTO>> addBorrow(
			@RequestBody BorrowRequestDTO dto, Authentication authentication) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseAPI.success(borrowService.Insert(authentication.getName(), dto)));
	}

	@PutMapping("/{uuid}/return")
	public ResponseEntity<ResponseAPI<BorrowResponseDTO>> returnBorrow(
			@PathVariable String uuid, Authentication authentication) {
		BorrowResponseDTO dto =
				borrowService.returnBorrow(uuid, authentication.getName(), isAdmin(authentication));
		return ResponseEntity.ok(ResponseAPI.success(dto));
	}

	@DeleteMapping("/{uuid}")
	public ResponseEntity<Void> deleteBorrowByUuid(@PathVariable String uuid) {
		if (!borrowService.Delete(uuid)) {
			throw new NotFoundException("Prestito non trovato");
		}
		return ResponseEntity.noContent().build();
	}
}