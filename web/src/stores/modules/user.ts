import {defineStore} from 'pinia'
import {ref} from 'vue'
import {GetUserInfo} from "@/api";
import type {User} from "@/types";



// 用户模块
export const useUserStore = defineStore(
    'HelloAPI-User',
    () => {
        const token = ref('')
        const setToken = (newToken: string) => {
            token.value = newToken
        }
        const removeToken = () => {
            token.value = ''
        }

        const user = ref<User>({})
        const setUser = (obj: User) => {
            user.value = obj
        }

        // 刷新用户信息
        const refreshUser = async () => {
            // 发送请求获取用户信息
            const res = await GetUserInfo()
            if (res.data.code === 200) {
                setUser(res.data.data)
            }
        }

        // 移除用户信息
        const removeUser = () => {
            setUser({})
        }

        // 移除所有信息
        const removeAll = () => {
            removeToken()
            removeUser()
        }

        return {
            token,
            setToken,
            removeToken,
            user,
            setUser,
            refreshUser,
            removeUser,
            removeAll,
        }
    },
    {
        persist: true,
    },
)