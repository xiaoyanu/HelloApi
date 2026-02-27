// 用于获取用户插件列表
export interface AppList {
    id: number;
    title: string;
    type: number;
    // 接口状态 0正常 1异常 2维护
    status: number;
    created: number;
    user_id: number;
    // 显示状态 0正常 1拒绝 2审核中
    view_status: number;
}

// 用于编辑插件/创建插件
export interface App {
    // 接口标题
    title: string;
    // 接口副标题
    smallTitle: string;
    // 接口状态 0正常 1异常 2维护
    status: number;
    // 接口类型 0免费 1收费
    type: number;
    // 接口URL
    url: string;
    // 接口请求类型 GET0 POST1 PUT2 DELETE3
    sendType: number;
    // 接口返回类型
    returnType: string;
    // 接口返回内容
    returnContent: string;
    // 接口请求参数
    params: Param[];
    // 显示状态 0正常 1拒绝 2审核中
    view_status: number;
}

interface Param {
    // 参数名称
    name: string;
    // 是否必填 0否 1是
    required: number;
    // 参数类型
    type: string;
    msg: string;
}

// 用于刷新用户信息，允许{}
export interface User {
    id?: number;
    mode?: number;
    name?: string;
    mail?: string;
    nick?: string;
}