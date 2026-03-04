import request from '@/utils/module/request'
import {HelloAPIConfig} from "@/config/config.ts";
import type {SelectFormApi} from "@/types";

const apiUrl = '/api/v1/user'

// 获取验证码
export const GetCaptchaUrl = (): string => HelloAPIConfig.serverURL + apiUrl + '/Captcha' + '?' + new Date().getTime()

// 用户注册
export const Register = (form: object): any => request.post(apiUrl + '/Register', form)

// 用户登录
export const Login = (form: object): any => request.post(apiUrl + '/Login', form)

// 用户信息
export const GetUserInfo = (id: number = 0): any => request.get(apiUrl + '/', {
    params: {
        ...(id != 0 && {id: id})
    }
})

// 用户接口列表
export const GetUserAppList =
    (page: number = 1, pageSize: number = 30): any => request.get(apiUrl + '/AppList', {
        params: {
            page: page,
            pageSize: pageSize,
        }
    })

// 用户接口列表搜索
export const UserAppListSearch = (form: SelectFormApi, pageSize: number, page: number): any =>
    request.get(apiUrl + '/AppList/Search', {
        params: {
            keywords: form.keywords,
            type: form.type,
            status: form.status,
            view_status: form.view_status,
            pageSize: pageSize,
            page: page,
        }
    })

// 更新用户昵称
export const UpdateUserNick = (nick: string, id: number = 0): any => request.put(apiUrl + '/nick', {
    nick,
}, {
    params: {
        ...(id != 0 && {id: id})
    }
})

// 更新用户邮箱
export const UpdateUserMail = (mail: string, id: number = 0): any => request.put(apiUrl + '/mail', {
    mail,
}, {
    params: {
        ...(id != 0 && {id: id})
    }
})


// 更新用户密码
export const UpdateUserPassword = (oldPassword: string, newPassword: string, id: number = 0): any => request.put(apiUrl + '/password', {
    oldPassword, newPassword
}, {
    params: {
        ...(id != 0 && {id: id})
    }
})

// 获取用户密钥
export const GetUserKey = (): any => request.get(apiUrl + '/key')

// 重置用户密钥
export const RestUserKey = (userId: number): any => request.put(apiUrl + '/key/' + userId)

// 获取用户列表
export const GetUserList = (page: number = 1, pageSize: number = 30): any => request.get(apiUrl + "/list", {
    params: {
        page,
        pageSize
    }
})

// 获取用户列表搜索
export const UserListSearch = (keywords: string, mode: number, page: number = 1, pageSize: number = 30): any =>
    request.get(apiUrl + "/list/Search", {
        params: {
            keywords,
            mode,
            page,
            pageSize
        }
    })

// 删除用户
export const DeleteUser = (id: number): any => request.delete(apiUrl + '/' + id)

// 设置用户密码
export const SetUserPassword = (id: number, password: string): any => request.put(apiUrl + '/setPassword', {
    password
}, {
    params: {
        id
    }
})

// 设置用户权限
export const SetUserMode = (id: number, mode: number): any => request.put(apiUrl + '/setMode', {
    mode
}, {
    params: {
        id
    }
})