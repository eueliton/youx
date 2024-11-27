<template>
    <div class="register">
      <form @submit.prevent="register">
        <div>
          <label for="name">Nome:</label>
          <input type="text" v-model="name" id="name" required />
        </div>
        <div>
          <label for="email">E-mail:</label>
          <input type="email" v-model="email" id="email" required />
        </div>
        <div>
          <label for="password">Senha:</label>
          <input type="password" v-model="password" id="password" required />
        </div>
        <button type="submit">Registrar</button>
        <p v-if="errorMessage" class="error-message">{{ errorMessage }}</p>
      </form>
    </div>
  </template>
  
  <script>
  import authService from "../services/authService";
 
  
  export default {
    name: "RegisterComponent",
    data() {
      return {
        name: "",
        email: "",
        password: "",
        errorMessage: "",
      };
    },
    methods: {
      async register() {
        try {
          await authService.register({
            name: this.name,
            email: this.email,
            password: this.password,
          });
          this.$router.push("/login");
        } catch (error) {
          this.errorMessage = "Erro ao registrar, tente novamente!";
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .register {
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-top: 50px;
  }
  
  input {
    margin: 10px 0;
    padding: 10px;
  }
  
  button {
    margin-top: 20px;
    padding: 10px;
    background-color: #007bff;
    color: white;
    border: none;
    border-radius: 5px;
  }
  
  .error-message {
    color: red;
    margin-top: 10px;
  }
  </style>
  