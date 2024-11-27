<template>
    <div class="pacientes">
      <h3>Pacientes</h3>
      <ul>
        <li v-for="paciente in pacientes" :key="paciente.id">
          {{ paciente.nome }} - {{ paciente.telefone }} 
        </li>
      </ul>
    </div>
  </template>
  
  <script>
  import pacienteService from "../services/pacienteService";
  
  export default {
    name: "PacientesComponent",
    data() {
      return {
        pacientes: [],
      };
    },
    async created() {
      this.pacientes = await pacienteService.getPacientes();
    },
  };
  </script>
  
  <style scoped>
  .pacientes {
    margin-top: 50px;
  }
  </style>
  