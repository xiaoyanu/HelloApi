// 默认配置
const HelloAPIConfig = {

    // 后端接口地址
    serverURL: 'http://localhost:8080',

    // 前端域名
    domain: 'api.zxz.ee',

    // 网站配置
    website: {
        index: {
            // 每页显示数量，影响范围：前台页面
            pageSize: 12,
        },
        admin: {
            // 每页显示数量，影响范围：后台页面
            pageSize: 10,
            // 水印
            watermark: {
                // 是否显示水印 [true/false]
                view: false,
                // 水印内容，留空则显示用户昵称
                content: '',
            },
        },
        // 页脚信息
        footer: `Powered by <a href="https://github.com/xiaoyanu/HelloAPI" target="_blank">HelloAPI</a>`,
    }
}

export {HelloAPIConfig}
