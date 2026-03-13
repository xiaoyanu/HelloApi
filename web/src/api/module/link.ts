import request from '@/utils/module/request'

const apiUrl = '/api/v1/link'

// 获取友链列表
export const GetLinkList =
    (page: number = 1, pageSize: number = 30, desc: number): any => request.get(apiUrl + '/', {
        params: {
            page,
            pageSize,
            desc
        }
    })

// 搜索友链列表
export const GetLinkListSearch = (keywords: string, pageSize: number, page: number): any =>
    request.get(apiUrl + '/Search', {
        params: {
            keywords,
            pageSize,
            page,
        }
    })

// 新增友链
export const CreateLink = (form: object): any => request.post(apiUrl + '/', form)

// 更新友链
export const UpdateLink = (id: number, form: object): any => request.put(apiUrl + '/' + id, form)

// 删除友链
export const DeleteLink = (id: number): any => request.delete(apiUrl + '/' + id)
