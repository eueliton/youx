<template>
    <div id="map" style="height: 400px;"></div>
  </template>
  
  <script>
  import L from "leaflet";
  import "leaflet/dist/leaflet.css";
  
  export default {
    name: "MapPicker",
    props: {
      initialLatitude: {
        type: Number,
        default: -14.235, // Centro aproximado do Brasil
      },
      initialLongitude: {
        type: Number,
        default: -51.9253, // Centro aproximado do Brasil
      },
      onLocationSelected: {
        type: Function,
        required: true,
      },
    },
    mounted() {
      // Inicializando o mapa
      this.map = L.map("map").setView([this.initialLatitude, this.initialLongitude], 4);
  
      // Adicionando camada do OpenStreetMap
      L.tileLayer("https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png", {
        attribution: 'Map data © <a href="https://openstreetmap.org">OpenStreetMap</a> contributors',
      }).addTo(this.map);
  
      // Adicionando um marcador inicial
      this.marker = L.marker([this.initialLatitude, this.initialLongitude], { draggable: true })
        .addTo(this.map)
        .bindPopup("Arraste o marcador para selecionar a localização.")
        .openPopup();
  
      // Evento ao mover o marcador
      this.marker.on("dragend", () => {
        const position = this.marker.getLatLng();
        this.onLocationSelected(position.lat, position.lng);
      });
  
      // Evento para adicionar marcador ao clicar no mapa
      this.map.on("click", (e) => {
        const { lat, lng } = e.latlng;
        this.marker.setLatLng([lat, lng]);
        this.onLocationSelected(lat, lng);
      });
    },
    beforeUnmount() {
      this.map.off(); // Remove eventos do mapa ao desmontar o componente
      this.map.remove();
    },
  };
  </script>
  
  <style scoped>
  #map {
    height: 100%;
    width: 100%;
  }
  </style>
  