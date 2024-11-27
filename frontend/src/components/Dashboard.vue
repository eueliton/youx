<template>
    <div class="dashboard">
        <div class="dashboard-content">
            <h1>Bem-vindo(a), {{ user.nome }}</h1>
            <p>Perfil: {{ user.role }}</p>
            <div id="map" class="map-container"></div>
            <div v-if="user.role === 'PACIENTE'">
                <h2>Suas Consultas</h2>
                <ul>
                    <li v-for="consulta in consultas" :key="consulta.id">
                        {{ consulta.medico.nome }} - {{ formatDate(consulta.dataHora) }} - {{ consulta.motivoConsulta }}
                        - {{ consulta.status }}
                    </li>
                </ul>
                <button @click="solicitarConsulta">Solicitar Nova Consulta</button>
            </div>

            <div v-if="user.role === 'ENFERMEIRO'">
                <h2>Gerenciar Consultas</h2>
                <ul>
                    <li v-for="consulta in consultas" :key="consulta.id">
                        {{ consulta.medico.nome }} - {{ formatDate(consulta.dataHora) }} - {{ consulta.motivoConsulta }}
                        - {{ consulta.status }}
                        <button @click="aprovarConsulta(consulta.id)">Aprovar</button>
                    </li>
                </ul>
                <button @click="cadastrarPaciente">Cadastrar Novo Paciente</button>
            </div>

            <div v-if="user.role === 'MEDICO'">
                <h2>Sua Agenda</h2>
                <ul>
                    <li v-for="consulta in consultas" :key="consulta.id">
                        {{ formatDate(consulta.dataHora) }} - {{ consulta.motivoConsulta }} - {{ consulta.status }}
                        <button @click="abrirModal(consulta)">Finalizar Consulta</button>
                    </li>
                </ul>
                <!-- Modal -->
                <div v-if="modalAberto" class="modal-overlay">
                    <div class="modal">
                        <h2>Finalizar Consulta</h2>
                        <p>Informe o parecer final para a consulta:</p>
                        <textarea v-model="parecerFinal" placeholder="Digite o parecer final..."></textarea>
                        <button @click="finalizarConsulta">Enviar</button>
                        <button @click="fecharModal">Cancelar</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script>

import { ref, onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';
import consultaService from '../services/consultaService';
import { useRouter } from 'vue-router';
import L from "leaflet";
import "leaflet/dist/leaflet.css";
import moment from 'moment';
import 'moment/locale/pt-br';



export default {
    name: "DashBoardComponent",
    methods: {
        formatDate(date) {
            return moment(date).format('DD/MM/YYYY HH:mm:ss');
        }
    },
    setup() {
        const authStore = useAuthStore();
        const user = authStore.user;
        const consultas = ref([]);
        const router = useRouter();
        const modalAberto = ref(false);
        const consultaAtual = ref(null);
        const parecerFinal = ref("");
        const map = ref(null);
        const markers = ref([]);



        const abrirModal = (consulta) => {
            consultaAtual.value = consulta;
            parecerFinal.value = "";
            modalAberto.value = true;
        };

        const fecharModal = () => {
            modalAberto.value = false;
        };
        const fetchConsultas = async () => {
            try {
                const response = await consultaService.getConsultas(user.role);
                consultas.value = response.data;
                atualizarMapa();
            } catch (error) {
                console.error('Erro ao buscar consultas:', error);
            }
        };



        const solicitarConsulta = () => {
            router.push('/solicitar-consulta');
        };

        const aprovarConsulta = async (id) => {

            try {
                await consultaService.aprovarConsulta(id);
                fetchConsultas();
            } catch (error) {
                console.error('Erro ao aprovar consulta:', error);
            }
        };

        const finalizarConsulta = async () => {
            if (!parecerFinal.value) {
                alert("O parecer final não pode estar vazio.");
                return;
            }
            try {

                await consultaService.finalizarConsulta({
                    id: consultaAtual.value.id,
                    parecerFinal: parecerFinal.value,
                });
                fecharModal();
                fetchConsultas();
            } catch (error) {
                console.error('Erro ao finalizar consulta:', error);
            }
        };

        const cadastrarPaciente = () => {
            // Navegar para a página de cadastro de paciente
            router.push('/cadastrar-paciente');
        };

        const inicializarMapa = () => {
            map.value = L.map("map").setView([-14.235, -51.9253], 4);
            // Configure o ícone padrão do Leaflet
            delete L.Icon.Default.prototype._getIconUrl;
            L.Icon.Default.mergeOptions({
                iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
                iconUrl: require("leaflet/dist/images/marker-icon.png"),
                shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
            });


            L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
                attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors',
            }).addTo(map.value);
        };

        const atualizarMapa = () => {
            if (!map.value) {
                inicializarMapa();
            }


            markers.value.forEach((marker) => map.value.removeLayer(marker));
            markers.value = [];


            consultas.value.forEach((consulta) => {
                if (consulta.latitude && consulta.longitude) {
                    const marker = L.marker([consulta.latitude, consulta.longitude])
                        .addTo(map.value)
                        .bindPopup(
                            `<strong>Médico:</strong> ${consulta.medico.nome}<br>
               <strong>Paciente:</strong> ${consulta.paciente.nome}<br>
               <strong>Data:</strong> ${moment(consulta.dataHora).format('DD/MM/YYYY HH:mm:ss')}
               <strong>Status:</strong> ${consulta.status}<br>
               `
                        );
                    markers.value.push(marker);
                }
            });
        };


        onMounted(() => {
            fetchConsultas();
            inicializarMapa();
        });

        return {
            user,
            consultas,
            solicitarConsulta,
            aprovarConsulta,
            finalizarConsulta,
            cadastrarPaciente,
            modalAberto,
            consultaAtual,
            parecerFinal,
            abrirModal,
            fecharModal,

        };
    },
};
</script>

<style scoped>
.dashboard {
    padding: 20px;
}

.dashboard-content {
    max-width: 800px;
    margin: 0 auto;
}

h1 {
    text-align: center;
    margin-bottom: 20px;
}

ul {
    list-style: none;
    padding: 0;
}

li {
    padding: 10px;
    margin-bottom: 5px;
    border: 1px solid #ccc;
    border-radius: 5px;
}

button {
    margin-left: 10px;
    background-color: #4caf50;
    color: white;
    border: none;
    border-radius: 5px;
    padding: 5px 10px;
    cursor: pointer;
}

button:hover {
    background-color: #45a049;
}

.modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    justify-content: center;
    align-items: center;
}

.modal {
    background: white;
    padding: 20px;
    border-radius: 8px;
    width: 400px;
    max-width: 90%;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

textarea {
    width: 100%;
    height: 100px;
    margin-bottom: 10px;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
}

.map-container {
    height: 400px;
    margin-bottom: 20px;
    border: 1px solid #ccc;
    border-radius: 8px;
}
</style>