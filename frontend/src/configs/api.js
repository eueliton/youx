import axios from 'axios';

const API_URL = 'http://localhost:8081/api'; // URL do backend

const api = axios.create({
  baseURL: API_URL,
  withCredentials: true, 
});
import { useAuthStore } from '../stores/auth';
   
  
 
api.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  const token = authStore.token;
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

export default api;