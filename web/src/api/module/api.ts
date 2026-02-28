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