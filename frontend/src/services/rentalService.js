import api from './api';

export const rentalService = {
  borrow: (bookCopyId) => api.post('/api/rentals/borrow', { bookCopyId }),
  returnBook: (rentalId) => api.post(`/api/rentals/${rentalId}/return`),
  myRentals: () => api.get('/api/rentals/my'),
  active: () => api.get('/api/rentals/active'),
  overdue: () => api.get('/api/rentals/overdue')
};
