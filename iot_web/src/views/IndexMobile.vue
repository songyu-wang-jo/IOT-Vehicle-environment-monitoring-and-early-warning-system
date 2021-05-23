<template>
  <div id="indexMobile">
    <el-container id="mainCon">
      <el-header id="title">设备管理中心</el-header>
      <el-main id="main_main">
        <router-view/>
      </el-main>
      <el-footer id="menu">
        <div class="menu_item" @click="changeView('/mIndex/messageView')">
          <svg-icon :icon-class="this.currentPage==='/mIndex/messageView'? 'car_detail':'car_detail_normal'" class="icon_item"/>
          <span :style="this.currentPage==='/mIndex/messageView'? 'color: #1296db': 'color: #707070'">信息</span>
        </div>
        <div class="menu_item" @click="changeView('/mIndex/statusView')">
          <svg-icon :icon-class="this.currentPage==='/mIndex/messageView'? 'warning_normal':'warning'" class="icon_item"/>
          <span :style="this.currentPage==='/mIndex/statusView'? 'color: #1296db': 'color: #707070'">状态</span>
        </div>
      </el-footer>
    </el-container>
  </div>
</template>

<script>

export default {
  name: "indexMobile",
  data(){
    return{
      currentPage: ''
    }
  },
  mounted() {
    this.changeView('/mIndex/messageView')
  },
  methods: {
    changeView(routerName) {
      if (this.currentPage !== routerName){
        this.$router.push({path: routerName})
        this.currentPage = routerName
      }
    },
  }
}
</script>

<style scoped>
#indexMobile{
  width: 100vw;
  height: 100vh;
}
#mainCon{
  width: 100vw;
  height: 100vh;
}
#title{
  height: 60px;
  line-height: 60px;
  text-align: center;
  background-color: #b3bfcd;
  font-family: 幼圆;
}
#menu{
  height: 60px;
  border-top: 1px #d1d4d7 solid;
  display: flex;
  justify-content: space-around;
  flex-direction: row;
  background-color: #b3bfcd;
}
.menu_item{
  height: 60px;
  width: 60px;
  display: flex;
  justify-content: center;
  flex-direction: column;
  text-align: center;
}

#main_main{
  background-color: #dcdcdc;
  padding: 0;
}

.icon_item{
  font-size: 20px;
  margin-left: 16px;
}
</style>
