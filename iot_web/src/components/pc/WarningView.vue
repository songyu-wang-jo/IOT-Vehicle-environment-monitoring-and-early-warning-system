<template>
  <div id="warningView">
    <div id="warn_header">
      fad
    </div>
    <div id="warn_main">
      <div class="warningItem" v-for="item in warnData">
        <div class="deviceInfo">
          <div class="deviceName">{{JSON.parse(item).deviceName}}</div>
          <div class="messageTime">{{JSON.parse(item).datetime}}</div>
        </div>
        <div class="warnDesc">
          {{JSON.parse(item).warnDesc}}
          {{JSON.parse(item).warnType}}
        </div>
        <div class="warnOperation">
          <el-button>fasd</el-button>
          <el-button>fasd</el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>

function initWarnHeight() {
  let availHeight = document.body.clientHeight;
  let main_main = document.getElementById("warn_main");
  main_main.style.height = availHeight - 180 + 'px'
}

window.onresize = () => {
  initWarnHeight()
}

window.onload = () => {
  initWarnHeight()
}

export default {
  name: "warningView",
  data() {
    return {
      pureWarnData: this.$store.state.webSockMessages
    }
  },
  methods: {},
  mounted() {
    initWarnHeight();
  },
  computed: {
    warnData(){
      return this.pureWarnData.reverse()
    }
  }
}
</script>

<style scoped>

#warningView {

}

#warn_header {
  height: 60px;
  background-color: #225250;
}

#warn_main {
  overflow: auto;
}

.deviceInfo {
  width: 90%;
  height: 40px;
  margin: 0 auto;
}

.deviceName{
  float: left;
}

.messageTime{
  float: right;
}

.warnDesc{
  width: 90%;
  height: 70px;
  margin: 0 auto;
}

.warnOperation{
  width: 90%;
  height: 40px;
  margin: 0 auto;
  text-align: right;
}

.warningItem {
  margin: 20px 100px;
  height: 150px;
  border-radius: 50px;
  border: 2px slategrey solid;
}

</style>
