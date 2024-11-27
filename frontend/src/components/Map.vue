<template>
  <div id="map-container">
    <LMap
      style="height: 500px; width: 100%;"
      :zoom="13"
      :center="mapCenter"
    >
      <LTileLayer :url="tileLayerUrl" :attribution="tileLayerAttribution" />
      <LMarker
        v-for="(marker, index) in markers"
        :key="index"
        :lat-lng="marker.position"
      >
        <LPopup>{{ marker.label }}</LPopup>
      </LMarker>
    </LMap>
  </div>
</template>

<script>
import { LMap, LTileLayer, LMarker, LPopup } from "vue3-leaflet";

export default {
  name: "MapComponent",
  components: {
    LMap,
    LTileLayer,
    LMarker,
    LPopup,
  },
  data() {
    return {
      mapCenter: [-23.55052, -46.633308], // Coordenadas iniciais (São Paulo, Brasil)
      tileLayerUrl: "https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png",
      tileLayerAttribution:
        'Map data © <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors',
      markers: [
        {
          position: [-23.55052, -46.633308],
          label: "Consulta em São Paulo",
        },
        {
          position: [-22.906847, -43.172896],
          label: "Consulta no Rio de Janeiro",
        },
      ],
    };
  },
};
</script>

<style>
#map-container {
  margin: 20px 0;
}
</style>
