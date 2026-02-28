import {createApp} from 'vue'
import App from '@/App.vue'
import router from '@/router'
import pinia from '@/stores'
import '@/assets/css/main.css'
import ElementPlus from 'element-plus'
import zhCn from 'element-plus/es/locale/lang/zh-cn'
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

VMdEditor.use(vuepressTheme);

// Vue 应用实例
const app = createApp(App)
app.use(pinia)
app.use(router)
app.use(VMdEditor)
app.use(ElementPlus, {
    locale: zhCn,
})
app.mount('#app')