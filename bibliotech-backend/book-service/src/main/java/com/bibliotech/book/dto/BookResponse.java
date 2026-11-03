package com.bibliotech.book.dto;

import java.time.LocalDateTime;

public class BookResponse {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String category;
    private String publisher;
    private String description;
    private LocalDateTime createdAt;
    private long availableCopies;
    private long totalCopies;

    public BookResponse() {
    }

    public BookResponse(Long id, String isbn, String title, String author, String category, String publisher, String description, LocalDateTime createdAt, long availableCopies, long totalCopies) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.category = category;
        this.publisher = publisher;
        this.description = description;
        this.createdAt = createdAt;
        this.availableCopies = availableCopies;
        this.totalCopies = totalCopies;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
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

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getAvailableCopies() {
        return availableCopies;
    }

    public void setAvailableCopies(long availableCopies) {
        this.availableCopies = availableCopies;
    }

    public long getTotalCopies() {
        return totalCopies;
    }

    public void setTotalCopies(long totalCopies) {
        this.totalCopies = totalCopies;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookResponse that = (BookResponse) o;
        return java.util.Objects.equals(id, that.id) &&
                java.util.Objects.equals(isbn, that.isbn) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(author, that.author) &&
                java.util.Objects.equals(category, that.category) &&
                java.util.Objects.equals(publisher, that.publisher) &&
                java.util.Objects.equals(description, that.description) &&
                java.util.Objects.equals(createdAt, that.createdAt) &&
                availableCopies == that.availableCopies &&
                totalCopies == that.totalCopies;
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(id, isbn, title, author, category, publisher, description, createdAt, availableCopies, totalCopies);
    }

    @Override
    public String toString() {
        return "BookResponse{" +
                "id=" + id + ", " + "isbn=" + isbn + ", " + "title=" + title + ", " + "author=" + author + ", " + "category=" + category + ", " + "publisher=" + publisher + ", " + "description=" + description + ", " + "createdAt=" + createdAt + ", " + "availableCopies=" + availableCopies + ", " + "totalCopies=" + totalCopies +
                '}';
    }
}
