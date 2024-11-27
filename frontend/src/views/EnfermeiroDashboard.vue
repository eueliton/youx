<template>
    <div class="enfermeiro-dashboard">
      <h1>Dashboard do Enfermeiro</h1>
      <Pacientes />
    </div>
  </template>
  
  <script>
  import Pacientes from "../components/Pacientes.vue";
  
  export default {
    name: "EnfermeiroDashboard",
    components: {
      Pacientes,
    },
  };
  </script>
  
  <style scoped>
  .enfermeiro-dashboard {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f4f4f4;
  }
  </style>
  