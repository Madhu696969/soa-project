import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

export default function Navbar() {
  const { user, logout, isAuthenticated } = useAuth();
  const navigate = useNavigate();

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  return (
    <nav className="navbar">
      <div className="navbar-brand">
        <Link to="/dashboard">📚 Bibliotech</Link>
      </div>
      {isAuthenticated && (
        <div className="navbar-links">
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/books">Books</Link>
          <Link to="/rentals">My Rentals</Link>
          <Link to="/fines">Fines</Link>
          <Link to="/notifications">Notifications</Link>
          {(user.role === 'ADMIN' || user.role === 'LIBRARIAN') && (
            <Link to="/admin">Admin</Link>
          )}
        </div>
      )}
      {isAuthenticated && (
        <div className="navbar-user">
          <span className="user-chip">{user.name} · {user.role}</span>
          <button className="btn btn-outline" onClick={handleLogout}>Logout</button>
        </div>
      )}
    </nav>
  );
}
