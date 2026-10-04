package com.example.demo.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.time.LocalDate;


public class BookResponseDTO {

    private String uuid;
    private String title;
    private String author;
    private String isbn;
    private BigDecimal price;
    private Integer availableCopies;
    private Date createdAt;

    public BookResponseDTO() {
    }

    public BookResponseDTO(String uuid, String title, String author, String isbn,
                           BigDecimal price, Integer availableCopies, Date createdAt) {
        this.uuid = uuid;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.price = price;
        this.availableCopies = availableCopies;
        this.createdAt = createdAt;
    }

    // --- GETTER E SETTER ---



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

    public Integer getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(Integer availableCopies) {
        this.availableCopies = availableCopies;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}