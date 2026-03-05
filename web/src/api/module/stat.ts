import request from '@/utils/module/request'

const apiUrl = '/api/v1/stat'

// 获取指定类型数据
export const GetStat = (type: string): any => request.post(apiUrl + '/', {type})
