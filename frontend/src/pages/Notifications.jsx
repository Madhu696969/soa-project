import React, { useEffect, useState } from 'react';
import { notificationService } from '../services/notificationService';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import { extractErrorMessage } from '../utils/helpers';

export default function Notifications() {
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const load = async () => {
    setLoading(true);
    try {
      const res = await notificationService.myNotifications();
      setNotifications(res.data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load notifications'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const handleMarkRead = async (id) => {
    try {
      await notificationService.markRead(id);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not update notification'));
    }
  };

  return (
    <div className="page">
      <h1>Notifications</h1>
      <ErrorMessage message={error} />
      {loading ? (
        <Loader />
      ) : (
        <div className="notification-list">
          {notifications.map((n) => (
            <div key={n.id} className={`notification-item ${n.isRead ? '' : 'unread'}`}>
              <div className="notification-type">{n.type}</div>
              <div className="notification-message">{n.message}</div>
              <div className="notification-footer">
                <span className="notification-time">{new Date(n.createdAt).toLocaleString()}</span>
                {!n.isRead && (
                  <button className="btn btn-outline btn-sm" onClick={() => handleMarkRead(n.id)}>
                    Mark as read
                  </button>
                )}
              </div>
            </div>
          ))}
          {notifications.length === 0 && <p>No notifications yet.</p>}
        </div>
      )}
    </div>
  );
}
