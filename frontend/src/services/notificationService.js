import api from './api';

export const notificationService = {
  myNotifications: () => api.get('/api/notifications/my'),
  markRead: (id) => api.put(`/api/notifications/${id}/read`)
};
