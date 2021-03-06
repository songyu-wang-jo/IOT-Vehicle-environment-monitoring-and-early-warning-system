<template>
<div id="index">
  欢迎界面
</div>
</template>

<script>
function isMobile() {// eslint-disable-line no-unused-vars
  return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
}

export default {
  name: "Index",
  methods: {
    openHomePage(){
      if (isMobile()){
        this.$router.replace({path: '/mobile'})
      }else {
        this.$router.replace({path: '/pc'})
      }
    }
  },
  mounted() {
    setTimeout(this.openHomePage,3000)
  }
}
</script>

<style scoped>
*{
  padding: 0;
  margin: 0;
}

</style>
