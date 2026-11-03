package com.bibliotech.book.repository;

import com.bibliotech.book.entity.BookCopy;
import com.bibliotech.book.entity.CopyStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    List<BookCopy> findByBookId(Long bookId);
    List<BookCopy> findByStatus(CopyStatus status);
    long countByBookIdAndStatus(Long bookId, CopyStatus status);
}
