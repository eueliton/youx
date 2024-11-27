<template>
    <div class="medico-dashboard">
      <h1>Dashboard do Médico</h1>
      <Consultas />
    </div>
  </template>
  
  <script>
  import Consultas from "../components/Consultas.vue";
  
  export default {
    name: "MedicoDashboard",
    components: {
      Consultas,
    },
  };
  </script>
  
  <style scoped>
  .medico-dashboard {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f4f4f4;
  }
  </style>
  