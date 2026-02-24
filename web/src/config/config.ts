import {getNowYear} from '@/utils/more'
// 默认配置
const HelloAPIConfig = {

    // 后端接口地址
    serverURL: 'http://localhost:8080',

    // 前端域名
    domain: 'api.zxz.ee',

    // 网站配置
    website: {
        footer: `© ${new Date().getFullYear()} All rights reserved. | Powered By HelloAPI`,
    }
}

export {HelloAPIConfig}
