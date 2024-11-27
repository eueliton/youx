<template>
  <div>
    <h1>Minhas Consultas</h1>
    <div v-if="loading">Carregando...</div>
    <div v-else-if="consultas.length === 0">Nenhuma consulta encontrada.</div>
    <ul v-else>
      <li v-for="consulta in consultas" :key="consulta.id">
        <p><strong>Data:</strong> {{ formatDate(consulta.dataHora) }}</p>
        <p><strong>Médico:</strong> {{ consulta.medico.nome }}</p>
        <p><strong>Motivo:</strong> {{ consulta.motivoConsulta }}</p>
        <p><strong>Status:</strong> {{ consulta.status }}</p>
        <hr />
      </li>
    </ul>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
import  pacienteService from '../services/pacienteService';
import { useAuthStore } from '../stores/auth';
import moment from 'moment';
import 'moment/locale/pt-br'; // Importa a localização brasileira
 
 
export default {
  name: 'ConsultasPage',
  methods: {
    formatDate(date) {     
      return moment(date).format('DD/MM/YYYY HH:mm:ss');
    }
  },
  setup() {
    const consultas = ref([]);
    const loading = ref(true);
    const authStore = useAuthStore();
    const user = authStore.user;
    const pacienteId = user.id; 

    const carregarConsultas = async () => {
      try {
        consultas.value = await pacienteService.getPacientesConsultas(pacienteId);
      } catch (error) {
        console.error('Erro ao carregar consultas:', error);
      } finally {
        loading.value = false;
      }
    };

    onMounted(() => {
      carregarConsultas();
    });

    return {
      consultas,
      loading,
    };
  },
};
</script>