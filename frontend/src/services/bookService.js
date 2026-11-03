import api from './api';

export const bookService = {
  getAll: (q) => api.get('/api/books', { params: q ? { q } : {} }),
  getAvailable: () => api.get('/api/books/available'),
  getById: (id) => api.get(`/api/books/${id}`),
  create: (data) => api.post('/api/books', data),
  update: (id, data) => api.put(`/api/books/${id}`, data),
  remove: (id) => api.delete(`/api/books/${id}`),
  getBranches: () => api.get('/api/books/branches'),
  getCopies: (bookId) => api.get(`/api/books/${bookId}/copies`),
  addCopy: (bookId, data) => api.post(`/api/books/${bookId}/copies`, data),
  updateCopyStatus: (copyId, status) => api.put(`/api/books/copies/${copyId}/status`, { status })
};
