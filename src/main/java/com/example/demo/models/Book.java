package com.example.demo.models;



import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="Books")
public class Book {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private String uuid;
	@Column
	private String title;
	@Column
	private String author;
	@Column
	private String isbn;
	@Column
	private BigDecimal price;
	@Column
	private Integer available_copies;
	@Column
	private Date createdAt;
	
	
	
	
	public Book() {

	}


	public Book(String uuid, String title, String author, String isbn, BigDecimal price, Integer available_copies,
			Date createdAt) {
		super();
		this.uuid = uuid;
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.price = price;
		this.available_copies = available_copies;
		this.createdAt = createdAt;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getUuid() {
		return uuid;
	}


	public void setUuid(String uuid) {
		this.uuid = uuid;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getAuthor() {
		return author;
	}


	public void setAuthor(String author) {
		this.author = author;
	}


	public String getIsbn() {
		return isbn;
	}


	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}


	public BigDecimal getPrice() {
		return price;
	}


	public void setPrice(BigDecimal price) {
		this.price = price;
	}


	public Integer getAvailable_copies() {
		return available_copies;
	}


	public void setAvailable_copies(Integer available_copies) {
		this.available_copies = available_copies;
	}


	public Date getCreatedAt() {
		return createdAt;
	}


	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	
	
	
	
}
