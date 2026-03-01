import request from '@/utils/request.ts'
import type {APIKey} from "@/types";

const apiUrl = '/api/v1/app'

// 创建API接口
export const CreateApi = (form: object): any => request.post(apiUrl + '/', form)

// 更新API接口
export const UpdateApi = (id: number, form: object): any => request.put(apiUrl + '/' + id, form)

// 获取API详细信息
export const GetApiInfo = (id: number): any => request.get(apiUrl + '/' + id)

// 删除API接口
export const DeleteApi = (id: number): any => request.delete(apiUrl + '/' + id)

// 获取API列表
export const GetApiList = (page: number = 1, pageSize: number = 30): any => request.get(apiUrl + '/list', {
    params: {
        page: page,
        pageSize: pageSize
    }
})

// 搜索API接口
export const SearchApi = (keywords: string, page: number = 1, pageSize: number = 30): any => request.get(apiUrl + '/search', {
    params: {
        keywords: keywords,
        page: page,
        pageSize: pageSize
    }
})

// 用户接口密钥列表
export const GetUserApiKeyList =
    (userid: number = 0, page: number = 1, pageSize: number = 30): any =>
        request.get(apiUrl + '/key/list/' + userid, {
            params: {
                page: page,
                pageSize: pageSize,
            }
        })

// 创建API密钥
export const CreateApiKey = (form: APIKey): any => request.post(apiUrl + '/key/' + form.api_id, form)

// 获取API密钥详细信息
export const GetApiKeyInfo = (key: string): any => request.get(apiUrl + '/key/' + key)

// 更新API密钥
export const UpdateApiKey = (key: string, form: APIKey): any => request.put(apiUrl + '/key/' + key, form)

// 删除API密钥
export const DeleteApiKey = (key: string): any => request.delete(apiUrl + '/key/' + key)
