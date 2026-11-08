package com.example.demo.services;


import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.mapper.BookMapper;
import com.example.demo.models.Book;
import com.example.demo.models.User;
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

	@Transactional
	public boolean deleteByIsbn(String isbn) {
		Optional<Book> book = bookRepository.findByIsbn(isbn);
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
		// 1. Istanzia la nuova Entity Book
		Book book = new Book();
		book.setTitle(obj.getTitle());
		book.setAuthor(obj.getAuthor());
		book.setIsbn(obj.getIsbn());
		book.setPrice(obj.getPrice());
		book.setAvailable_copies(obj.getAvailableCopies() != null ? obj.getAvailableCopies() : 1);

		// 2. Genera UUID univoco lato Java e data di creazione
		book.setUuid(UUID.randomUUID().toString());
		book.setCreatedAt(new Date());

		// 3. Esegue la INSERT sul database tramite save()
		Book savedBook = bookRepository.save(book);

		// 4. Converte il libro appena salvato in BookResponseDTO e lo restituisce
		return BookMapper.toDTO(savedBook);
	}
	
	@Transactional
	public BookResponseDTO updateBook(String uuid, BookRequestDTO requestDTO) {
		// 1. Cerca il libro esistente tramite UUID
		Optional<Book> bookOptional = bookRepository.findByUuid(uuid);
		if (bookOptional.isEmpty()) {
			return null; // Libro non trovato
		}

		Book existingBook = bookOptional.get();

		// 2. Aggiorna i campi consentiti
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

		// 3. Salva le modifiche (esegue UPDATE sul database)
		Book savedBook = bookRepository.save(existingBook);

		// 4. Converte in ResponseDTO
		return BookMapper.toDTO(savedBook);
	}
}
