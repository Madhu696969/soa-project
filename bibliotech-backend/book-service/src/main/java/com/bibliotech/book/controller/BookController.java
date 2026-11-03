package com.bibliotech.book.controller;

import com.bibliotech.book.dto.*;
import com.bibliotech.book.entity.CopyStatus;
import com.bibliotech.book.service.BookService;
import com.bibliotech.book.service.RoleGuard;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    private final BookService bookService;
    private final RoleGuard roleGuard;

    public BookController(BookService bookService, RoleGuard roleGuard) {
        this.bookService = bookService;
        this.roleGuard = roleGuard;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks(@RequestParam(required = false) String q) {
        if (q != null && !q.isBlank()) {
            return ResponseEntity.ok(bookService.search(q));
        }
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/available")
    public ResponseEntity<List<BookResponse>> getAvailableBooks() {
        return ResponseEntity.ok(bookService.getAvailableBooks());
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBook(@PathVariable Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestHeader("X-User-Role") String role,
                                                     @Valid @RequestBody BookRequest request) {
        roleGuard.require(role, List.of("ADMIN", "LIBRARIAN"));
        return ResponseEntity.ok(bookService.createBook(request));
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@RequestHeader("X-User-Role") String role,
                                                     @PathVariable Long id,
                                                     @Valid @RequestBody BookRequest request) {
        roleGuard.require(role, List.of("ADMIN", "LIBRARIAN"));
        return ResponseEntity.ok(bookService.updateBook(id, request));
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@RequestHeader("X-User-Role") String role, @PathVariable Long id) {
        roleGuard.require(role, List.of("ADMIN", "LIBRARIAN"));
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }
    @PostMapping("/{bookId}/copies")
    public ResponseEntity<BookCopyResponse> addCopy(@RequestHeader("X-User-Role") String role,
                                                       @PathVariable Long bookId,
                                                       @Valid @RequestBody BookCopyRequest request) {
        roleGuard.require(role, List.of("ADMIN", "LIBRARIAN"));
        return ResponseEntity.ok(bookService.addCopy(bookId, request));
    }
    @GetMapping("/{bookId}/copies")
    public ResponseEntity<List<BookCopyResponse>> getCopies(@PathVariable Long bookId) {
        return ResponseEntity.ok(bookService.getCopies(bookId));
    }
    @GetMapping("/copies/{copyId}")
    public ResponseEntity<BookCopyResponse> getCopy(@PathVariable Long copyId) {
        return ResponseEntity.ok(bookService.getCopy(copyId));
    }
    @PutMapping("/copies/{copyId}/status")
    public ResponseEntity<BookCopyResponse> updateCopyStatus(@RequestHeader("X-User-Role") String role,
                                                                @PathVariable Long copyId,
                                                                @Valid @RequestBody StatusUpdateRequest request) {
        roleGuard.require(role, List.of("ADMIN", "LIBRARIAN", "SYSTEM"));
        return ResponseEntity.ok(bookService.updateCopyStatus(copyId, request.getStatus()));
    }
}
