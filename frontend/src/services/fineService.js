import api from './api';

export const fineService = {
  myFines: () => api.get('/api/fines/my'),
  pay: (fineId) => api.post(`/api/fines/${fineId}/pay`)
};
