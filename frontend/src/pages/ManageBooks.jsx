import React, { useEffect, useState } from 'react';
import { bookService } from '../services/bookService';
import Loader from '../components/Loader';
import ErrorMessage from '../components/ErrorMessage';
import { extractErrorMessage } from '../utils/helpers';

const EMPTY_FORM = { isbn: '', title: '', author: '', category: '', publisher: '', description: '' };

export default function ManageBooks() {
  const [books, setBooks] = useState([]);
  const [form, setForm] = useState(EMPTY_FORM);
  const [editingId, setEditingId] = useState(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [saving, setSaving] = useState(false);

  const load = async () => {
    setLoading(true);
    try {
      const res = await bookService.getAll();
      setBooks(res.data);
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not load books'));
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    load();
  }, []);

  const update = (field) => (e) => setForm({ ...form, [field]: e.target.value });

  const resetForm = () => {
    setForm(EMPTY_FORM);
    setEditingId(null);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setSaving(true);
    setError('');
    try {
      if (editingId) {
        await bookService.update(editingId, form);
      } else {
        await bookService.create(form);
      }
      resetForm();
      await load();
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not save book'));
    } finally {
      setSaving(false);
    }
  };

  const handleEdit = (book) => {
    setEditingId(book.id);
    setForm({
      isbn: book.isbn,
      title: book.title,
      author: book.author,
      category: book.category || '',
      publisher: book.publisher || '',
      description: book.description || ''
    });
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Delete this book and all of its copies?')) return;
    setError('');
    try {
      await bookService.remove(id);
      await load();
    } catch (err) {
      setError(extractErrorMessage(err, 'Could not delete book'));
    }
  };

  return (
    <div className="page">
      <h1>Manage Books</h1>
      <ErrorMessage message={error} />

      <form className="inline-form" onSubmit={handleSubmit}>
        <h2>{editingId ? 'Edit Book' : 'Add New Book'}</h2>
        <div className="form-grid">
          <input placeholder="ISBN" value={form.isbn} onChange={update('isbn')} required />
          <input placeholder="Title" value={form.title} onChange={update('title')} required />
          <input placeholder="Author" value={form.author} onChange={update('author')} required />
          <input placeholder="Category" value={form.category} onChange={update('category')} />
          <input placeholder="Publisher" value={form.publisher} onChange={update('publisher')} />
        </div>
        <textarea placeholder="Description" value={form.description} onChange={update('description')} />
        <div className="form-actions">
          <button className="btn btn-primary" type="submit" disabled={saving}>
            {editingId ? 'Update Book' : 'Add Book'}
          </button>
          {editingId && (
            <button className="btn btn-outline" type="button" onClick={resetForm}>Cancel</button>
          )}
        </div>
      </form>

      <h2>All Books</h2>
      {loading ? (
        <Loader />
      ) : (
        <table className="data-table">
          <thead>
            <tr>
              <th>Title</th>
              <th>Author</th>
              <th>ISBN</th>
              <th>Copies</th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            {books.map((book) => (
              <tr key={book.id}>
                <td>{book.title}</td>
                <td>{book.author}</td>
                <td>{book.isbn}</td>
                <td>{book.availableCopies}/{book.totalCopies}</td>
                <td>
                  <button className="btn btn-outline btn-sm" onClick={() => handleEdit(book)}>Edit</button>
                  <button className="btn btn-danger btn-sm" onClick={() => handleDelete(book.id)}>Delete</button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      )}
    </div>
  );
}
