import axios from 'axios'
import {HelloAPIConfig} from '@/config/config'
import router from '@/router'

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
        // 如果登录了则携带token
        // const userStore = useUserStore()
        // if (userStore.token) {
        //     config.headers.Authorization = 'Bearer ' + userStore.token
        // }
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

        // code 200 原封不动返回
        if (res.data.code === 200) {
            return res
        }

        // code 401 登录过期/无效


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