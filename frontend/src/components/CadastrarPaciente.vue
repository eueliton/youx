<template>
    <div class="cadastrar-paciente-container">
      <form @submit.prevent="handleCadastro">
        <h1>Cadastrar Paciente</h1>
        <div>
          <label for="nome">Nome:</label>
          <input
            id="nome"
            v-model="paciente.nome"
            type="text"
            placeholder="Digite o nome do paciente"
            required
          />
        </div>
        <div>
          <label for="email">Email:</label>
          <input
            id="email"
            v-model="paciente.email"
            type="email"
            placeholder="Digite o email"
            required
          />
        </div>
        <div>
          <label for="telefone">Telefone:</label>
          <input
            id="telefone"
            v-model="paciente.telefone"
            type="text"
            placeholder="Digite o telefone"
            v-mask="'(##) #####-####'"           
            required
          />
        </div>
        <div>
          <label for="dataNascimento">Data de Nascimento:</label>
          <input
            id="dataNascimento"
            v-model="paciente.dataNascimento"
            type="date"
            required
          />
        </div>
        <div>
          <label for="endereco">Endereço:</label>
          <input
            id="endereco"
            v-model="paciente.endereco"
            type="text"
            placeholder="Digite o endereço"
            required
          />
        </div>
        <button type="submit">Cadastrar</button>
        <p v-if="mensagemErro" class="error">{{ mensagemErro }}</p>
        <p v-if="mensagemSucesso" class="success">{{ mensagemSucesso }}</p>
      </form>
    </div>
  </template>
  
  <script>
  import { ref } from 'vue';
  import pacienteService from "../services/pacienteService";
  
  export default {
    name: 'CadastrarPaciente',
    setup() {
      const paciente = ref({
        nome: '',
        email: '',
        telefone: '',
        dataNascimento: '',
        endereco: '',
      });
  
      const mensagemErro = ref('');
      const mensagemSucesso = ref('');
 
  
      const handleCadastro = async () => {
        try {
          mensagemErro.value = '';
          mensagemSucesso.value = '';
            
          await pacienteService.cadastrarPaciente(paciente);
  
          mensagemSucesso.value = 'Paciente cadastrado com sucesso!';
          paciente.value = {
            nome: '',
            email: '',
            telefone: '',
            dataNascimento: '',
            endereco: '',
          };
        } catch (error) {
          mensagemErro.value =
            error.response?.data?.message || 'Erro ao cadastrar o paciente.';
        }
      };
  
      return {
        paciente,
        mensagemErro,
        mensagemSucesso,
        handleCadastro,
      };
    },
  };
  </script>
  
  <style scoped>
  .cadastrar-paciente-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 20px;
    border: 1px solid #ddd;
    border-radius: 8px;
    background: #f9f9f9;
  }
  form div {
    margin-bottom: 12px;
  }
  .error {
    color: red;
    font-size: 14px;
  }
  .success {
    color: green;
    font-size: 14px;
  }
  </style>
  