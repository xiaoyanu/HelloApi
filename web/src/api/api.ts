import request from '@/utils/request'
import {HelloAPIConfig} from "@/config/config.ts";

const apiUrl = '/api/v1/app'

// 创建API接口
export const CreateApi = (form: object): any => request.post(apiUrl + '/', form)

// 编辑API接口
export const UpdateApi = (form: object): any => request.put(apiUrl + '/', form)
