import {createRouter, createWebHistory} from 'vue-router'
import IndexLayout from "@/layouts/IndexLayout.vue";
import AdminLayout from "@/layouts/AdminLayout.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // 前台路由组
        {
            path: '/',
            component: IndexLayout,
            children: [
                {
                    path: '', // 默认首页
                    component: () => import('@/views/home/HomePage.vue')
                }
            ]
        },
        {
            path: '/admin',
            component: AdminLayout,
            children: [
                {
                    path: '', // 后台默认页 /admin
                    component: () => import('@/views/admin/AdminPage.vue')
                }
            ]
        }
    ],
})

export default router
