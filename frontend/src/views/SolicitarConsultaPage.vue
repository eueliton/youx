<template>
  <div class="solicitar-consulta">
    <h1>Solicitar Nova Consulta</h1>

    <form @submit.prevent="solicitarConsulta">
      <div class="form-group">
        <label for="medico">Médico</label>
        <select id="medico" v-model="consulta.medicoId" required>
          <option value="" disabled>Selecione um médico</option>
          <option v-for="medico in medicos" :key="medico.id" :value="medico.id">
            {{ medico.nome }}
          </option>
        </select>
      </div>

      <div class="form-group">
        <label for="dataHora">Data e Hora</label>
        <input type="datetime-local" id="dataHora" v-model="consulta.dataHora" required />
      </div>

      <div class="form-group">
        <label for="motivo">Motivo da Consulta</label>
        <textarea id="motivoConsulta" v-model="consulta.motivoConsulta" placeholder="Descreva o motivo" required></textarea>
      </div>

     
      <div>
        <label for="local">Local:</label>
        <MapWithSearch
          :initialLatitude="consulta.latitude || -14.235"
          :initialLongitude="consulta.longitude || -51.9253"
          @update-latitude="consulta.latitude  = $event"
          @update-longitude="consulta.longitude = $event"
        />
      </div>

      <div class="form-group">
        <label for="latitude">Latitude</label>
        <input type="number" id="latitude" v-model="consulta.latitude" readonly />
      </div>

      <div class="form-group">
        <label for="longitude">Longitude</label>
        <input type="number" id="longitude" v-model="consulta.longitude" readonly />
      </div>

      <button type="submit" :disabled="loading">Solicitar Consulta</button>
    </form>

    <div v-if="successMessage" class="success-message">
      {{ successMessage }}
    </div>

    <div v-if="errorMessage" class="error-message">
      {{ errorMessage }}
    </div>
  </div>
</template>

<script>
import { ref, onMounted } from 'vue';
 
import consultaService from '../services/consultaService';
import medicoService from '../services/medicoService';
import { useAuthStore } from "../stores/auth";   
import MapWithSearch from "@/components/MapWithSearch.vue";
export default {
  name: 'SolicitarConsultaPage',
  components: {
    MapWithSearch,
  },
  setup() {
    const authStore = useAuthStore();
    const medicos = ref([]);
    const consulta = ref({
      pacienteId: authStore.user.id,
      medicoId: '',
      dataHora: '',
      motivoConsulta: '',
      latitude: '',
      longitude: '',
    });    
    const loading = ref(false);
    const successMessage = ref('');
    const errorMessage = ref('');

    const carregarMedicos = async () => {
      try {
        medicos.value = await  medicoService.fetchMedicos();
      } catch (error) {
        console.error('Erro ao carregar médicos:', error);
        errorMessage.value = 'Erro ao carregar a lista de médicos.';
      }
    };

    const atualizarCoordenadas = (latitude, longitude) => {
      consulta.value.latitude = latitude;
      consulta.value.longitude = longitude;
    };


    const solicitarConsultaHandler = async () => {
      successMessage.value = '';
      errorMessage.value = '';
      if (!consulta.value.latitude || !consulta.value.longitude) {
        errorMessage.value = "Latitude e longitude são obrigatórias.";
        return;
      }
      loading.value = true;

      try {
        await consultaService.solicitarConsulta(consulta.value);
        successMessage.value = 'Consulta solicitada com sucesso!';
        consulta.value = { pacienteId: authStore.user.id, medicoId: '', dataHora: '', motivoConsulta: '', longitude: '',latitude: '' }; // Resetar formulário
      } catch (error) {
        console.error('Erro ao solicitar consulta:', error);
        errorMessage.value = 'Não foi possível solicitar a consulta.';
      } finally {
        loading.value = false;
      }
    };

    onMounted(() => {
      carregarMedicos();
    });

    return {
      medicos,
      consulta,
      loading,
      successMessage,
      errorMessage,
      solicitarConsulta: solicitarConsultaHandler,
      atualizarCoordenadas
    };
  },
};
</script>

<style scoped>
.solicitar-consulta {
  max-width: 600px;
  margin: 0 auto;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  font-weight: bold;
  margin-bottom: 5px;
}

input,
select,
textarea {
  width: 100%;
  padding: 8px;
  font-size: 16px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

button {
  background-color: #007bff;
  color: #fff;
  border: none;
  padding: 10px 15px;
  font-size: 16px;
  border-radius: 4px;
  cursor: pointer;
}

button:disabled {
  background-color: #aaa;
  cursor: not-allowed;
}

.success-message {
  color: green;
  margin-top: 15px;
}

.error-message {
  color: red;
  margin-top: 15px;
}
</style>