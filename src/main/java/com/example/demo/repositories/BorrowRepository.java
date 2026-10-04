package com.example.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.models.Borrow;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Integer> {
	Optional<Borrow> findByUuid(String uuid);
}
