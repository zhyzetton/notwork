import { createApp } from 'vue'
import './style.css'
import 'remixicon/fonts/remixicon.css'
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(router) // 注册路由
app.mount('#app') // 挂载app