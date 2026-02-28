import {createRouter, createWebHistory} from 'vue-router'
import IndexLayout from "@/layouts/IndexLayout.vue";
import AdminLayout from "@/layouts/AdminLayout.vue";
import {useUserStore} from "@/stores";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        // 前台路由组
        {
            path: '/',
            component: IndexLayout,
            children: [
                {
                    name: 'Home',
                    path: '', // 默认首页
                    component: () => import('@/views/home/HomePage.vue')
                },
                {
                    name: 'Search',
                    path: 'search/:keywords', // 默认首页
                    component: () => import('@/views/home/SearchPage.vue')
                },
                {
                    name: 'Info',
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
                    name: 'Admin',
                    path: '', // 后台默认页 /admin
                    component: () => import('@/views/admin/AdminPage.vue')
                },
                {
                    name: 'AdminApi',
                    path: 'api', // API管理页 /admin/api
                    component: () => import('@/views/admin/ApiPage.vue')
                },
                {
                    name: 'AdminKey',
                    path: 'key', // 密钥管理页 /admin/key
                    component: () => import('@/views/admin/KeyPage.vue')
                },
                {
                    name: 'AdminUser',
                    path: 'user', // 用户个人资料页面 /admin/user
                    component: () => import('@/views/admin/UserPage.vue')
                },
                {
                    name: 'AdminManage',
                    path: 'manage', // 用户管理页 /admin/manage
                    component: () => import('@/views/admin/ManageUserPage.vue')
                },
                {
                    name: 'AdminStat',
                    path: 'stat', // 数据概括页 /admin/stat
                    component: () => import('@/views/admin/StatPage.vue')

                },
                {
                    name: 'AdminDoc',
                    path: 'doc', // 文档页 /admin/doc
                    component: () => import('@/views/admin/DocPage.vue')
                },
                {
                    name: 'AdminSetting',
                    path: 'setting', // 全局设置页 /admin/setting
                    component: () => import('@/views/admin/SettingPage.vue')
                }
            ]
        },
        {
            name: 'AdminLogin',
            path: '/admin/login', // 登录页 /admin/login，不使用layout
            component: () => import('@/views/LoginPage.vue')
        },
        {
            name: 'NotFound',
            path: '/:pathMatch(.*)*', // 404 页面不存在
            component: () => import('@/views/NotFound.vue')
        }
    ],
})

// 路由守卫
router.beforeEach((to) => {
    const userStore = useUserStore()
    const token = userStore.token

    // 已登录拦截到后台首页
    if (token !== '' && to.path == '/admin/login') {
        return '/admin'
    }

    // 匹配路径为/admin/*
    if (to.path.startsWith('/admin')) {
        // 移除路径末尾的斜杠
        const path = to.path.endsWith('/') ? to.path.slice(0, -1) : to.path
        if (path !== '/admin/login' && token === '') {
            ElMessage.warning('请先登录')
            return '/admin/login';
        }

        if (path == '/admin/manage' || path == '/admin/setting' || path == '/admin') {
            void userStore.refreshUser()
        }
    }
})
export default router
