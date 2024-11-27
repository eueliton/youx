<template>
    <div class="map-with-search">
      <div class="search-container">
        <input
          v-model="address"
          @keyup.enter="searchLocation"
          placeholder="Digite um endereço e pressione Enter"
        />
        <button @click="searchLocation">Buscar</button>
      </div>
      <div id="map" style="width: 100%; height: 400px; margin-top: 10px;"></div>
      
    </div>
  </template>
  
  <script>
  import L from "leaflet";
  import "leaflet/dist/leaflet.css";
  
  export default {
    name: "MapWithSearch",
    props: {
      initialLatitude: {
        type: Number,
        default: -14.235, // Centro do Brasil
      },
      initialLongitude: {
        type: Number,
        default: -51.9253, // Centro do Brasil
      },
    },
    data() {
      return {
        address: "",
        latitude: this.initialLatitude,
        longitude: this.initialLongitude,
        map: null,
        marker: null,
      };
    },
    mounted() {
      this.initializeMap();
    },
    watch: {
      // Sempre que as coordenadas mudarem, envie para o componente pai
      latitude(newLatitude) {
        this.$emit("update-latitude", newLatitude);
      },
      longitude(newLongitude) {
        this.$emit("update-longitude", newLongitude);
      },
    },
    methods: {
      initializeMap() {
        // Configure o ícone padrão do Leaflet
        delete L.Icon.Default.prototype._getIconUrl;
        L.Icon.Default.mergeOptions({
          iconRetinaUrl: require("leaflet/dist/images/marker-icon-2x.png"),
          iconUrl: require("leaflet/dist/images/marker-icon.png"),
          shadowUrl: require("leaflet/dist/images/marker-shadow.png"),
        });
  
        // Inicialize o mapa
        this.map = L.map("map").setView([this.latitude, this.longitude], 5);
  
        // Adicione a camada base (OpenStreetMap)
        L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
          attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors',
        }).addTo(this.map);
  
        // Adicione um marcador inicial
        this.marker = L.marker([this.latitude, this.longitude], { draggable: true })
          .addTo(this.map)
          .bindPopup("Arraste para ajustar a localização.")
          .openPopup();
  
        // Atualize as coordenadas ao arrastar o marcador
        this.marker.on("dragend", () => {
          const position = this.marker.getLatLng();
          this.latitude = position.lat;
          this.longitude = position.lng;
        });
      },
      async searchLocation() {
        if (!this.address) {
          alert("Por favor, insira um endereço.");
          return;
        }
  
        try {
          const response = await fetch(
            `https://nominatim.openstreetmap.org/search?format=json&q=${encodeURIComponent(
              this.address
            )}`
          );
          const data = await response.json();
  
          if (data && data.length > 0) {
            const { lat, lon } = data[0];
            this.latitude = parseFloat(lat);
            this.longitude = parseFloat(lon);
  
            // Atualize o mapa e o marcador
            this.map.setView([this.latitude, this.longitude], 14);
            this.marker.setLatLng([this.latitude, this.longitude]);
          } else {
            alert("Endereço não encontrado. Por favor, tente novamente.");
          }
        } catch (error) {
          console.error("Erro ao buscar o endereço:", error);
          alert("Ocorreu um erro ao buscar o endereço.");
        }
      },
    },
  };
  </script>
  
  <style scoped>
  .map-with-search {
    display: flex;
    flex-direction: column;
  }
  
  .search-container {
    display: flex;
    gap: 10px;
  }
  
  .search-container input {
    flex: 1;
    padding: 8px;
    font-size: 16px;
  }
  
  .search-container button {
    padding: 8px 12px;
    font-size: 16px;
    cursor: pointer;
  }
  
  .coordinates {
    margin-top: 10px;
    font-size: 14px;
  }
  </style>
  