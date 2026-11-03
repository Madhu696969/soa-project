import React from 'react';
import { Routes, Route, Navigate, useLocation } from 'react-router-dom';
import Navbar from './components/Navbar';
import ProtectedRoute from './components/ProtectedRoute';
import Login from './pages/Login';
import Register from './pages/Register';
import Dashboard from './pages/Dashboard';
import Books from './pages/Books';
import BookDetails from './pages/BookDetails';
import MyRentals from './pages/MyRentals';
import Fines from './pages/Fines';
import Notifications from './pages/Notifications';
import AdminDashboard from './pages/AdminDashboard';
import ManageBooks from './pages/ManageBooks';
import ManageBookCopies from './pages/ManageBookCopies';
import NotFound from './pages/NotFound';

export default function App() {
  const location = useLocation();
  const hideNav = ['/login', '/register'].includes(location.pathname);

  return (
    <div className="app-shell">
      {!hideNav && <Navbar />}
      <main className="app-main">
        <Routes>
          <Route path="/" element={<Navigate to="/dashboard" replace />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />

          <Route path="/dashboard" element={<ProtectedRoute><Dashboard /></ProtectedRoute>} />
          <Route path="/books" element={<ProtectedRoute><Books /></ProtectedRoute>} />
          <Route path="/books/:id" element={<ProtectedRoute><BookDetails /></ProtectedRoute>} />
          <Route path="/rentals" element={<ProtectedRoute><MyRentals /></ProtectedRoute>} />
          <Route path="/fines" element={<ProtectedRoute><Fines /></ProtectedRoute>} />
          <Route path="/notifications" element={<ProtectedRoute><Notifications /></ProtectedRoute>} />

          <Route path="/admin" element={
            <ProtectedRoute allowedRoles={['ADMIN', 'LIBRARIAN']}><AdminDashboard /></ProtectedRoute>
          } />
          <Route path="/admin/books" element={
            <ProtectedRoute allowedRoles={['ADMIN', 'LIBRARIAN']}><ManageBooks /></ProtectedRoute>
          } />
          <Route path="/admin/copies" element={
            <ProtectedRoute allowedRoles={['ADMIN', 'LIBRARIAN']}><ManageBookCopies /></ProtectedRoute>
          } />

          <Route path="*" element={<NotFound />} />
        </Routes>
      </main>
    </div>
  );
}
