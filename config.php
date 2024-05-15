<?php
// 文件路径
$ApiFilePath = "main.json";

class ApiMain
{
    function GetApi()
    {
        global $ApiFilePath;
        if (is_file($ApiFilePath) == false) {
            return false;
        }
        $temp = file_get_contents($ApiFilePath);
        if ($temp == "") {
            return false;
        } else {
            return $temp;
        }
    }
}
