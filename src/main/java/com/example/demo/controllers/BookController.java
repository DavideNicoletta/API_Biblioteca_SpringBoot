package com.example.demo.controllers;


import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.BookResponseDTO;
import com.example.demo.models.Book;
import com.example.demo.response.ResponseAPI;
import com.example.demo.services.BookService;

@RestController
@RequestMapping("/api/book")
public class BookController {
	
	private BookService bookService;
	
	public BookController(BookService bookService) {
		this.bookService = bookService;
	}
	
	@GetMapping
	public ResponseEntity<ResponseAPI<List<BookResponseDTO>>> findAll(){
		List<BookResponseDTO> data = bookService.findAll();
		if(data != null) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<ResponseAPI<Optional<BookResponseDTO>>> findByUuid(@PathVariable String uuid) {
		Optional<BookResponseDTO> data = bookService.findByUuid(uuid);
		if(! data.isEmpty()) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();

	}
	
	@GetMapping("/title/{title}")
	public ResponseEntity<ResponseAPI<Optional<BookResponseDTO>>> findByTitle(@PathVariable String title){
		Optional<BookResponseDTO> data = bookService.findByTitle(title);
		if(!data.isEmpty()) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/isbn/{isbn}")
	public ResponseEntity<ResponseAPI<Optional<BookResponseDTO>>> findByIsbn(@PathVariable String isbn){
		Optional<BookResponseDTO> data = bookService.findByIsbn(isbn);
		if(!data.isEmpty()) {
			return ResponseEntity.ok(ResponseAPI.success(data));
		}
		return ResponseEntity.noContent().build();
	}
}
