<template>
    <div class="register-page">
      <h1>Registrar-se</h1>
      <Register />
    </div>
  </template>
  
  <script>
  import Register from "../components/Register.vue";
  
  export default {
    name: "RegisterPage",
    components: {
      Register,
    },
  };
  </script>
  
  <style scoped>
  .register-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f4f4f4;
  }
  </style>
  