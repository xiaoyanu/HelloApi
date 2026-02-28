import request from '@/utils/request.ts'
import {HelloAPIConfig} from "@/config/config.ts";
import type {selectFormApi} from "@/types";

const apiUrl = '/api/v1/user'

// 获取验证码
export const GetCaptchaUrl = (): string => HelloAPIConfig.serverURL + apiUrl + '/Captcha' + '?' + new Date().getTime()

// 用户注册
export const Register = (form: object): any => request.post(apiUrl + '/Register', form)

// 用户登录
export const Login = (form: object): any => request.post(apiUrl + '/Login', form)

// 用户信息
export const GetUserInfo = (): any => request.get(apiUrl + '/')

// 用户接口列表
export const GetUserAppList =
    (userid: number = 0, page: number = 1, pageSize: number = 30): any => request.get(apiUrl + '/AppList', {
        params: {
            ...(userid != 0 && {id: userid}),
            page: page,
            pageSize: pageSize,
        }
    })

// 用户接口列表搜索
export const UserAppListSearch = (form: selectFormApi, pageSize: number, page: number): any =>
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
