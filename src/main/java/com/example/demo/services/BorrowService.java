package com.example.demo.services;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.dto.BookResponseDTO;
import com.example.demo.dto.BorrowResponseDTO;
import com.example.demo.mapper.BorrowMapper;
import com.example.demo.models.Borrow;
import com.example.demo.repositories.BookRepository;
import com.example.demo.repositories.BorrowRepository;

import jakarta.transaction.Transactional;

@Service
public class BorrowService implements IReadService<BorrowResponseDTO>, IWriteService<BorrowResponseDTO>{

	private BorrowRepository borrowRepository;
	
	public BorrowService(BorrowRepository borrowRepository) {
		this.borrowRepository = borrowRepository;
	}

	@Override
	public boolean Update(BorrowResponseDTO obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Delete(String uuid) {
		// TODO Auto-generated method stub
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

	
}
