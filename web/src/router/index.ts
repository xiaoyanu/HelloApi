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
                },
                {
                    path: 'search/:keywords', // 默认首页
                    component: () => import('@/views/home/SearchPage.vue')
                },
                {
                    path: 'info/:id', // 默认首页
                    component: () => import('@/views/home/InfoPage.vue')
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
                },
                {
                    path: 'api', // API管理页 /admin/api
                    component: () => import('@/views/admin/ApiPage.vue')
                },
                {
                    path: 'key', // 密钥管理页 /admin/key
                    component: () => import('@/views/admin/KeyPage.vue')
                },
                {
                    path: 'user', // 用户个人资料页面 /admin/user
                    component: () => import('@/views/admin/UserPage.vue')
                },
                {
                    path: 'manage', // 用户管理页 /admin/manage
                    component: () => import('@/views/admin/ManageUserPage.vue')
                },
                {
                    path: 'stat', // 数据概括页 /admin/stat
                    component: () => import('@/views/admin/StatPage.vue')

                },
                {
                    path: 'doc', // 文档页 /admin/doc
                    component: () => import('@/views/admin/DocPage.vue')
                }
            ]
        },
        {
            path: '/admin/login', // 登录页 /admin/login，不使用layout
            component: () => import('@/views/LoginPage.vue')
        },
        {
            path: '/:pathMatch(.*)*', // 404 页面不存在
            component: () => import('@/views/NotFound.vue')
        }
    ],
})

export default router
