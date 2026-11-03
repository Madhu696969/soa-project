import React, { useEffect, useState } from 'react';
import { useAuth } from '../context/AuthContext';
import { bookService } from '../services/bookService';
import { rentalService } from '../services/rentalService';
import { fineService } from '../services/fineService';
import { notificationService } from '../services/notificationService';
import Card from '../components/Card';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import { extractErrorMessage } from '../utils/helpers';

export default function Dashboard() {
  const { user } = useAuth();
  const [stats, setStats] = useState(null);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    async function loadStats() {
      try {
        const [availableRes, rentalsRes, finesRes, notifRes] = await Promise.all([
          bookService.getAvailable(),
          rentalService.myRentals(),
          fineService.myFines(),
          notificationService.myNotifications()
        ]);

        const rentals = rentalsRes.data;
        const activeCount = rentals.filter((r) => r.status === 'ACTIVE').length;
        const overdueCount = rentals.filter((r) => r.status === 'OVERDUE').length;
        const pendingFine = finesRes.data
          .filter((f) => f.status === 'PENDING')
          .reduce((sum, f) => sum + Number(f.amount), 0);
        const unreadCount = notifRes.data.filter((n) => !n.isRead).length;

        setStats({
          availableBooks: availableRes.data.length,
          activeRentals: activeCount,
          overdueRentals: overdueCount,
          pendingFine,
          unreadNotifications: unreadCount
        });
      } catch (err) {
        setError(extractErrorMessage(err, 'Could not load dashboard data'));
      } finally {
        setLoading(false);
      }
    }
    loadStats();
  }, []);

  return (
    <div className="page">
      <h1>Welcome, {user.name}</h1>
      <p className="page-subtitle">Here's what's happening with your library account.</p>
      <ErrorMessage message={error} />
      {loading ? (
        <Loader />
      ) : stats ? (
        <div className="stats-grid">
          <Card title="Available Books" value={stats.availableBooks} />
          <Card title="My Active Rentals" value={stats.activeRentals} />
          <Card title="Overdue Books" value={stats.overdueRentals} subtitle={stats.overdueRentals > 0 ? 'Please return soon' : 'All clear'} />
          <Card title="Pending Fine" value={`₹${stats.pendingFine}`} />
          <Card title="Unread Notifications" value={stats.unreadNotifications} />
        </div>
      ) : null}
    </div>
  );
}
