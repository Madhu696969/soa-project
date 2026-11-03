import React from 'react';
import { Link } from 'react-router-dom';

export default function BookCard({ book }) {
  return (
    <div className="book-card">
      <h3>{book.title}</h3>
      <p className="book-author">by {book.author}</p>
      <p className="book-meta">{book.category || 'Uncategorized'} · {book.publisher}</p>
      <p className="book-availability">
        {book.availableCopies > 0
          ? `${book.availableCopies} of ${book.totalCopies} copies available`
          : 'No copies available'}
      </p>
      <Link className="btn btn-primary" to={`/books/${book.id}`}>View Details</Link>
    </div>
  );
}
