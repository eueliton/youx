<template>
    <div class="login-page">
      <h1>Login</h1>
      <Login />
    </div>
  </template>
  
  <script>
  import Login from "../components/Login.vue";
  
  export default {
    name: "LoginPage",
    components: {
      Login,
    },
  };
  </script>
  
  <style scoped>
  .login-page {
    display: flex;
    justify-content: center;
    align-items: center;
    min-height: 100vh;
    background-color: #f4f4f4;
  }
  </style>
  