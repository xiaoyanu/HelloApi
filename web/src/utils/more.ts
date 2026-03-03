import {MD5} from 'crypto-js'

const formatter = new Intl.NumberFormat('en-US');
/**
 * 判断是否为移动端
 * @returns {boolean} - 是否为移动端
 */
export const isMobile = window.matchMedia('(max-width: 768px)').matches

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

/**
 * 格式化数字，支持亿级单位，万级单位，千级单位，保留两位小数
 * @param num 要格式化的数字
 * @returns 格式化后的字符串
 */
export const formatNumber = (num: number): string => {
    if (num === 0) return '0';
    const isNegative = num < 0;
    const absNum = isNegative ? -num : num;
    let result: string;
    if (absNum >= 1e8) {
        const yi = absNum / 1e8;
        result = yi % 1 === 0
            ? `${yi}亿`
            : `${Math.floor(absNum / 1e6) / 100}亿＋`;
    } else if (absNum >= 1e4) {
        const wan = absNum / 1e4;
        result = wan % 1 === 0
            ? `${wan}万`
            : `${Math.floor(absNum / 100) / 100}万＋`;
    } else {
        result = formatter.format(absNum);
    }
    return isNegative ? `-${result}` : result;
};


export const formatDuration = (now: number, end: string): string => {
    const target = Date.parse(end);
    if (Number.isNaN(target) || target <= now) return '0天';
    const dNow = new Date(now);
    const dTarget = new Date(target);
    let years = dTarget.getFullYear() - dNow.getFullYear();
    let months = dTarget.getMonth() - dNow.getMonth();
    let days = dTarget.getDate() - dNow.getDate();
    if (days < 0) {
        months -= 1;
        days += new Date(dTarget.getFullYear(), dTarget.getMonth(), 0).getDate();
    }
    if (months < 0) {
        years -= 1;
        months += 12;
    }
    let str = '';
    if (years > 0) str += years + '年';
    if (months > 0) str += months + '月';
    if (days > 0 || str === '') str += days + '天';
    return str;
};

// 格式化原生日期字符串为易读格式
export const formatNativeDate = (dateStr: string) => {
    const d = new Date(dateStr);
    if (Number.isNaN(d.getTime())) return '-';
    const pad = (n: number) => n.toString().padStart(2, '0');
    return `${d.getFullYear()}年${pad(d.getMonth() + 1)}月${pad(d.getDate())}日 ${pad(d.getHours())}:${pad(d.getMinutes())}`;
};
