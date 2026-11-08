package com.example.demo.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookResponseDTO;
import com.example.demo.dto.BorrowRequestDTO;
import com.example.demo.dto.BorrowResponseDTO;
import com.example.demo.mapper.BorrowMapper;
import com.example.demo.models.Book;
import com.example.demo.models.Borrow;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.BorrowRepository;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowService implements IReadService<BorrowResponseDTO>, IWriteService<BorrowResponseDTO>{

	private final BorrowRepository borrowRepository;
	
	private final UserRepository userRepository;
	
	private final BookRepository bookRepository;
	
	public BorrowService(
			BorrowRepository borrowRepository,
			UserRepository userRepository,
			BookRepository bookRepository) {
		
		this.borrowRepository = borrowRepository;
		this.userRepository = userRepository;
		this.bookRepository = bookRepository;
	}

	@Override
	public boolean Update(BorrowResponseDTO obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Delete(String uuid) {
		Optional<Borrow> borrow = borrowRepository.findByUuid(uuid);
		if(borrow.isPresent()) {
			borrowRepository.delete(borrow.get());
			return true;
		}
		return false;
	}
	
	@Transactional
	@Override
	public List<BorrowResponseDTO> findAll() {
		return borrowRepository.findAll().stream().map(BorrowMapper::toDTO).toList();
	}

	@Transactional
	@Override
	public Optional<BorrowResponseDTO> findByUuid(String uuid) {
		return borrowRepository.findByUuid(uuid).map(BorrowMapper::toDTO);
	}
	
	@Transactional
	public BorrowResponseDTO Insert(BorrowRequestDTO obj) {
		// 1. Cerca l'utente e il libro tramite UUID
		User user = userRepository.findByUuid(obj.getUserUuid())
				.orElseThrow(() -> new RuntimeException("Utente non trovato: " + obj.getUserUuid()));

		com.example.demo.models.Book book = bookRepository.findByUuid(obj.getBookUuid())
				.orElseThrow(() -> new RuntimeException("Libro non trovato: " + obj.getBookUuid()));

		// 2. Controlla disponibilità copie e scala di 1
		if (book.getAvailable_copies() == null || book.getAvailable_copies() <= 0) {
			throw new RuntimeException("Nessuna copia disponibile per il libro: " + book.getTitle());
		}
		book.setAvailable_copies(book.getAvailable_copies() - 1);
		bookRepository.save(book);

		// 3. Istanzia la nuova Entity Borrow
		Borrow borrow = new Borrow();
		borrow.setUuid(UUID.randomUUID().toString());
		borrow.setUser(user);
		borrow.setBook(book);
		borrow.setBorrowDate(obj.getBorrowDate() != null ? obj.getBorrowDate() : LocalDate.now());
		borrow.setStatus("ACTIVE");

		// 4. Salva sul database e converte in DTO
		Borrow savedBorrow = borrowRepository.save(borrow);
		return BorrowMapper.toDTO(savedBorrow);
	}
	
	@Transactional
	public BorrowResponseDTO returnBorrow(String uuid) {
		Optional<Borrow> borrowOptional = borrowRepository.findByUuid(uuid);
		if (borrowOptional.isEmpty()) {
			return null;
		}

		Borrow existingBorrow = borrowOptional.get();

		// Se il prestito era ancora attivo, ri-aggiunge la copia disponibile al libro
		if ("ACTIVE".equalsIgnoreCase(existingBorrow.getStatus())) {
			Book book = existingBorrow.getBook();
			book.setAvailable_copies(book.getAvailable_copies() + 1);
			bookRepository.save(book);
		}

		existingBorrow.setReturnDate(LocalDate.now());
		existingBorrow.setStatus("RETURNED");

		Borrow savedBorrow = borrowRepository.save(existingBorrow);
		return BorrowMapper.toDTO(savedBorrow);
	}

	
}
