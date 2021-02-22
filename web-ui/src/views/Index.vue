<template>
<div id="index">
  index
</div>
</template>

<script>
export default {
  name: "Index",
  methods: {
    openHomePage(){
      this.$router.replace({name: 'home'})
    }
  },
  mounted() {
    setTimeout(this.openHomePage,3000)
  }
}
</script>

<style scoped>

</style>
