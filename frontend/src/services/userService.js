import axios from 'axios';

const API_URL = 'http://localhost:8080/api/users';

export default {
  async getUserDetails() {
    return await axios.get(`${API_URL}/me`);
  },

  async getPacientes() {
    return await axios.get(`${API_URL}/pacientes`);
  },
};
