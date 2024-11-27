<template>
    <div>
      <Navbar />
      <Map />
    </div>
  </template>
  
  <script>
  import Navbar from "../components/Navbar.vue";
  import Map from "../components/Map.vue";
  
  export default {
    name: "DashboardPage",
    components: {
      Navbar,
      Map,
    },
  };
  </script>
  