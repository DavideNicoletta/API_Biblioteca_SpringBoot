package com.example.demo.services;

public interface IWriteService<T> {

	boolean Update(T obj);
	boolean Delete(String uuid);
}
