import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { extractErrorMessage } from '../utils/helpers';
import ErrorMessage from '../components/ErrorMessage';

export default function Register() {
  const { register } = useAuth();
  const navigate = useNavigate();
  const [form, setForm] = useState({ name: '', email: '', password: '', role: 'STUDENT' });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await register(form);
      navigate('/dashboard');
    } catch (err) {
      setError(extractErrorMessage(err, 'Registration failed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Create Account</h1>
        <p className="auth-subtitle">Join Bibliotech</p>
        <ErrorMessage message={error} />
        <label>Full Name</label>
        <input value={form.name} onChange={update('name')} required />
        <label>Email</label>
        <input type="email" value={form.email} onChange={update('email')} required />
        <label>Password</label>
        <input type="password" value={form.password} onChange={update('password')} required minLength={6} />
        <label>Role</label>
        <select value={form.role} onChange={update('role')}>
          <option value="STUDENT">Student</option>
          <option value="FACULTY">Faculty</option>
          <option value="LIBRARIAN">Librarian</option>
          <option value="ADMIN">Admin</option>
        </select>
        <button className="btn btn-primary btn-block" type="submit" disabled={loading}>
          {loading ? 'Creating account...' : 'Register'}
        </button>
        <p className="auth-footer">
          Already have an account? <Link to="/login">Login here</Link>
        </p>
      </form>
    </div>
  );
}
