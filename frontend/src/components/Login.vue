<template>
    <div class="login-container">
      <form @submit.prevent="handleLogin">
        <h1>Login</h1>
        <div>
          <label for="email">Email:</label>
          <input
            id="email"
            v-model="email"
            type="email"
            placeholder="Digite seu email"
            required
          />
        </div>
        <div>
          <label for="senha">Senha:</label>
          <input
            id="senha"
            v-model="senha"
            type="password"
            placeholder="Digite sua senha"
            required
          />
        </div>
        <button type="submit">Entrar</button>
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>
      </form>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  import { useAuthStore } from '../stores/auth'; // Importa a store de autenticação
  import { useRouter } from 'vue-router';
 
  export default {
    name: 'LoginComponent',
    setup() {
      const authStore = useAuthStore();
      const email = ref('');
      const senha = ref('');
      const errorMessage = ref('');
      const router = useRouter();
  
      const handleLogin = async () => {
        try {
          errorMessage.value = ''; // Limpa mensagens de erro
          await authStore.login(email.value, senha.value); // Chama a ação de login na store
          router.push('/dashboard'); // Redireciona após o login
        } catch (error) {
          errorMessage.value = error; // Mostra erro caso ocorra
        }
      };
  
      return {
        email,
        senha,
        errorMessage,
        handleLogin,
      };
    },
  };
  </script>
  
  <style scoped>
  .login-container {
    max-width: 400px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
  }
  form div {
    margin-bottom: 12px;
  }
  .error {
    color: red;
    font-size: 14px;
  }
  </style>