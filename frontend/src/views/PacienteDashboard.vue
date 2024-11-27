<template>
    <div class="paciente-dashboard">
      <h1>Dashboard do Paciente</h1>
      <Consultas />
    </div>
  </template>
  
  <script>
  import Consultas from "../components/Consultas.vue";
  
  export default {
    name: "PacienteDashboard",
    components: {
      Consultas,
    },
  };
  </script>
  
  <style scoped>
  .paciente-dashboard {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f4f4f4;
  }
  </style>
  