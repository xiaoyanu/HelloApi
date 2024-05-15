<?php
include 'config.php';
$Api = new ApiMain();
$temp = $Api->GetApi();
if ($temp == false) {
    echo "核心文件Json未找到，可能是文件路径没有正确填写或文件名称您更改，请修正config.php文件中的路径为正确路径。";
    die;
}
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
	<meta name="description" content="<?php echo $nr['api'][$num]['name'] . " - " . $nr['api'][$num]['txt'] ?>">
	<meta name="keywords" content="<?php echo $nr['keywords'] ?>">
	<title><?php echo $nr['api'][$num]['name'] ?> - <?php echo $nr['title'] ?></title>
	<link rel="icon" type="image/ico" href="<?php echo $nr['icon'] ?>">
	<link rel="stylesheet" href="css/info.css">
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
					<th class="name" id="name1">名称</th>
					<th class="sfbt" id="sfbt1">必填</th>
					<th class="type" id="type1">类型</th>
					<th class="ms" id="ms1">描述</th>
				</tr>
				<?php
				$i = $nr['api'][$num]['count'];
				$k = 1;
				while ($i >= $k) {
					if ($nr['api'][$num][$k . '-name'] != "") {
						echo
						'<tr>
                        <td class="name">' . $nr['api'][$num][$k . '-name'] . '</td>
                        <td class="sfbt">' . $nr['api'][$num][$k . '-ok'] . '</td>
                        <td class="type">' . $nr['api'][$num][$k . '-type'] . '</td>
                        <td class="ms">' . $nr['api'][$num][$k . '-main'] . '</td>
                        </tr>';
					}
					$k++;
				}
				?>
			</table>
			<p id="cs">&nbsp;返回参数</p>
			<p class="cs-main">
				<?php
				function replace_spaces($text)
				{
					$outside_tag = true;
					$result = '';
					// 按字符逐一处理文本
					for ($i = 0; $i < strlen($text); $i++) {
						$ch = $text[$i];
						// 如果当前字符是空格
						if ($ch == ' ' && $outside_tag) {
							$result .= '&nbsp;';  // 将空格替换为 "&nbsp;"
						} else {
							$result .= $ch;  // 否则保留原样
						}
						// 判断是否在 HTML 标签中
						if ($ch == '<') {
							$outside_tag = false;
						} else if ($ch == '>') {
							$outside_tag = true;
						}
					}
					return $result;
				}
				echo replace_spaces($nr['api'][$num]['fh']);
				?>
			</p>
		</div>
		<div id="footer">
			<p><?php echo $nr['footer']; ?></p>
		</div>
		<div id="xf">
			<a ks-tag="left" ks-text="返回首页" href="<?php echo '//' . $nr['ym'] ?>">
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
