import api from './../configs/api';

export default {
   
  async register(user) {
    return await api.post('/register', user);
  },

  logout() {
    localStorage.removeItem('authToken');
    //delete api.defaults.headers.common['Authorization'];
  },

  isAuthenticated() {
    return !!localStorage.getItem('authToken');
  },
};
