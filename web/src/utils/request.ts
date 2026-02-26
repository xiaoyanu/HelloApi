import axios from 'axios'
import {HelloAPIConfig} from '@/config/config'
import {useUserStore} from "@/stores";
import router from "@/router"

const instance = axios.create({
    // URL 基础地址
    baseURL: HelloAPIConfig.serverURL,
    // 超时时间
    timeout: 10000,
    // 跨域请求时是否需要使用凭证
    withCredentials: true,
})

// 请求拦截器
instance.interceptors.request.use(
    (config) => {
        const userStore = useUserStore()

        // 如果登录了则携带token
        if (userStore.token) {
            config.headers.Authorization = 'Bearer ' + userStore.token
        }
        return config
    },
    (err) => {
        return Promise.reject(err)
    },
)

// 响应拦截器
instance.interceptors.response.use(
    (res) => {
        // HTTP状态码为2XX时
        const userStore = useUserStore()

        // code 200 原封不动返回
        if (res.data.code === 200) {
            return res
        }

        // code 401 登录过期/无效
        if (res.data.code === 401) {
            // 移除token和user信息
            userStore.removeAll()
            const currentPath = router.currentRoute.value.path
            if (currentPath.startsWith('/admin')) {
                // 提示登录过期
                ElMessage.error('登录过期，请重新登录')
                // 跳转到登录页
                void router.push({name: 'AdminLogin'})
            }
            return Promise.reject(res.data.msg || '登录过期')
        }

        // code 非200 提示错误信息，返回响应
        ElMessage.error(res.data.msg || '服务异常')
        return res
    },
    (err) => {
        // HTTP状态码非200 提示错误信息，返回响应
        ElMessage.error('服务异常：' + err.message)
        return Promise.reject(err)
    },
)

export default instance