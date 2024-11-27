import api from './../configs/api';

export default {
    async getConsultas(role) {
        const response = await api.get(`/consultas/${role}`);
        return response;
    },

    async aprovarConsulta(consultaId) {
        return await api.put(`/consultas/${consultaId}/aprovar`);
    },

    async finalizarConsulta(payload) {
        const { id, parecerFinal } = payload; 
        return  await api.put(`/consultas/${id}/finalizar`, {
            parecerFinal:  parecerFinal,
          });
    },
   
    async solicitarConsulta(consulta) {
        return await api.post('/consultas', consulta);
    },

};

