<template>
  <div id="mobile" style="background-color: coral">
    <MobileMain></MobileMain>
    <bar/>
  </div>
</template>

<script>
import Bar from "@/components/mobile/Bar";
import MobileMain from "@/components/mobile/MobileMain";
export default {
  name: "Mobile",
  components:{
    Bar,
    MobileMain
  }
}
</script>

<style scoped>
*{
  padding: 0;
  margin: 0;
}
</style>
