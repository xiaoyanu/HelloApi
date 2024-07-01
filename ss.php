<?php
include 'config.php';
$Api = new ApiMain();
$temp = $Api->GetApi();
if ($temp == false) {
    echo "核心文件Json未找到，可能是文件路径没有正确填写或文件名称您更改，请修正config.php文件中的路径为正确路径。";
    die;
}
$nr = json_decode($temp, true);
?>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $nr['title'] ?> - 接口搜索</title>
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
        <a ks-tag="bottom" ks-text="继续搜索" href="//<?php echo $nr['ym'] ?>/ss.php">
            <h1>🔍搜索接口</h1>
        </a>
    </div>
    <div id="main">
        <div id="center">
            <?php
            $word = $_GET['s'];
            if ($word == "") {
                echo '<center>
                <div id="ss-main">
                    <form action="ss.php" method="GET">
                        <input id="ss" type="text" name="s" placeholder="请输入关键字...">
                        <center>
                            <input id="g-ss" type="submit" value="搜索">
                        </center>
                    </form>
                </div>
            </center>';
            } else {
                $num = count($nr['api']) - 1;
                $i = 0;
                $name = [];
                $ms = [];
                $out = [];
                while ($num >= $i) {
                    if (strpos($nr['api'][$i]['name'], strtoupper($word)) !== false)
                        array_push($name, $i);
                    $i++;
                }
                $i = 0;
                while ($num >= $i) {
                    if (strpos($nr['api'][$i]['txt'], strtoupper($word)) !== false)
                        array_push($ms, $i);
                    $i++;
                }
                // 数组去重
                $out = array_unique(array_merge($name, $ms));
                $out = array_values($out);
                if (count($out) != 0) {
                    $i = 0;
                    while (count($out) - 1 >= $i) {
                        //判断是否是维护状态
                        switch ($nr['api'][$out[$i]]['now']) {
                            case "未公开":
                                $now = '<span class="zt" style="background-color: #0288D1">' . $nr['api'][$out[$i]]['now'] . '</span>';
                                break;
                            case "维护":
                                $now = '<span class="zt" style="background-color: #f56c6c">' . $nr['api'][$out[$i]]['now'] . '</span>';
                                break;
                            case "收费":
                                $now = '<span class="zt" style="background-color: #FF9800">' . $nr['api'][$out[$i]]['now'] . '</span>';
                                break;
                            default:
                                $now = '<span class="zt" style="background-color: #67c23a">' . $nr['api'][$out[$i]]['now'] . '</span>';
                                break;
                        }
                        //辨别链接
                        switch ($nr['api'][$out[$i]]['now']) {
                            case "未公开":
                                $url = '<a class="url" onclick="ks.notice(\'' . $nr['api'][$out[$i]]['name'] . ' 未公开\', {time: 1500});">查看</a>';
                                break;
                            case "维护":
                                $url = '<a class="url" onclick="ks.notice(\'' . $nr['api'][$out[$i]]['name'] . ' 维护中\', {time: 1500});">查看</a>';
                                break;
                            default:
                                $url = '<a target="_blank" href="//' . $nr['ym'] . '/info.php?id=' . $out[$i] . '" class="url">查看</a>';
                                break;
                        }
                        echo
                        '<div class="apibox">
                ' . $now . '
                <div class="title">
                <p>' . $nr['api'][$out[$i]]['name'] . '</p>
                </div>
                <div class="text">
                <p>' . $nr['api'][$out[$i]]['txt'] . '</p>
                </div>
                ' . $url . '</div>';
                        $i++;
                    }
                } else {
                    echo '
                    <center>
                    <div id="ss-main">
                    <form action="ss.php" method="GET">
                        <input id="ss" type="text" name="s" placeholder="请输入内容...">
                        <center>
                            <input id="g-ss" type="submit" value="搜索">
                        </center>
                        </form>
                        <br>
                        <h2>没有找到与 ' . $word . ' 相关的接口</h2>
                    </div>
                    </center>
                    ';
                }
            }
            ?>

        </div>
        <div id="footer">
            <p><?php echo $nr['footer']; ?></p>
        </div>
        <div id="xf">
            <a ks-tag="left" ks-text="返回首页" href="/">
                <svg t="1686999655670" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="3490" width="56" height="56">
                    <path d="M863.68 430.848l-33.088-33.152V248.64a52.096 52.096 0 0 0-104.192 0v44.608l-200.512-200.96a19.5456 19.5456 0 0 0-27.712 0l-337.792 338.56L36.736 554.816c-11.776 11.776-3.392 31.936 13.248 31.936h110.4v280.832c0 30.912 25.088 56 56 56h190.144v-255.36c0-58.24 47.232-105.472 105.536-105.472 29.12 0 55.488 11.84 74.56 30.912s30.912 45.44 30.912 74.56v255.36h190.144c30.912 0 56-25.088 56-56V586.752h110.4c16.704 0 25.024-20.16 13.248-31.936l-123.648-123.968z" fill="#61677C" p-id="3491"></path>
                </svg>
            </a>
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
