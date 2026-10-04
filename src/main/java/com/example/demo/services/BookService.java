package com.example.demo.services;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookResponseDTO;
import com.example.demo.dto.UserResponseDTO;
import com.example.demo.mapper.BookMapper;
import com.example.demo.models.Book;
import com.example.demo.models.User;
import com.example.demo.repositories.BookRepository;

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
		// TODO Auto-generated method stub
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

}
