import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    currentViewTitle: '数据查看',
    userToken: '',
    webSockMessages: []
  },
  mutations: {
    changeViewTitle(state,title){
      state.currentViewTitle = title
    },
    setUserToken(state,token){
      state.userToken = token
    },
    cleanUserToken(state){
      state.userToken = ''
    },
    setWebSockMessages(state, message){
      state.webSockMessages.push(message)
    },
    cleanWebSockMessages(state, message){
      state.webSockMessages.forEach((value, index, array)=>{
        console.log(value);
        console.log(index);
        console.log(array);
      })
    },
    cleanAllWebSockMessages(state){
      state.webSockMessages = []
    }
  },
  actions: {
  },
  modules: {
  }
})
