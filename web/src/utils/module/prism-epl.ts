import Prism from 'prismjs';

// 向 Prism 注册易语言 (EPL) 高亮规则
Prism.languages.epl = {
    'comment': /'.*/,
    'string': {
        pattern: /"(?:[^"\\]|\\.)*"/,
        greedy: true
    },
    'keyword': /\.(?:版本|程序集|子程序|局部变量|全局变量|参数|常量|数据类型|支持库|DLL命令)/,
    'class-name': /(?:文本型|整数型|逻辑型|小数型|双精度小数型|字节型|短整数型|长整数型|日期时间型|子程序指针|变体型)\b/,
    'builtin': /(?:如果|如果真|判断|判断循环首|判断循环尾|计次循环首|计次循环尾|变量循环首|变量循环尾|跳出循环|到循环尾|返回|结束)\b/,
    'function': /[\u4e00-\u9fa5a-zA-Z0-9_]+(?=\s*\()/,
    'number': /\b\d+(?:\.\d+)?\b/,
    'operator': /[+\-*\/=<>!]+/,
    'punctuation': /[(),.]/
};

// 增加中文别名
Prism.languages['易语言'] = Prism.languages.epl;