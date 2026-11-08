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
	
	@PostMapping
	public ResponseEntity addBook(@RequestBody BookRequestDTO dto) {
		return ResponseEntity.status(HttpStatus.CREATED).body(bookService.Insert(dto));
	}
	
	@DeleteMapping("/uuid/delete/{uuid}")
	public ResponseEntity deleteBookByUuid(@PathVariable String uuid) {
		boolean deleted = bookService.Delete(uuid);
		if(deleted) {
			return ResponseEntity.ok(ResponseAPI.success("Libro eliminato con successo"));
		}
		
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body(new ResponseAPI<>("NOT_FOUND", "Libro non trovato"));
	}
	
	@DeleteMapping("/isbn/delete/{isbn}")
	public ResponseEntity deleteBookByIsbn(@PathVariable String isbn) {
		boolean deleted = bookService.deleteByIsbn(isbn);
		if(deleted) {
			return ResponseEntity.ok(ResponseAPI.success("Libro eliminato con successo"));
		}
		
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body(new ResponseAPI<>("NOT_FOUND", "Libro non trovato"));
	}
	
	@PutMapping("update/{uuid}")
	public ResponseEntity<ResponseAPI<BookResponseDTO>> updateBook(
			@PathVariable String uuid,
			@RequestBody BookRequestDTO request) {
		BookResponseDTO dto = bookService.updateBook(uuid, request);
		if(dto != null) {
			return ResponseEntity.ok(ResponseAPI.success(dto));
		}
		
		return ResponseEntity
				.status(HttpStatus.NO_CONTENT)
				.body(new ResponseAPI<>("NOT_FOUND", null));
	}
	
	
}
