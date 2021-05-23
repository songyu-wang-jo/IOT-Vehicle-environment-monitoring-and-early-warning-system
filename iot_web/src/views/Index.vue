<template>
  <div id="index">
    <router-view/>
  </div>
</template>

<script>
import IndexMobile from "@/views/IndexMobile";
import IndexPC from "@/views/IndexPC";

export default {
  name: "Index",
  components: {
    IndexMobile,
    IndexPC
  },
  data: ()=>{
    return {

    }
  },
  methods:{

  },
  created() {
  },
  beforeMount() {
    if (this.$store.state.userToken === ''){
      console.log(this.$store.state.userToken);
      this.isMobileFlag ? this.$router.replace({path: '/Mogin'}) : this.$router.replace({path: '/Pogin'})
    }else{
      this.isMobileFlag ? this.$router.push({path: 'mIndex'}) : this.$router.push({path: 'pcIndex'})
    }
  },
  computed: {
    isMobileFlag:()=> {
      return navigator.userAgent.match(/(phone|pad|pod|iPhone|iPod|ios|iPad|Android|Mobile|BlackBerry|IEMobile|MQQBrowser|JUC|Fennec|wOSBrowser|BrowserNG|WebOS|Symbian|Windows Phone)/i);
    }
  }
}
</script>

<style scoped>

</style>
