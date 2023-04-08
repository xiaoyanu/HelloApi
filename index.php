<?php
$temp = file_get_contents("main.json");
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
    <title><?php echo $nr['title'] ?></title>
    <link rel="icon" type="image/ico" href="<?php echo $nr['icon'] ?>">
    <link rel="stylesheet" href="css/index.css">
    <script src="css/js.js"></script>
</head>

<body>
    <div id="head">
        <img id="logo" src="<?php echo $nr['logo'] ?>">
        <a id="title" href="<?php echo '//' . $nr['ym'] ?>"><?php echo $nr['title'] ?></a>
        <img id="tj" src="//api.zxz.ee/api/tongji/?t=2">
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
                        $now = '<div class="zt" style="background-color: #0288D1">' . $nr['api'][$i]['now'] . '</div>';
                        break;
                    case "维护":
                        $now = '<div class="zt" style="background-color: #f56c6c">' . $nr['api'][$i]['now'] . '</div>';
                        break;
                    case "收费":
                        $now = '<div class="zt" style="background-color: #FF9800">' . $nr['api'][$i]['now'] . '</div>';
                        break;
                    default:
                        $now = '<div class="zt" style="background-color: #67c23a">' . $nr['api'][$i]['now'] . '</div>';
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
                <p class="title">' . $nr['api'][$i]['name'] . '</p>
                <p class="text">' . $nr['api'][$i]['txt'] . '</p>
                ' . $url . '</div>';
                $i++;
            }
            ?>

        </div>
        <div id="footer">
            <p><?php echo $nr['footer']; ?></p>
        </div>
        <div id="xf">
            <a ks-tag="left" ks-text="返回顶部" href="#tj">
                <svg t="1680926673355" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="2320">
                    <path d="M188.274526 257.534448l0-97.428983 645.061528 0 0 97.428983L188.274526 257.534448zM252.732583 515.366677l255.82655-226.355331 260.329097 226.355331-192.371332 0 0 192.362122 0 161.89625-130.911562 0 0-354.258373L252.732583 515.366677z" p-id="2321" fill="#61677C"></path>
                </svg>
            </a>
        </div>
    </div>
</body>

</html>
<?php die; ?>
