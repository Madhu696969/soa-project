import React, { useEffect, useState } from 'react';
import { bookService } from '../services/bookService';
import BookCard from '../components/BookCard';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import { extractErrorMessage } from '../utils/helpers';

export default function Books() {
  const [books, setBooks] = useState([]);
  const [query, setQuery] = useState('');
  const [category, setCategory] = useState('ALL');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadBooks = async (q) => {
    setLoading(true);
    try {
      const res = await bookService.getAll(q);
      setBooks(res.data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load books'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadBooks();
  }, []);

  const handleSearch = (e) => {
    e.preventDefault();
    loadBooks(query);
  };

  const categories = ['ALL', ...new Set(books.map((b) => b.category).filter(Boolean))];
  const visibleBooks = category === 'ALL' ? books : books.filter((b) => b.category === category);

  return (
    <div className="page">
      <h1>Browse Books</h1>
      <form className="search-bar" onSubmit={handleSearch}>
        <input
          placeholder="Search by title, author, category or ISBN"
          value={query}
          onChange={(e) => setQuery(e.target.value)}
        />
        <button className="btn btn-primary" type="submit">Search</button>
        <select value={category} onChange={(e) => setCategory(e.target.value)}>
          {categories.map((c) => (
            <option key={c} value={c}>{c}</option>
          ))}
        </select>
      </form>
      <ErrorMessage message={error} />
      {loading ? (
        <Loader />
      ) : (
        <div className="book-grid">
          {visibleBooks.map((book) => (
            <BookCard key={book.id} book={book} />
          ))}
          {visibleBooks.length === 0 && <p>No books found.</p>}
        </div>
      )}
    </div>
  );
}
