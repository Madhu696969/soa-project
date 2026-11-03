package com.bibliotech.book.dto;

import jakarta.validation.constraints.NotBlank;

public class BookRequest {
    @NotBlank(message = "ISBN is required")
    private String isbn;

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Author is required")
    private String author;

    private String category;
    private String publisher;
    private String description;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BookRequest that = (BookRequest) o;
        return java.util.Objects.equals(isbn, that.isbn) &&
                java.util.Objects.equals(title, that.title) &&
                java.util.Objects.equals(author, that.author) &&
                java.util.Objects.equals(category, that.category) &&
                java.util.Objects.equals(publisher, that.publisher) &&
                java.util.Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(isbn, title, author, category, publisher, description);
    }

    @Override
    public String toString() {
        return "BookRequest{" +
                "isbn=" + isbn + ", " + "title=" + title + ", " + "author=" + author + ", " + "category=" + category + ", " + "publisher=" + publisher + ", " + "description=" + description +
                '}';
    }
}
