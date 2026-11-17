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

import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.exceptions.NotFoundException;
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
	public ResponseEntity<ResponseAPI<List<BookResponseDTO>>> findAll() {
	    return ResponseEntity.ok(ResponseAPI.success(bookService.findAll()));
	}
	
	@GetMapping("/uuid/{uuid}")
	public ResponseEntity<ResponseAPI<BookResponseDTO>> findByUuid(@PathVariable String uuid) {
	    BookResponseDTO data = bookService.findByUuid(uuid)
	            .orElseThrow(() -> new NotFoundException("Libro non trovato"));
	    return ResponseEntity.ok(ResponseAPI.success(data));
	}

	
	@GetMapping("/title/{title}")
	public ResponseEntity<ResponseAPI<BookResponseDTO>> findByTitle(@PathVariable String title){
		BookResponseDTO data = bookService.findByTitle(title)
				.orElseThrow(() -> new NotFoundException("Libro non trovato"));
		return ResponseEntity.ok(ResponseAPI.success(data));
	}
	
	@GetMapping("/isbn/{isbn}")
	public ResponseEntity<ResponseAPI<BookResponseDTO>> findByIsbn(@PathVariable String isbn){
		BookResponseDTO data = bookService.findByIsbn(isbn)
				.orElseThrow(() -> new NotFoundException("Libro non trovato"));
		return ResponseEntity.ok(ResponseAPI.success(data));
	}
	
	@PostMapping
	public ResponseEntity<ResponseAPI<BookResponseDTO>> addBook(@RequestBody BookRequestDTO dto) {
	    return ResponseEntity.status(HttpStatus.CREATED)
	            .body(ResponseAPI.success(bookService.Insert(dto)));
	}
	
	@DeleteMapping("/{uuid}")
	public ResponseEntity<Void> deleteBookByUuid(@PathVariable String uuid) {
	    if (!bookService.Delete(uuid)) {
	        throw new NotFoundException("Libro non trovato");
	    }
	    return ResponseEntity.noContent().build();
	}
	
	
	@PutMapping("/{uuid}")
	public ResponseEntity<ResponseAPI<BookResponseDTO>> updateBook(
	        @PathVariable String uuid, @RequestBody BookRequestDTO request) {
	    return ResponseEntity.ok(ResponseAPI.success(bookService.updateBook(uuid, request)));
	}
	
	
}
