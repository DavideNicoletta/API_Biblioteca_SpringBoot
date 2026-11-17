package com.example.demo.dto;

public class BorrowRequestDTO {

    private String bookUuid;

    public BorrowRequestDTO() {
    }

    public BorrowRequestDTO(String bookUuid) {
        this.bookUuid = bookUuid;
    }

    public String getBookUuid() {
        return bookUuid;
    }

    public void setBookUuid(String bookUuid) {
        this.bookUuid = bookUuid;
    }
}