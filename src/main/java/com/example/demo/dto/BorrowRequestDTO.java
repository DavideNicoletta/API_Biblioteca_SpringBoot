package com.example.demo.dto;

import java.time.LocalDate;

public class BorrowRequestDTO {

    private String userUuid;
    private String bookUuid;
    private LocalDate borrowDate; // Opzionale: se nullo, il backend imposta LocalDate.now()

    public BorrowRequestDTO() {
    }

    public BorrowRequestDTO(String userUuid, String bookUuid, LocalDate borrowDate) {
        this.userUuid = userUuid;
        this.bookUuid = bookUuid;
        this.borrowDate = borrowDate;
    }

    public String getUserUuid() {
        return userUuid;
    }

    public void setUserUuid(String userUuid) {
        this.userUuid = userUuid;
    }

    public String getBookUuid() {
        return bookUuid;
    }

    public void setBookUuid(String bookUuid) {
        this.bookUuid = bookUuid;
    }

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }
}