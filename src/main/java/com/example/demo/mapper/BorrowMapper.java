package com.example.demo.mapper;

import com.example.demo.dto.BorrowResponseDTO;
import com.example.demo.models.Borrow;

public class BorrowMapper {

	private BorrowMapper() {
		
	}
	
	public static BorrowResponseDTO toDTO(Borrow borrow) {
	    return new BorrowResponseDTO(
	        borrow.getUuid(),
	        borrow.getUser().getUuid(),
	        borrow.getUser().getUsername(),
	        borrow.getUser().getEmail(),
	        borrow.getBook().getUuid(),
	        borrow.getBook().getTitle(),
	        borrow.getBook().getIsbn(),
	        borrow.getBorrowDate(),
	        borrow.getReturnDate(),
	        borrow.getStatus()
	    );
	}
	
}
