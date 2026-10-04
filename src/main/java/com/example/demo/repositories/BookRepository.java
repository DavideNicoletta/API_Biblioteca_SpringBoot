package com.example.demo.repositories;



import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Book;


@Repository
public interface BookRepository extends JpaRepository<Book, Integer>{
	Optional<Book> findByUuid(String uuid);
	
	Optional<Book> findByTitle(String title);
	
	Optional<Book> findByIsbn(String isbn);
}
