package com.example.demo.services;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;

import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.BookMapper;
import com.example.demo.models.Book;

import com.example.demo.repositories.BookRepository;

import jakarta.transaction.Transactional;

@Service
public class BookService implements IReadService<BookResponseDTO>, IWriteService<BookResponseDTO>{

	private BookRepository bookRepository;
	
	public BookService(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}



	@Override
	public boolean Update(BookResponseDTO obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Delete(String uuid) {
		Optional<Book> book = bookRepository.findByUuid(uuid);
		if(book.isPresent()) {
			bookRepository.delete(book.get());
			return true;
		}
		return false;
	}

	@Override
	public List<BookResponseDTO> findAll() {
		return bookRepository.findAll().stream().map(BookMapper::toDTO).toList();
	}

	@Override
	public Optional<BookResponseDTO> findByUuid(String uuid) {
		// TODO Auto-generated method stub
		return bookRepository.findByUuid(uuid).map(BookMapper::toDTO);
	}
	
	public Optional<BookResponseDTO> findByTitle(String title){
		return bookRepository.findByTitle(title).map(BookMapper::toDTO);
	}
	
	public Optional<BookResponseDTO>findByIsbn(String isbn){
		return bookRepository.findByIsbn(isbn).map(BookMapper::toDTO);
	}
	
	@Transactional
	public BookResponseDTO Insert(BookRequestDTO obj) {
		
		if (bookRepository.findByIsbn(obj.getIsbn()).isPresent()) {
		    throw new ConflictException("Esiste già un libro con ISBN " + obj.getIsbn());
		}
		
		Book book = new Book();
		book.setTitle(obj.getTitle());
		book.setAuthor(obj.getAuthor());
		book.setIsbn(obj.getIsbn());
		book.setPrice(obj.getPrice());
		book.setAvailable_copies(obj.getAvailableCopies() != null ? obj.getAvailableCopies() : 1);

		
		book.setUuid(UUID.randomUUID().toString());
		book.setCreatedAt(new Date());

		
		Book savedBook = bookRepository.save(book);


		return BookMapper.toDTO(savedBook);
	}
	
	@Transactional
	public BookResponseDTO updateBook(String uuid, BookRequestDTO requestDTO) {

		Optional<Book> bookOptional = bookRepository.findByUuid(uuid);
		if (bookOptional.isEmpty()) {
			throw new NotFoundException("Libro non trovato");
		}

		if (requestDTO.getIsbn() != null
		        && bookRepository.findByIsbn(requestDTO.getIsbn())
		            .filter(b -> !b.getUuid().equals(uuid))
		            .isPresent()) {
		    throw new ConflictException("ISBN già usato da un altro libro");
		}
		
		Book existingBook = bookOptional.get();

		if (requestDTO.getTitle() != null && !requestDTO.getTitle().isBlank()) {
			existingBook.setTitle(requestDTO.getTitle());
		}
		if (requestDTO.getAuthor() != null && !requestDTO.getAuthor().isBlank()) {
			existingBook.setAuthor(requestDTO.getAuthor());
		}
		if (requestDTO.getIsbn() != null && !requestDTO.getIsbn().isBlank()) {
			existingBook.setIsbn(requestDTO.getIsbn());
		}
		if (requestDTO.getPrice() != null) {
			existingBook.setPrice(requestDTO.getPrice());
		}
		if (requestDTO.getAvailableCopies() != null) {
			existingBook.setAvailable_copies(requestDTO.getAvailableCopies());
		}

		Book savedBook = bookRepository.save(existingBook);

		return BookMapper.toDTO(savedBook);
	}
}
