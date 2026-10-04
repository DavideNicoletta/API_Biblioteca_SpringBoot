package com.example.demo.dto;

import java.time.LocalDate;

public class BorrowResponseDTO {

    private String uuid;
    private String userUuid;
    private String username;
    private String userEmail;
    private String bookUuid;
    private String bookTitle;
    private String bookIsbn;
    private LocalDate borrowDate;
    private LocalDate returnDate;
    private String status;

    public BorrowResponseDTO() {
    }

    public BorrowResponseDTO(String uuid, String userUuid, String username, String userEmail,
                             String bookUuid, String bookTitle, String bookIsbn,
                             LocalDate borrowDate, LocalDate returnDate, String status) {
        this.uuid = uuid;
        this.userUuid = userUuid;
        this.username = username;
        this.userEmail = userEmail;
        this.bookUuid = bookUuid;
        this.bookTitle = bookTitle;
        this.bookIsbn = bookIsbn;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // --- GETTER E SETTER ---

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getBookUuid() {
        return bookUuid;
    }

    public void setBookUuid(String bookUuid) {
        this.bookUuid = bookUuid;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public String getBookIsbn() {
        return bookIsbn;
    }

    public void setBookIsbn(String bookIsbn) {
        this.bookIsbn = bookIsbn;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}