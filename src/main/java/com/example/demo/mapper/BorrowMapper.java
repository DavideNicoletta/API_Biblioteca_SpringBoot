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
				borrow.getBook().getUuid(),
				borrow.getBook().getTitle(),
				borrow.getBook().getIsbn(),
				borrow.getBorrowDate(),
				borrow.getReturnDate(),
				formatStatus(borrow.getStatus())
		);
	}

	private static String formatStatus(String status) {
		return "RETURNED".equalsIgnoreCase(status) ? "Returned" : "Active";
	}
}