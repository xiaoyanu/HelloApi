import {MD5} from 'crypto-js'

/**
 * 获取当前年份
 */
export const getNowYear = () => {
    return new Date().getFullYear()
}

/**
 * 返回随机一条搜索结果提示
 */
export const getSearchNullMessage = () => {
    const messages = [
        '啥也不填你在搜什么？兄弟！',
        '(⊙ˍ⊙)？你在干什么？填了再搜呀！',
        '(oﾟvﾟ)ノ你到底要搜啥？不填我怎么知道',
    ]
    return messages[Math.floor(Math.random() * messages.length)]
}

/**
 * 将邮箱转换为 Gravatar 哈希值
 * @param email 用户邮箱
 * @returns MD5 哈希字符串
 */
export const getGravatarHash = (email: string): string => {
    const cleanEmail = email.trim().toLowerCase();
    return MD5(cleanEmail).toString();
};
