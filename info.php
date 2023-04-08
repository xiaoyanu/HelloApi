<?php
$temp = file_get_contents("main.json");
$nr = json_decode($temp, true);
$num = $_GET['id'];
if ($num == "") {
    die(header("Location:http://" . $nr['ym']));
}
if ($nr['api'][$num]['name'] == "") {
    die(header("Location:http://" . $nr['ym']));
}
if ($nr['api'][$num]['now'] != "正常" && $nr['api'][$num]['now'] != "收费") {
    die(header("Location:http://" . $nr['ym']));
}
?>
<!DOCTYPE html>
<html lang="zh">

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><?php echo $nr['api'][$num]['name'] ?> - <?php echo $nr['title'] ?></title>
    <link rel="icon" type="image/ico" href="<?php echo $nr['icon'] ?>">
    <link rel="stylesheet" href="css/info.css">
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
        <a ks-tag="bottom" ks-text="返回首页" href="<?php echo '//' . $nr['ym'] ?>">
            <h1><?php echo $nr['api'][$num]['name'] ?></h1>
        </a>
    </div>
    <div id="main">
        <div id="center">
            <h2><?php echo $nr['api'][$num]['name'] ?></h2>
            <p class="url">接口地址：<a id="text" class="link" href="<?php echo $nr['api'][$num]['url'] ?>" target="_blank"><?php echo $nr['api'][$num]['url'] ?></a>
                <button ks-tag="bottom" ks-text="复制" onclick="copyText()">
                    <textarea style="position: absolute;top: 0;left: 0;opacity: 0;z-index: -10;" id="input"></textarea>
                    <svg t="1680918393404" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5679" width="20" height="20">
                        <path d="M753.784471 870.520471c0 34.334118-26.985412 62.162824-60.235295 62.223058h-542.117647c-33.310118 0-60.235294-27.888941-60.235294-62.223058v-559.585883c0-34.334118 26.925176-62.162824 60.235294-62.162823h542.117647c33.249882 0 60.235294 27.828706 60.235295 62.162823v559.585883z m-60.235295-683.91153h-542.117647c-66.56 0-120.470588 55.657412-120.470588 124.325647v559.585883c0 68.668235 53.910588 124.385882 120.470588 124.385882h542.117647c66.499765 0 120.470588-55.657412 120.470589-124.385882v-559.585883c0-68.668235-53.970824-124.325647-120.470589-124.325647" fill="#61677C" p-id="5680"></path>
                        <path d="M211.365647 808.056471h240.941177v-62.223059h-240.941177v62.223059z m0-124.385883h421.647059v-62.162823h-421.647059v62.162823z m0-124.325647h421.647059V497.121882h-421.647059v62.223059z m0-124.385882h421.647059V372.856471h-421.647059V434.898824z" fill="#61677C" p-id="5681"></path>
                        <path d="M875.098353 0.180706h-542.117647c-66.56 0-120.470588 55.657412-120.470588 124.325647h60.235294c0-34.334118 26.985412-62.162824 60.235294-62.162824h542.117647c33.249882 0 60.235294 27.828706 60.235294 62.162824v559.585882c0 34.334118-26.985412 62.162824-60.235294 62.223059v62.162824c66.56 0 120.470588-55.657412 120.470588-124.385883v-559.585882c0-68.668235-53.910588-124.325647-120.470588-124.325647" fill="#61677C" p-id="5682"></path>
                    </svg>
                </button>
            </p>
            <p class="qq">请求方式：<span><?php echo $nr['api'][$num]['get'] ?></span></p>
            <p class="gs">返回格式：<span><?php echo $nr['api'][$num]['out'] ?></span></p>
            <p id="cs">&nbsp;请求参数</p>
            <table>
                <tr>
                    <th>名称</th>
                    <th>必填</th>
                    <th>类型</th>
                    <th>描述</th>
                </tr>
                <?php
                $i = $nr['api'][$num]['count'];
                $k = 1;
                while ($i >= $k) {
                    if ($nr['api'][$num][$k . '-name'] != "") {
                        echo
                        '<tr>
                        <td>' . $nr['api'][$num][$k . '-name'] . '</td>
                        <td>' . $nr['api'][$num][$k . '-ok'] . '</td>
                        <td>' . $nr['api'][$num][$k . '-type'] . '</td>
                        <td>' . $nr['api'][$num][$k . '-main'] . '</td>
                        </tr>';
                    }
                    $k++;
                }
                ?>
            </table>
            <p id="cs">&nbsp;返回参数</p>
            <p class="cs-main"><?php echo str_replace(" ","&nbsp;",$nr['api'][$num]['fh']) ?></p>
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
            <a ks-tag="left" ks-text="返回首页" href="<?php echo '//' . $nr['ym'] ?>">
                <svg t="1680926059694" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="5426">
                    <path d="M675.8 901.1h-81.9V737.3c0-45.2-36.7-81.9-81.9-81.9s-81.9 36.7-81.9 81.9v163.8h-81.9V737.3c0-90.3 73.5-163.8 163.8-163.8S675.8 647 675.8 737.3v163.8z" p-id="5427" fill="#61677C"></path>
                    <path d="M750.5 983H274.3c-32.2 0-63.6-13.6-86.2-37.2-22.4-23.5-34.8-54.6-34.8-87.6V562.9H113.6c-30.4-0.2-57-19-67.8-48-10.5-28.1-3.5-59.7 17.8-80.4l0.2-0.2L445.7 67.8c37.1-35.7 95.4-35.7 132.5 0l-56.7 59.1c-5.6-5.4-13.4-5.4-19 0L133.4 481h60.9c22.6 0 41 18.3 41 41v336.4c0 11.6 4.4 22.9 12.1 31 7.3 7.7 16.9 11.9 27 11.9H750.6c10.2 0 19.8-4.2 27.1-11.9 7.7-8.1 12.1-19.4 12.1-31V521.9c0-22.6 18.3-41 41-41h60l-369.2-354 56.7-59.1 381.9 366.4 0.2 0.2c21.3 20.8 28.3 52.3 17.8 80.4-10.6 28.5-37.9 47.8-67.8 48h-38.9v295.3c0 33-12.3 64.2-34.8 87.7-22.5 23.7-53.9 37.2-86.2 37.2z" p-id="5428" fill="#61677C"></path>
                </svg>
            </a>
        </div>
    </div>
</body>

</html>
<?php die; ?>
