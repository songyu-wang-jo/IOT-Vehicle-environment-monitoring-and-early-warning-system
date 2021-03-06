<template>
  <div class="mobile_main">
    <Map/>
  </div>
</template>

<script>
import Map from "@/components/Map";
export default {
  name: "MobileMain",
  components: {
    Map
  }
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
}
.mobile_main{
  width: 100vw;
}
</style>
