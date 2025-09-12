import { createRouter, createWebHistory, type RouteRecordRaw } from 'vue-router'

// 路由 
const routes = [
    {
        path: '/',
        component: () => import('@/components/Layout/index.vue')
    }
] as RouteRecordRaw[]
const router = createRouter({
  history: createWebHistory(),
  routes: routes
})

export default router