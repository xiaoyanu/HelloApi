import { createApp } from 'vue'
import App from '@/App.vue'
import router from '@/router'
import pinia from '@/stores'
import '@/assets/css/main.css'


// Vue 应用实例
const app = createApp(App)
app.use(pinia)
app.use(router)
app.mount('#app')