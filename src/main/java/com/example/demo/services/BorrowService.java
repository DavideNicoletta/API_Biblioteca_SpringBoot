package com.example.demo.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BorrowRequestDTO;
import com.example.demo.dto.BorrowResponseDTO;
import com.example.demo.exceptions.ConflictException;
import com.example.demo.exceptions.NotFoundException;
import com.example.demo.mapper.BorrowMapper;
import com.example.demo.models.Book;
import com.example.demo.models.Borrow;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.BorrowRepository;
import com.example.demo.repositories.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowService implements IReadService<BorrowResponseDTO>, IWriteService<BorrowResponseDTO> {

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
		return false;
	}

	@Override
	public boolean Delete(String uuid) {
		Optional<Borrow> borrow = borrowRepository.findByUuid(uuid);
		if (borrow.isPresent()) {
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
	public List<BorrowResponseDTO> findAll(String username, boolean isAdmin) {
		List<Borrow> borrows = isAdmin
				? borrowRepository.findAll()
				: borrowRepository.findByUserUsername(username);
		return borrows.stream().map(BorrowMapper::toDTO).toList();
	}

	@Transactional
	@Override
	public Optional<BorrowResponseDTO> findByUuid(String uuid) {
		return borrowRepository.findByUuid(uuid).map(BorrowMapper::toDTO);
	}

	@Transactional
	public Optional<BorrowResponseDTO> findByUuid(String uuid, String username, boolean isAdmin) {
		return borrowRepository.findByUuid(uuid)
				.filter(b -> isAdmin || b.getUser().getUsername().equals(username))
				.map(BorrowMapper::toDTO);
	}

	@Transactional
	public BorrowResponseDTO Insert(String username, BorrowRequestDTO obj) {
		User user = userRepository.findByUsername(username)
				.filter(u -> Boolean.TRUE.equals(u.getEnabled()))
				.orElseThrow(() -> new NotFoundException("Utente non trovato"));

		Book book = bookRepository.findByUuid(obj.getBookUuid())
				.orElseThrow(() -> new NotFoundException("Libro non trovato"));

		if (book.getAvailable_copies() == null || book.getAvailable_copies() <= 0) {
			throw new ConflictException("Nessuna copia disponibile per il libro: " + book.getTitle());
		}
		book.setAvailable_copies(book.getAvailable_copies() - 1);
		bookRepository.save(book);

		Borrow borrow = new Borrow();
		borrow.setUuid(UUID.randomUUID().toString());
		borrow.setUser(user);
		borrow.setBook(book);
		borrow.setBorrowDate(LocalDate.now());
		borrow.setStatus("Active");

		return BorrowMapper.toDTO(borrowRepository.save(borrow));
	}

	@Transactional
	public BorrowResponseDTO returnBorrow(String uuid, String username, boolean isAdmin) {
		Borrow borrow = borrowRepository.findByUuid(uuid)
				.filter(b -> isAdmin || b.getUser().getUsername().equals(username))
				.orElseThrow(() -> new NotFoundException("Prestito non trovato"));

		if ("Returned".equalsIgnoreCase(borrow.getStatus())) {
			throw new ConflictException("Prestito già restituito");
		}

		Book book = borrow.getBook();
		book.setAvailable_copies(book.getAvailable_copies() + 1);
		bookRepository.save(book);

		borrow.setReturnDate(LocalDate.now());
		borrow.setStatus("Returned");

		return BorrowMapper.toDTO(borrowRepository.save(borrow));
	}
}