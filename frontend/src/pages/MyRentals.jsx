import React, { useEffect, useState } from 'react';
import { rentalService } from '../services/rentalService';
import { fineService } from '../services/fineService';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import StatusBadge from '../components/StatusBadge';
import { extractErrorMessage, formatDate } from '../utils/helpers';

export default function MyRentals() {
  const [rentals, setRentals] = useState([]);
  const [finesByRental, setFinesByRental] = useState({});
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [returningId, setReturningId] = useState(null);

  const load = async () => {
    setLoading(true);
    try {
      const [rentalsRes, finesRes] = await Promise.all([
        rentalService.myRentals(),
        fineService.myFines()
      ]);
      setRentals(rentalsRes.data);
      const map = {};
      finesRes.data.forEach((f) => { map[f.rentalId] = f; });
      setFinesByRental(map);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load rentals'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleReturn = async (rentalId) => {
    setReturningId(rentalId);
    setError('');
    try {
      await rentalService.returnBook(rentalId);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not return this book'));
    } finally {
      setReturningId(null);
    }
  };

  return (
    <div className="page">
      <h1>My Rentals</h1>
      <ErrorMessage message={error} />
      {loading ? (
        <Loader />
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Book</th>
              <th>Borrowed</th>
              <th>Due Date</th>
              <th>Returned</th>
              <th>Status</th>
              <th>Fine</th>
              <th>Action</th>
            </tr>
          </thead>
          <tbody>
            {rentals.map((r) => (
              <tr key={r.id}>
                <td>{r.bookTitle}</td>
                <td>{formatDate(r.borrowedAt)}</td>
                <td>{formatDate(r.dueDate)}</td>
                <td>{formatDate(r.returnedAt)}</td>
                <td><StatusBadge status={r.status} /></td>
                <td>
                  {finesByRental[r.id]
                    ? `₹${finesByRental[r.id].amount} (${finesByRental[r.id].status})`
                    : (r.fineAmount != null ? `₹${r.fineAmount}` : '-')}
                </td>
                <td>
                  {(r.status === 'ACTIVE' || r.status === 'OVERDUE') ? (
                    <button className="btn btn-primary" disabled={returningId === r.id} onClick={() => handleReturn(r.id)}>
                      {returningId === r.id ? 'Returning...' : 'Return'}
                    </button>
                  ) : (
                    <span className="muted">-</span>
                  )}
                </td>
              </tr>
            ))}
            {rentals.length === 0 && (
              <tr><td colSpan="7">You haven't borrowed any books yet.</td></tr>
            )}
          </tbody>
        </table>
      )}
    </div>
  );
}
