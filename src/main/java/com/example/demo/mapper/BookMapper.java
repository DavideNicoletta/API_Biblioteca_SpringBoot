package com.example.demo.mapper;



import com.example.demo.dto.BookRequestDTO;
import com.example.demo.dto.BookResponseDTO;
import com.example.demo.models.Book;

public class BookMapper {

	public BookMapper() {
		
	}
	
	public static BookResponseDTO toDTO(Book book) {
	    return new BookResponseDTO(
	        book.getUuid(),
	        book.getTitle(),
	        book.getAuthor(),
	        book.getIsbn(),
	        book.getPrice(),
	        book.getAvailable_copies(),
	        book.getCreatedAt()
	    );
	}

	// Da RequestDTO a Entity (per la creazione)
	public static Book toEntity(BookRequestDTO dto) {
	    Book book = new Book();
	    book.setTitle(dto.getTitle());
	    book.setAuthor(dto.getAuthor());
	    book.setIsbn(dto.getIsbn());
	    book.setPrice(dto.getPrice());
	    book.setAvailable_copies(dto.getAvailableCopies() != null ? dto.getAvailableCopies() : 1);
	    return book;
	}
}
