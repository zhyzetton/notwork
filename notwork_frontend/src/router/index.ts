import {
  createRouter,
  createWebHistory,
  type RouteRecordRaw,
} from "vue-router";

// 路由
const routes = [
  {
    path: "/",
    redirect: "/blog/all",
    component: () => import("@/components/Layout/index.vue"),
    children: [
      {
        path: "/blog/:type",
        component: () => import("@/views/Blog/index.vue"),
      },
      {
        path: "/blog/detail/:id",
        component: () => import("@/views/BlogDetail/index.vue")
      },
      {
        path: "/write",
        component: () => import("@/views/Write/index.vue"),
      },
    ],
  },
] as RouteRecordRaw[];
const router = createRouter({
  history: createWebHistory(),
  routes: routes,
});

export default router;
