import { createRouter, createWebHistory } from 'vue-router';
import Login from '../components/Login.vue';
import Dashboard from '../components/Dashboard.vue';
import { useAuthStore } from '../stores/auth';
import CadastrarPacientePage from '@/views/CadastrarPacientePage.vue';
import Pacientes from '@/components/Pacientes.vue';
import ConsultasPage from '@/views/ConsultasPage.vue';
import SolicitarConsultaPage from '@/views/SolicitarConsultaPage.vue';

const routes = [
  { path: '/', component: Login },
  { 
    path: '/dashboard', 
    component: Dashboard,
    meta: { requiresAuth: true }, 
    
  },
  { path: '/cadastrar-paciente', 
    component: CadastrarPacientePage, 
    meta: { requiresAuth: true } 
  },
  { path: '/pacientes', 
    component: Pacientes, 
    meta: { requiresAuth: true } 
  },
  { path: '/consultas', 
    component: ConsultasPage, 
    meta: { requiresAuth: true } 
  },
  { path: '/solicitar-consulta', 
    component: SolicitarConsultaPage, 
    meta: { requiresAuth: true } 
  },


   
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

// Middleware para proteger rotas
router.beforeEach((to, from, next) => {
  const authStore = useAuthStore();
  if (to.meta.requiresAuth && !authStore.token) {
    next('/');
  } else {
    next();
  }
});

export default router;