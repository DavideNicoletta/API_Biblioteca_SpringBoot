package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
}
