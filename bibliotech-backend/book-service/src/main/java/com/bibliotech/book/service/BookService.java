package com.bibliotech.book.service;

import com.bibliotech.book.dto.*;
import com.bibliotech.book.entity.*;
import com.bibliotech.book.exception.DuplicateResourceException;
import com.bibliotech.book.exception.ResourceNotFoundException;
import com.bibliotech.book.repository.BookCopyRepository;
import com.bibliotech.book.repository.BookRepository;
import com.bibliotech.book.repository.BranchRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final BookCopyRepository bookCopyRepository;
    private final BranchRepository branchRepository;

    public BookService(BookRepository bookRepository, BookCopyRepository bookCopyRepository, BranchRepository branchRepository) {
        this.bookRepository = bookRepository;
        this.bookCopyRepository = bookCopyRepository;
        this.branchRepository = branchRepository;
    }

    public List<BookResponse> getAllBooks() {
        return bookRepository.findAll().stream().map(this::toResponse).collect(Collectors.toList());
    }

    public BookResponse getBookById(Long id) {
        return toResponse(findBookOrThrow(id));
    }

    public List<BookResponse> search(String keyword) {
        return bookRepository.search(keyword).stream().map(this::toResponse).collect(Collectors.toList());
    }

    public List<BookResponse> getAvailableBooks() {
        return bookRepository.findAll().stream()
                .filter(b -> bookCopyRepository.countByBookIdAndStatus(b.getId(), CopyStatus.AVAILABLE) > 0)
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public BookResponse createBook(BookRequest request) {
        bookRepository.findByIsbn(request.getIsbn()).ifPresent(b -> {
            throw new DuplicateResourceException("A book with this ISBN already exists");
        });

        Book book = new Book();
        book.setIsbn(request.getIsbn());
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setPublisher(request.getPublisher());
        book.setDescription(request.getDescription());

        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public BookResponse updateBook(Long id, BookRequest request) {
        Book book = findBookOrThrow(id);
        book.setTitle(request.getTitle());
        book.setAuthor(request.getAuthor());
        book.setCategory(request.getCategory());
        book.setPublisher(request.getPublisher());
        book.setDescription(request.getDescription());
        book.setIsbn(request.getIsbn());
        return toResponse(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long id) {
        Book book = findBookOrThrow(id);
        bookRepository.delete(book);
    }

    @Transactional
    public BookCopyResponse addCopy(Long bookId, BookCopyRequest request) {
        Book book = findBookOrThrow(bookId);
        Branch branch = branchRepository.findById(request.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found"));

        BookCopy copy = new BookCopy();
        copy.setBook(book);
        copy.setBranch(branch);
        copy.setCopyCode(request.getCopyCode());
        copy.setStatus(CopyStatus.AVAILABLE);

        return toCopyResponse(bookCopyRepository.save(copy));
    }

    public List<BookCopyResponse> getCopies(Long bookId) {
        findBookOrThrow(bookId);
        return bookCopyRepository.findByBookId(bookId).stream().map(this::toCopyResponse).collect(Collectors.toList());
    }

    @Transactional
    public BookCopyResponse updateCopyStatus(Long copyId, CopyStatus status) {
        BookCopy copy = bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("Book copy not found"));
        copy.setStatus(status);
        return toCopyResponse(bookCopyRepository.save(copy));
    }

    public BookCopyResponse getCopy(Long copyId) {
        return toCopyResponse(bookCopyRepository.findById(copyId)
                .orElseThrow(() -> new ResourceNotFoundException("Book copy not found")));
    }

    private Book findBookOrThrow(Long id) {
        return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found"));
    }

    private BookResponse toResponse(Book book) {
        long available = bookCopyRepository.countByBookIdAndStatus(book.getId(), CopyStatus.AVAILABLE);
        long total = bookCopyRepository.findByBookId(book.getId()).size();
        return new BookResponse(book.getId(), book.getIsbn(), book.getTitle(), book.getAuthor(),
                book.getCategory(), book.getPublisher(), book.getDescription(), book.getCreatedAt(),
                available, total);
    }

    private BookCopyResponse toCopyResponse(BookCopy copy) {
        return new BookCopyResponse(copy.getId(), copy.getBook().getId(), copy.getBook().getTitle(),
                copy.getBranch().getId(), copy.getBranch().getName(), copy.getCopyCode(), copy.getStatus());
    }
}
