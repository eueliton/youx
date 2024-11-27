<template>
    <nav class="navbar">
      <div class="navbar-brand">
        <router-link to="/" class="navbar-logo">Clínica Médica</router-link>
      </div>
      <ul class="navbar-links">
        <li v-if="isAuthenticated">
          <router-link to="/dashboard">Dashboard</router-link>
        </li>
        <li v-if="isAuthenticated && isPaciente">
          <router-link to="/consultas">Minhas Consultas</router-link>
        </li>
        <li v-if="isAuthenticated && isEnfermeiro">
          <router-link to="/pacientes">Pacientes</router-link>
        </li>
        <li v-if="isAuthenticated && isMedico">
          <router-link to="/agenda">Minha Agenda</router-link>
        </li>
        <li v-if="!isAuthenticated">
          <router-link to="/login">Login</router-link>
        </li>
        <li v-if="!isAuthenticated">
          <router-link to="/register">Registrar</router-link>
        </li>
        <li v-if="isAuthenticated">
          <button @click="logout" class="logout-button">Logout</button>
        </li>
      </ul>
    </nav>
  </template>
  
  <script>
  import { useAuthStore } from "../stores/auth";
  import { computed } from "vue";
  
  export default {
    name: "NavbarComponent",
    setup() {
      const authStore = useAuthStore();
  
      const isAuthenticated = computed(() => !!authStore.token);
      const isPaciente = computed(() => authStore.user?.role === "PACIENTE");
      const isEnfermeiro = computed(() => authStore.user?.role === "ENFERMEIRO");
      const isMedico = computed(() => authStore.user?.role === "MEDICO");
  
      const logout = () => {
        authStore.logout();
        window.location.href = "/"; // Redireciona após logout
      };
  
      return {
        isAuthenticated,
        isPaciente,
        isEnfermeiro,
        isMedico,
        logout,
      };
    },
  };
  </script>
  
  <style scoped>
  .navbar {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 10px 20px;
    background-color: #007bff;
    color: white;
  }
  
  .navbar-logo {
    font-size: 1.5em;
    color: white;
    text-decoration: none;
    font-weight: bold;
  }
  
  .navbar-links {
    list-style: none;
    display: flex;
    gap: 15px;
    margin: 0;
    padding: 0;
  }
  
  .navbar-links a {
    color: white;
    text-decoration: none;
  }
  
  .navbar-links a:hover {
    text-decoration: underline;
  }
  
  .logout-button {
    background: none;
    border: none;
    color: white;
    cursor: pointer;
    font-size: 1em;
  }
  
  .logout-button:hover {
    text-decoration: underline;
  }
  </style>
  