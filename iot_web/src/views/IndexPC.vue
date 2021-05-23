<template>
  <div id="indexPC">
    <el-container>
      <el-aside :width="this.isCollapse? '65px': '260px'" id="aside">
        <el-container>
          <el-header id="aside_header">
            <svg-icon icon-class="car" style="font-size: 30px; padding-top: 15px"/>
          </el-header>
          <el-main id="aside_main">
            <el-menu
                default-active="2"
                class="el-menu-vertical-demo"
                background-color="#267ca1"
                text-color="#fff"
                active-text-color="#ffd04b"
                :collapse="isCollapse"
                id="asideMenu">
              <el-menu-item index="1" @click="changeView('/pcIndex/dataView')" class="el_menu_item">
                <svg-icon icon-class="dataView"></svg-icon>
                <span slot="title">数据查看</span>
              </el-menu-item>
              <el-menu-item index="2" @click="changeView('/pcIndex/warningView')" class="el_menu_item">
                <el-badge :value="$store.state.webSockMessages.length===0 ? '': $store.state.webSockMessages.length">
                  <svg-icon icon-class="warningView"></svg-icon>
                </el-badge>
                <span slot="title">异常记录</span>
              </el-menu-item>
              <el-menu-item index="3" @click="changeView('/pcIndex/deviceManagerView')" class="el_menu_item">
                <svg-icon icon-class="deviceManagerView"></svg-icon>
                <span slot="title">设备管理</span>
              </el-menu-item>
            </el-menu>
          </el-main>
        </el-container>
      </el-aside>
      <el-container>
        <el-header id="main_header">
          <el-row>
            <el-col :span="4" style="height: 60px">
              <el-button type="text" @click="()=>{this.isCollapse = !this.isCollapse}"
                         style="font-size: 20px; padding-left: 20px;">
                <svg-icon v-show="isCollapse" icon-class="menu_open"></svg-icon>
                <svg-icon v-show="!isCollapse" icon-class="menu_close"></svg-icon>
              </el-button>
            </el-col>
            <el-col :span="16" style="text-align: center; color: #893d3d; font-size: 30px; font-weight: bolder">
              车内环境监测平台
            </el-col>
            <el-col :span="4">
              <el-tooltip content="login out!" placement="bottom" effect="light">
                <el-button round style="margin-top: 5px" size="mini" @click="loginOut">
                  <svg-icon icon-class="admin"></svg-icon>
                </el-button>
              </el-tooltip>
              <span style="color: darkgray; margin-left: 10px">admin</span>
            </el-col>
          </el-row>
        </el-header>
        <el-main id="main_main">
          <router-link to="/index/messageView"/>
          <router-view/>
        </el-main>
        <el-footer id="main_footer" style="text-align: center">
          <span>
            版权所有：SongyuWong<br/>邮箱：songyuwong@163.com
          </span>
        </el-footer>
      </el-container>
    </el-container>
  </div>
</template>

<script>

function initMainHeight() {
  let availHeight = document.body.clientHeight;
  let main_main = document.getElementById("main_main");
  main_main.style.height = availHeight - 120 + 'px'
}

window.onresize = () => {
  initMainHeight()
}

window.onload = () => {
  initMainHeight()
}

export default {
  name: "indexPC",
  components: {},
  data: () => {
    return {
      isCollapse: true,
      currentPage: '',
      sessionId: '',
      webSock: null
    }
  },
  methods: {
    changeView(routerName) {
      if (this.currentPage !== routerName) {
        this.$router.push({path: routerName})
        this.currentPage = routerName
      }
    },
    loginOut(){
      this.$store.commit('cleanUserToken')
      this.$router.replace({path: "/"})
    },
    initWebSocket(){ //初始化webSocket
      if (WebSocket){
        this.webSock = new WebSocket("ws://127.0.0.1:8787/iot/websocket/" + this.sessionId);
        // this.webSock = new WebSocket("ws://localhost:8787/iot/websocket/"+this.$store.state.sessionId);
        this.webSock.onmessage = this.webSocketOnMessage;
        this.webSock.onopen = this.webSocketOnOpen;
        this.webSock.onerror = this.webSocketOnError;
        this.webSock.onclose = this.webSocketClose;
      }else {
        this.$message.error("该浏览器不支持websocket！！！实时预警功能不支持请升级浏览器内核版本")
      }
    },
    webSocketOnOpen(){ //连接建立之后执行send方法发送数据
      this.$message.success("异常信息实时获取中")
    },
    webSocketOnError(){//连接建立失败重连
      this.initWebSocket();
    },
    webSocketOnMessage(e){ //数据接收
      this.$store.commit("setWebSockMessages",e.data)
    },
    webSocketSend(Data){//数据发送
      this.webSock.send(Data);
    },
    webSocketClose(e){  //关闭
      console.log('断开连接'+e.data);
    },
  },
  mounted() {
    this.changeView("/pcIndex/dataView")
  },
  created() {
    this.sessionId = new Date().getMilliseconds().toString()
    this.initWebSocket();
  },
  beforeDestroy() {
    this.webSock.close() //离开路由之后断开websocket连接
  }
}
</script>

<style scoped>

#indexPC {
  width: 100vw;
  height: 100vh;
}

#aside {
  background-color: #267ca1;
  height: 100vh
}

#aside_header {
  height: 60px;
  line-height: 60px;
  font-size: 16px;
  color: white;
  font-weight: bolder;
  text-align: center;
  padding: 0;
}

#aside_main {
  padding: 0;
}

#asideMenu {
}

.el_menu_item {
  font-size: 16px;
}

.el_menu_item span {
  padding-left: 15px;
}

#main_header {
  border-bottom: 2px #d9cdcd solid;
  height: 60px;
  line-height: 60px;
  padding: 0;
}

#main_main {
  padding: 0;
}

#main_footer {
  height: 60px;
  border-top: 1px #b3bfcd solid;
}
</style>
