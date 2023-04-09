# 简介
采用拟态风格，简约的Api管理程序，无需数据库~
- 支持正常、维护、未公开、收费四种状态
- 自适应PC、PE
- 轻量化部署

# 截图
![Home](https://user-images.githubusercontent.com/61397705/230703480-35ae55b9-f0d8-4d16-9a7a-0d0266a56315.png)
------
![Info](https://user-images.githubusercontent.com/61397705/230703493-93169f71-9d59-4d22-be0d-e81f228d7dd0.png)

# Demo演示站

https://api.zxz.ee



# 使用说明

所有的API在 **main.json** 这个文件中编辑

采用的Json形式保存你的API，并用于展示

我在main.json中写了几个例子，方便理解



目录：api/tongji，此API请勿删除或更改名称，否则将会影响API调用统计。

在自己的API中顶部加入代码，即可开始统计API总体调用次数

（记得把域名改成API管理站点的域名）

```php
file_get_contents('http://域名/api/tongji/?t=1');
```



### 相关参数说明：

> title - 网站标题
>
> ym - 网站域名（无需带http或https）
>
> logo - 网站Logo
>
> icon - 网站图标
>
> footer - 页脚版权信息
>
> qqurl - 问题反馈链接
>
> 
>
> **api** 所有的接口都存在这里面：
>
> name - 接口名字
>
> now - 接口状态（正常/维护/未公开/收费）
>
> txt - 简短描述
>
> url - 接口网址
>
> get - 请求方式
>
> out - 返回类型
>
> count - 参数数量
>
> %-name 参数名
>
> %-ok 是否必填（是/否）
>
> %-type 参数格式
>
> %-main 参数说明
>
> 注意：%代表第几个参数，参数数量由count决定，一定要与之对应，否则将会出错，我在里面写了个多个参数例子，一看就能明白了
>
> fh - API返回内容，可以使用HTML代码

# 安全相关

如果你不想自己的API配置参数被别人发现，你可以通过重命名或修改main.json文件所在位置来达到隐藏的效果。

不过重命名或移动位置后，请及时修改info.php、index.php中第二行的文件路径，为与之对应的路径
