import request from '@/utils/request.ts'

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
