<?php
// 这是路径
$temp = file_get_contents("main.json");
if ($temp == "") {
    echo "核心文件Json未找到，可能是文件路径没有正确填写或文件名称您更改，请修正index.php、info.php文件的第一行代码的路径为正确路径。";
    die;
}
$nr = json_decode($temp, true);
?>
<!-- 
    灵感来源：星之阁API
	作者：小言u
	Github：https://github.com/xiaoyanu/HelloApi
-->
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="<?php echo $nr['ms'] ?>">
    <meta name="keywords" content="<?php echo $nr['keywords'] ?>">
    <title><?php echo $nr['title'] ?></title>
    <link rel="icon" type="image/ico" href="<?php echo $nr['icon'] ?>">
    <link rel="stylesheet" href="css/index.css">
    <script src="css/js.js"></script>
</head>

<body>
    <div id="head">
        <img id="logo" src="<?php echo $nr['logo'] ?>">
        <a id="title" href="<?php echo '//' . $nr['ym'] ?>"><?php echo $nr['title'] ?></a>
        <img id="tj" src="./api/tongji/?t=2">
        <div id="head-right">
            <a id="wt" href="<?php echo $nr['qqurl'] ?>">问题反馈</a>
        </div>
    </div>
    <div id="banner">
        <h1><?php echo $nr['title'] ?></h1>
    </div>
    <div id="main">
        <div id="center">
            <?php
            $num = count($nr['api']) - 1;
            $i = 0;
            while ($num >= $i) {

                //判断是否是维护状态
                switch ($nr['api'][$i]['now']) {
                    case "未公开":
                        $now = '<span class="zt" style="background-color: #0288D1">' . $nr['api'][$i]['now'] . '</span>';
                        break;
                    case "维护":
                        $now = '<span class="zt" style="background-color: #f56c6c">' . $nr['api'][$i]['now'] . '</span>';
                        break;
                    case "收费":
                        $now = '<span class="zt" style="background-color: #FF9800">' . $nr['api'][$i]['now'] . '</span>';
                        break;
                    default:
                        $now = '<span class="zt" style="background-color: #67c23a">' . $nr['api'][$i]['now'] . '</span>';
                        break;
                }

                //辨别链接
                switch ($nr['api'][$i]['now']) {
                    case "未公开":
                        $url = '<a class="url" onclick="ks.notice(\'' . $nr['api'][$i]['name'] . ' 未公开\', {time: 1500});">查看</a>';
                        break;
                    case "维护":
                        $url = '<a class="url" onclick="ks.notice(\'' . $nr['api'][$i]['name'] . ' 维护中\', {time: 1500});">查看</a>';
                        break;
                    default:
                        $url = '<a target="_blank" href="//' . $nr['ym'] . '/info.php?id=' . $i . '" class="url">查看</a>';
                        break;
                }

                echo
                '<div class="apibox">
                ' . $now . '
                <div class="title">
                <p>' . $nr['api'][$i]['name'] . '</p>
                </div>
                <div class="text">
                <p>' . $nr['api'][$i]['txt'] . '</p>
                </div>
                ' . $url . '</div>';
                $i++;
            }
            ?>

        </div>
        <div id="footer">
            <p><?php echo $nr['footer']; ?></p>
        </div>
        <div id="xf">
            <a ks-tag="left" ks-text="搜索" id="searchButton">
                <svg t="1686984175067" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2531" id="mx_n_1686984175067" width="56" height="56">
                    <path d="M949.76 884.3264a88.68864 88.68864 0 0 1-25.64096 62.67904 87.14752 87.14752 0 0 1-123.76576 0.16896l-164.29568-160.87552a382.4128 382.4128 0 0 1-26.43968 12.6208 382.83776 382.83776 0 0 1-300.032 0 383.38048 383.38048 0 0 1-122.48064-83.39968 391.296 391.296 0 0 1 0-550.36928 384.56832 384.56832 0 0 1 627.55328 123.648 391.00416 391.00416 0 0 1-40.704 376.57088l150.32832 156.56448a88.576 88.576 0 0 1 25.47712 62.39232z m-153.6512-444.04736c0-186.33216-150.41536-337.92-335.30368-337.92s-335.32928 151.6032-335.32928 337.92S275.89632 778.24 460.8 778.24s335.3088-151.64928 335.3088-337.96096z m-503.61344 168.90368a240.45568 240.45568 0 0 1 0-337.73568l34.63168 40.07424a183.46496 183.46496 0 0 0 0 257.50528z" p-id="2532" data-spm-anchor-id="a313x.7781069.0.i5" class="selected" fill="#61677C"></path>
                </svg>
            </a>
            <a ks-tag="left" ks-text="返回顶部" href="#tj">
                <svg t="1680926673355" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2320">
                    <path d="M188.274526 257.534448l0-97.428983 645.061528 0 0 97.428983L188.274526 257.534448zM252.732583 515.366677l255.82655-226.355331 260.329097 226.355331-192.371332 0 0 192.362122 0 161.89625-130.911562 0 0-354.258373L252.732583 515.366677z" p-id="2321" fill="#61677C"></path>
                </svg>
            </a>
        </div>
    </div>
    <!-- 搜索盒子 -->
    <div id="main-ss" style="z-index: -9999;">
        <center>
            <h1>🔍搜索接口</h1>
        </center>
        <form action="ss.php" method="GET">
            <input id="ss" type="text" name="s" placeholder="请输入关键字...">
            <center>
                <input id="g-ss" type="submit" value="搜索">
            </center>
        </form>
    </div>

    <script>
        // 获取搜索按钮元素
        const searchButton = document.querySelector('#searchButton');
        // 获取弹出的DIV元素
        const popupDiv = document.querySelector('#main-ss');
        // 添加点击事件监听器
        searchButton.addEventListener('click', () => {
            // 显示弹出的DIV
            popupDiv.style.zIndex = '9999';
            popupDiv.classList.add('show');
            // 创建背景层
            const backdrop = document.createElement('div');
            backdrop.classList.add('backdrop');
            // 将背景层插入到弹出DIV之前
            popupDiv.parentNode.insertBefore(backdrop, popupDiv);
            // 延迟添加动画类以触发动画效果
            setTimeout(() => {
                backdrop.style.opacity = '1';
                popupDiv.style.opacity = '1';
                popupDiv.style.transform = 'translate(-50%, -50%)';
            }, 10);

            // 添加点击事件监听器到背景层
            backdrop.addEventListener('click', () => {
                popupDiv.classList.remove('show');
                removeBackdrop();
                popupDiv.style.zIndex = '-9999';
            });
        });

        // 删除背景层
        function removeBackdrop() {
            const backdrop = document.querySelector('.backdrop');
            if (backdrop) {
                backdrop.style.opacity = '0';
                popupDiv.style.opacity = '0';
                popupDiv.style.transform = 'translate(-50%, -55%)';
                // 延迟移除背景层以等待动画完成
                setTimeout(() => {
                    backdrop.remove();
                }, 300);
            }
        }
    </script>

</body>

</html>
<?php die; ?>
