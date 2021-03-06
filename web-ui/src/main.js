import { createApp } from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import store from './store'
import AMapAPILoader from "vue-amap/src/lib/services/lazy-amap-api-loader";

createApp(App).use(store,AMapAPILoader,router).use(router).mount('#app')
