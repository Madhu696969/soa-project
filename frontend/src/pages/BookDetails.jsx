import React, { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { bookService } from '../services/bookService';
import { rentalService } from '../services/rentalService';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import StatusBadge from '../components/StatusBadge';
import { extractErrorMessage } from '../utils/helpers';

export default function BookDetails() {
  const { id } = useParams();
  const navigate = useNavigate();
  const [book, setBook] = useState(null);
  const [copies, setCopies] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [message, setMessage] = useState('');
  const [borrowing, setBorrowing] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const [bookRes, copiesRes] = await Promise.all([
        bookService.getById(id),
        bookService.getCopies(id)
      ]);
      setBook(bookRes.data);
      setCopies(copiesRes.data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load book'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [id]);

  const handleBorrow = async (copyId) => {
    setBorrowing(true);
    setError('');
    setMessage('');
    try {
      await rentalService.borrow(copyId);
      setMessage('Book borrowed successfully! Due in 14 days.');
      await load();
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not borrow this book'));
    } finally {
      setBorrowing(false);
    }
  };

  if (loading) return <div className="page"><Loader /></div>;
  if (!book) return <div className="page"><ErrorMessage message={error || 'Book not found'} /></div>;

  return (
    <div className="page">
      <button className="btn btn-outline" onClick={() => navigate(-1)}>&larr; Back</button>
      <div className="book-details-card">
        <h1>{book.title}</h1>
        <p className="book-author">by {book.author}</p>
        <div className="detail-grid">
          <div><strong>ISBN:</strong> {book.isbn}</div>
          <div><strong>Category:</strong> {book.category || '-'}</div>
          <div><strong>Publisher:</strong> {book.publisher || '-'}</div>
          <div><strong>Availability:</strong> {book.availableCopies} of {book.totalCopies} copies</div>
        </div>
        {book.description && <p className="book-description">{book.description}</p>}
      </div>

      <ErrorMessage message={error} />
      {message && <div className="success-banner">{message}</div>}

      <h2>Physical Copies</h2>
      <table className="data-table">
        <thead>
          <tr>
            <th>Copy Code</th>
            <th>Branch</th>
            <th>Status</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {copies.map((copy) => (
            <tr key={copy.id}>
              <td>{copy.copyCode}</td>
              <td>{copy.branchName}</td>
              <td><StatusBadge status={copy.status} /></td>
              <td>
                {copy.status === 'AVAILABLE' ? (
                  <button className="btn btn-primary" disabled={borrowing} onClick={() => handleBorrow(copy.id)}>
                    Borrow
                  </button>
                ) : (
                  <span className="muted">Unavailable</span>
                )}
              </td>
            </tr>
          ))}
          {copies.length === 0 && (
            <tr><td colSpan="4">No physical copies registered yet.</td></tr>
          )}
        </tbody>
      </table>
    </div>
  );
}
