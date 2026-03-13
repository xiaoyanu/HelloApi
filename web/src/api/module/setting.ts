import request from '@/utils/module/request'

const apiUrl = '/api/v1/setting'

// 获取全局设置
export const GetSettingList = (): any => request.get(apiUrl + '/list')

// 获取指定参数
export const GetSettingValue = (value: string): any => request.get(apiUrl + '/' + value)

// 更改全局设置
export const UpdateSettingValue = (key: string, value: any): any => request.put(apiUrl + '/', {
    key,
    value
})