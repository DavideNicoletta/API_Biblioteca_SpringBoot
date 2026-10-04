package com.example.demo.services;

import java.util.List;
import java.util.Optional;

public interface IReadService<T> {
	
	List<T> findAll();
	
	Optional<T> findByUuid(String uuid);
	
}
