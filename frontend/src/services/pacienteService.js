import api from './../configs/api';

export default {  
    async cadastrarPaciente(paciente) {
        const response = await api.post('/pacientes', paciente.value);        
        return response.data;
      },     
         
      async getPacientes() {
        const response = await api.get('/pacientes');
        return response.data;
      }, 

      async getPacientesConsultas(pacienteId) {
        const response = await api.get(`/pacientes/${pacienteId}/consultas`);
        return response.data;
      },
    
};

 