<template>
  <div class="bottom_bar">
    <div id="error" class="bar_item" @click="changePage('异常')">异常</div>
    <div id="danger" class="bar_item" @click="changePage('报警')">报警</div>
    <div id="position" class="bar_item" @click="changePage('定位')">定位</div>
    <div id="temperature" class="bar_item" @click="changePage('温度')">温度</div>
  </div>
</template>

<script>
export default {
  name: "Bar",
  methods: {
    changePage(topic) {
      alert(topic)
    }
  },
  created() {


  }
}
</script>

<style scoped>

.bottom_bar {
  display: flex;
  justify-content: space-around;
  position: fixed;
  bottom: 0;
  left: 0;
  text-align: center;
  height: 10vh;
  width: 100vw;
  border-top: 1px #c1c1a7 solid;
}

.bar_item {
  position: relative;
  height: 100vh;
  width: 20vw;
  float: left;
}
</style>
