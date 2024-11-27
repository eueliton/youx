import { defineStore } from "pinia";
import axios from "axios";
import api from './../configs/api';

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: null,
    token: null,
  }),

  actions: {
    /**
     * Faz login com as credenciais do usuário.
     * @param {Object} credentials - Dados do login (email e senha).
     * @returns {Promise} - Dados do usuário logado.
     */
    async login(email, senha) {
      try { 
        const response = await api.post('/auth/login', {
          email: email,
          password: senha,
        });  

        this.token = response.data.token;
        this.user = response.data.user;

        // Configura o cabeçalho padrão para futuras requisições
        axios.defaults.headers.common["Authorization"] = `Bearer ${this.token}`;

        return response.data;
      } catch (error) {
        console.error("Erro no login:", error);
        throw error;
      }
    },

    /**
     * Faz o registro de um novo usuário.
     * @param {Object} userData - Dados do novo usuário.
     * @returns {Promise} - Dados do usuário registrado.
     */
    async register(userData) {
      try {
        const response = await axios.post("/api/auth/register", userData);
        return response.data;
      } catch (error) {
        console.error("Erro no registro:", error);
        throw error;
      }
    },

    /**
     * Faz logout limpando o token e dados do usuário.
     */
    logout() {
      this.user = null;
      this.token = null;
      delete axios.defaults.headers.common["Authorization"];
    },

    /**
     * Atualiza os dados do usuário autenticado.
     * @param {Object} updatedData - Novos dados do usuário.
     * @returns {Promise} - Dados do usuário atualizado.
     */
    async updateUser(updatedData) {
      try {
        const response = await axios.put("/api/auth/update", updatedData);
        this.user = response.data.user;
        return response.data;
      } catch (error) {
        console.error("Erro ao atualizar o usuário:", error);
        throw error;
      }
    },
  },

  getters: {
    /**
     * Retorna se o usuário está autenticado.
     * @returns {Boolean}
     */
    isAuthenticated: (state) => !!state.token,

    /**
     * Retorna o papel do usuário atual.
     * @returns {String|null}
     */
    userRole: (state) => state.user?.role || null,
  },
});
