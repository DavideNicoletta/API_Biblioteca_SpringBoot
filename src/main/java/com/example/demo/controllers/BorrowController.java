package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

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

import com.example.demo.dto.BorrowRequestDTO;
import com.example.demo.dto.BorrowResponseDTO;
import com.example.demo.response.ResponseAPI;
import com.example.demo.services.BorrowService;

@RestController
@RequestMapping("/api/borrow")
public class BorrowController {

	private BorrowService borrowService;
	
	public BorrowController(BorrowService borrowService) {
		this.borrowService = borrowService;
	}
	
	
	@GetMapping
	public ResponseEntity<ResponseAPI<List<BorrowResponseDTO>>> findAll(){
		List<BorrowResponseDTO> data = borrowService.findAll();
		if(data != null) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
		
	}
	
	@GetMapping("/{uuid}")
	public ResponseEntity<ResponseAPI<Optional<BorrowResponseDTO>>> findByUuid(@PathVariable String uuid){
		Optional<BorrowResponseDTO> data = borrowService.findByUuid(uuid);
		if(!data.isEmpty()) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
	}
	
	@PostMapping
	public ResponseEntity addBorrow(@RequestBody BorrowRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(borrowService.Insert(dto));
	}
	
	@PutMapping("return/{uuid}")
	public ResponseEntity<ResponseAPI<BorrowResponseDTO>> returnBorrow(@PathVariable String uuid) {
		BorrowResponseDTO dto = borrowService.returnBorrow(uuid);
		if(dto != null) {
			return ResponseEntity.ok(ResponseAPI.success(dto));
		}
		
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body(new ResponseAPI<>("NOT_FOUND", null));
	}
	
	@DeleteMapping("/uuid/delete/{uuid}")
	public ResponseEntity deleteBorrowByUuid(@PathVariable String uuid) {
		boolean deleted = borrowService.Delete(uuid);
		if(deleted) {
			return ResponseEntity.ok(ResponseAPI.success("Prestito eliminato con successo"));
		}
		
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body(new ResponseAPI<>("NOT_FOUND", "Prestito non trovato"));
	}
	
}
