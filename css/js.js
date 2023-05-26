Array.prototype.remove = function (value) {
    var index = this.indexOf(value);
    if (index > -1) this.splice(index, 1);
};

(function (global, setting) {
    var KStyle = function (a, b) {
        return KStyle.fn.init(a, b);
    };

    KStyle.fn = KStyle.prototype = {
        construtor: KStyle,
        init: function (a, b) {
            a = KStyle.selectAll(a);

            a.each = function (fn) {
                return KStyle.each(a, fn);
            };

            a.image = function () {
                return KStyle.image(a);
            };

            a.lazy = function (bg) {
                return KStyle.lazy(a, bg);
            };

            a.scrollTo = function (offset) {
                return KStyle.scrollTo(a, offset);
            };

            a.empty = function () {
                return KStyle.each(a, function (item) {
                    KStyle.empty(item);
                });
            };

            return a;
        },
    };

    // 批量处理
    KStyle.each = function (data, fn) {
        for (var i = 0; i < data.length; i++) {
            fn(data[i], i, data);
        }
    };

    // 创建对象
    KStyle.create = function (tag, prop) {
        var obj = document.createElement(tag);

        if (prop) {
            if (prop.id) obj.id = prop.id;
            if (prop.src) obj.src = prop.src;
            if (prop.href) obj.href = prop.href;
            if (prop.class) obj.className = prop.class;
            if (prop.text) obj.innerText = prop.text;
            if (prop.html) obj.innerHTML = prop.html;

            if (prop.child) {
                if (prop.child.constructor === Array) {
                    KStyle.each(prop.child, (i) => {
                        obj.appendChild(i);
                    });
                } else {
                    obj.appendChild(prop.child);
                }
            }

            if (prop.attr) {
                if (prop.attr.constructor === Array) {
                    KStyle.each(prop.attr, (i) => {
                        obj.setAttribute(i.name, i.value);
                    });
                } else if (prop.attr.constructor === Object) {
                    obj.setAttribute(prop.attr.name, prop.attr.value);
                }
            }

            if (prop.parent) prop.parent.appendChild(obj);
        }

        return obj;
    };

    // 选择对象
    KStyle.select = function (obj) {
        switch (typeof obj) {
            case "object":
                return obj;
                break;
            case "string":
                return document.querySelector(obj);
                break;
        }
    };

    KStyle.selectAll = function (obj) {
        switch (typeof obj) {
            case "object":
                return obj;
                break;
            case "string":
                return document.querySelectorAll(obj);
                break;
        }
    };

    // 清空子元素
    KStyle.empty = function (obj) {
        while (obj.firstChild) {
            obj.removeChild(obj.firstChild);
        }
    };

    // 弹窗
    var notice = {
        wrap: KStyle.create("notice"),
        list: [],
    };

    KStyle.notice = function (content, attr) {
        var item = KStyle.create("div", {
            class: "ks-notice",
            html: "<span class='content'>" + content + "</span>",
            parent: notice.wrap,
        });

        notice.list.push(item);

        if (!document.querySelector("body > notice"))
            document.body.appendChild(notice.wrap);

        if (attr && attr.time) {
            setTimeout(notice_remove, attr.time);
        } else {
            var close = KStyle.create("span", { class: "close", parent: item });

            close.onclick = notice_remove;
        }

        if (attr && attr.color) {
            item.classList.add(attr.color);
        }

        function notice_remove() {
            item.classList.add("remove");
            notice.list.remove(item);

            setTimeout(function () {
                try {
                    notice.wrap.removeChild(item);
                    item = null;
                } catch (err) { }

                if (
                    document.querySelector("body > notice") &&
                    notice.list.length === 0
                ) {
                    document.body.removeChild(notice.wrap);
                }
            }, 300);
        }
    };

    global.ks = KStyle;
    console.log(
        "%c HelloApi %c https://github.com/xiaoyanu/HelloApi %c\n 一个开源的Api管理系统 ",
        "color: #fff; margin: 1em 0; padding: 5px 0; background: #3498db;",
        "margin: 1em 0; padding: 5px 0; background: #efefef;",
        "color: #fff; margin: 1em 0; padding: 5px 0; background: #3498db;"
    );
})(window);
function copyText() {
    var text = document.getElementById("text").innerHTML;
    var input = document.getElementById("input");
    input.value = text.replace(/&amp;/g, "&");
    input.select(); //选中文本
    document.execCommand("copy");
    ks.notice("复制成功", {
        time: 1500,
    });
}
