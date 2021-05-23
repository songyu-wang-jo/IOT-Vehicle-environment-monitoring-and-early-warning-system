<template>
  <div id="welcome">
    汽车安全管理平台
<!--    <img :src="bkImageSRC" width="100%" height="100%" alt="背景图片丢失！" />-->
  </div>
</template>

<script>
export default {
  name: "Welcome",
  data(){
    return {
      // bkImageSRC: require('../../src/assets/welcome.jpeg')
    }
  },
  methods: {

  },
  mounted() {
    window.setTimeout(()=>{
      this.$router.replace({name: 'index'})
    }, 3000)
  }
}
</script>

<style scoped>
#welcome{
  width: 100vw;
  height: 100vh;
  line-height: 100vh;
  text-align: center;
  color: #893d3d;
  font-size: 30px;
}
</style>
