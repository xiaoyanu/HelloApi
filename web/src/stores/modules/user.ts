import {defineStore} from 'pinia'
import {ref} from 'vue'

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

        return {
            token,
            setToken,
            removeToken,
        }
    },
    {
        persist: true,
    },
)