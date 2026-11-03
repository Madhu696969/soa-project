import React, { useEffect, useState } from 'react';
import { Link } from 'react-router-dom';
import { bookService } from '../services/bookService';
import { rentalService } from '../services/rentalService';
import Card from '../components/Card';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import { extractErrorMessage } from '../utils/helpers';

export default function AdminDashboard() {
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  useEffect(() => {
    async function load() {
      try {
        const [booksRes, activeRes, overdueRes] = await Promise.all([
          bookService.getAll(),
          rentalService.active(),
          rentalService.overdue()
        ]);
        setStats({
          totalBooks: booksRes.data.length,
          activeRentals: activeRes.data.length,
          overdueRentals: overdueRes.data.length
        });
      } catch (err) {
        setError(extractErrorMessage(err, 'Could not load admin stats'));
      } finally {
        setLoading(false);
      }
    }
    load();
  }, []);

  return (
    <div className="page">
      <h1>Admin Dashboard</h1>
      <ErrorMessage message={error} />
      {loading ? (
        <Loader />
      ) : stats ? (
        <div className="stats-grid">
          <Card title="Total Books" value={stats.totalBooks} />
          <Card title="Active Rentals" value={stats.activeRentals} />
          <Card title="Overdue Rentals" value={stats.overdueRentals} />
        </div>
      ) : null}
      <div className="admin-links">
        <Link className="btn btn-primary" to="/admin/books">Manage Books</Link>
        <Link className="btn btn-primary" to="/admin/copies">Manage Book Copies</Link>
      </div>
    </div>
  );
}
