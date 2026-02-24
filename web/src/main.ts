import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/css/main.css'
import pinia from './stores'

// Vue 应用实例
const app = createApp(App)
app.use(pinia)
app.use(router)
app.mount('#app')