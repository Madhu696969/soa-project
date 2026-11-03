import React, { useEffect, useState } from 'react';
import { bookService } from '../services/bookService';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import StatusBadge from '../components/StatusBadge';
import { extractErrorMessage } from '../utils/helpers';

export default function ManageBookCopies() {
  const [books, setBooks] = useState([]);
  const [branches, setBranches] = useState([]);
  const [selectedBookId, setSelectedBookId] = useState('');
  const [copies, setCopies] = useState([]);
  const [form, setForm] = useState({ branchId: '', copyCode: '' });
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    async function init() {
      try {
        const [booksRes, branchesRes] = await Promise.all([
          bookService.getAll(),
          bookService.getBranches()
        ]);
        setBooks(booksRes.data);
        setBranches(branchesRes.data);
        if (booksRes.data.length > 0) {
          setSelectedBookId(String(booksRes.data[0].id));
        }
      } catch (err) {
        setError(extractErrorMessage(err, 'Could not load data'));
      } finally {
        setLoading(false);
      }
    }
    init();
  }, []);

  const loadCopies = async (bookId) => {
    if (!bookId) return;
    try {
      const res = await bookService.getCopies(bookId);
      setCopies(res.data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load copies'));
    }
  };

  useEffect(() => {
    loadCopies(selectedBookId);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedBookId]);

  const handleAddCopy = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      await bookService.addCopy(selectedBookId, form);
      setForm({ branchId: '', copyCode: '' });
      await loadCopies(selectedBookId);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not add copy'));
    } finally {
      setSaving(false);
    }
  };

  const handleStatusChange = async (copyId, status) => {
    setError('');
    try {
      await bookService.updateCopyStatus(copyId, status);
      await loadCopies(selectedBookId);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not update status'));
    }
  };

  if (loading) return <div className="page"><Loader /></div>;

  return (
    <div className="page">
      <h1>Manage Book Copies</h1>
      <ErrorMessage message={error} />

      <div className="form-grid">
        <select value={selectedBookId} onChange={(e) => setSelectedBookId(e.target.value)}>
          {books.map((b) => (
            <option key={b.id} value={b.id}>{b.title}</option>
          ))}
        </select>
      </div>

      <form className="inline-form" onSubmit={handleAddCopy}>
        <h2>Add Physical Copy</h2>
        <div className="form-grid">
          <select value={form.branchId} onChange={(e) => setForm({ ...form, branchId: e.target.value })} required>
            <option value="">Select Branch</option>
            {branches.map((br) => (
              <option key={br.id} value={br.id}>{br.name}</option>
            ))}
          </select>
          <input placeholder="Copy Code (e.g. CC-BOOK-03)" value={form.copyCode}
                 onChange={(e) => setForm({ ...form, copyCode: e.target.value })} required />
          <button className="btn btn-primary" type="submit" disabled={saving}>Add Copy</button>
        </div>
      </form>

      <h2>Copies for Selected Book</h2>
      <table className="data-table">
        <thead>
          <tr>
            <th>Copy Code</th>
            <th>Branch</th>
            <th>Status</th>
            <th>Change Status</th>
          </tr>
        </thead>
        <tbody>
          {copies.map((copy) => (
            <tr key={copy.id}>
              <td>{copy.copyCode}</td>
              <td>{copy.branchName}</td>
              <td><StatusBadge status={copy.status} /></td>
              <td>
                <select value={copy.status} onChange={(e) => handleStatusChange(copy.id, e.target.value)}>
                  <option value="AVAILABLE">AVAILABLE</option>
                  <option value="BORROWED">BORROWED</option>
                  <option value="LOST">LOST</option>
                  <option value="DAMAGED">DAMAGED</option>
                </select>
              </td>
            </tr>
          ))}
          {copies.length === 0 && <tr><td colSpan="4">No copies for this book yet.</td></tr>}
        </tbody>
      </table>
    </div>
  );
}
