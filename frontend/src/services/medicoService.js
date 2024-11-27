
import api from './../configs/api';

export default {

    async fetchMedicos() {
        const response = await api.get('/usuarios/all/medicos');
        return response.data;
    },

};

