import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import { extractErrorMessage } from '../utils/helpers';
import ErrorMessage from '../components/ErrorMessage';

export default function Login() {
  const { login } = useAuth();
  const navigate = useNavigate();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);
    try {
      await login(email, password);
      navigate('/dashboard');
    } catch (err) {
      setError(extractErrorMessage(err, 'Login failed'));
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="auth-page">
      <form className="auth-card" onSubmit={handleSubmit}>
        <h1>Bibliotech</h1>
        <p className="auth-subtitle">Sign in to your library account</p>
        <ErrorMessage message={error} />
        <label>Email</label>
        <input type="email" value={email} onChange={(e) => setEmail(e.target.value)} required />
        <label>Password</label>
        <input type="password" value={password} onChange={(e) => setPassword(e.target.value)} required />
        <button className="btn btn-primary btn-block" type="submit" disabled={loading}>
          {loading ? 'Signing in...' : 'Login'}
        </button>
        <p className="auth-footer">
          No account? <Link to="/register">Register here</Link>
        </p>
        <div className="demo-creds">
          <strong>Demo logins:</strong>
          <div>admin@bibliotech.com / Admin@123</div>
          <div>librarian@bibliotech.com / Librarian@123</div>
          <div>student@bibliotech.com / Student@123</div>
          <div>faculty@bibliotech.com / Faculty@123</div>
        </div>
      </form>
    </div>
  );
}
