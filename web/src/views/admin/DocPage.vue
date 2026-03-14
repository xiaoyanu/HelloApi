<script setup lang="ts">

import AdminMainBody from "@/components/AdminMainBody.vue";

const text = `
### ❗问题反馈
- 如有BUG、建议可在Github中提交或联系我的邮箱 \`1872786834@qq.com\`
- [https://github.com/xiaoyanu/HelloAPI](https://github.com/xiaoyanu/HelloAPI)
---
### 📖如何将自己写好的API接入
 - 首先要明确你的后端地址，以下均用 \`后端\` 来代指后端地址
 - 请求链接：\`后端\` + \`/api/v1/stat/log\`
     -  例如：http://domain.com/api/v1/stat/log
 - 请求方式：\`POST\`
 - 请求参数：必须要穿JSON格式的参数
     - \`api_id\` （必填）API的ID
     - \`api_key\` （必填）API请求的APIKey，如果没有则传 \`NULL\`
     - \`user_id\` （必填）此接口所属用户的ID
     - \`user_key\` （必填）用户的密钥，如果没有用户密钥会直接拒绝写入，方便溯源
     - \`ip\` 请求者的IP地址（可空）
     - \`header\` 请求携带的header（可空，如果传，则必须是JSON格式化后的JSON文本）
     - \`body\` 请求的完整body（可空，如果传，则必须是JSON格式化后的JSON文本）
     - 注意：必填项均需要真实的对应，否则会影响到数据库中的数据统计
 - 返回参数：
     - \`\`\`json
          {
              \"code\": 200,
              \"msg\": \"调用失败，请联系站长/开发者 代码：ERROR_1\",
              \"data\": \"user_key用户密钥不存在\"
          }
          \`\`\`
     - \`code\` 成功为200，其他为失败：比如APIKey过期、次数不足、数据库错误等等
     - \`msg\` 给访问者所看的提示，可以直接返回给访问者
     - \`data\` 系统给作者/管理员看的提示，供自己调试参考，不推荐返回给访问者
     - 你需要在你API返回结果之前来访问这个接口，然后来决定是否真的返回访问者结果
 - 以PHP为例子
     - \`\`\`php
          <?php
          // 此例子基于PHP7.2
          if (!function_exists(\'getallheaders\')) {
            function getallheaders()
            {
              $headers = [];
              foreach ($_SERVER as $name => $value) {
                if (substr($name, 0, 5) == 'HTTP_') {
                  $headers[str_replace(\' \', \'-\', ucwords(strtolower(str_replace(\'_\', \' \', substr($name, 5)))))] = $value;
                }
              }
              return $headers;
            }
          }
          // 获取请求body
          function getAllBody()
          {
            $method = strtoupper($_SERVER[\'REQUEST_METHOD\'] ?? \'GET\');
            $bodyMethods = [\'POST\', \'PUT\', \'DELETE\'];
            if ($method !== \'GET\' && !in_array($method, $bodyMethods)) {
              return null;
            }
            if ($method === \'GET\') {
              return !empty($_GET) ? $_GET : null;
            }
            $inputBody = file_get_contents(\'php://input\');
            return !empty($inputBody) ? $inputBody : (!empty($_POST) ? $_POST : null);
          }
          // 获取访问者IP
          function getRealIp()
          {
            $realIp = $_SERVER[\'HTTP_X_REAL_IP\'] ?? \'\';
            if (filter_var($realIp, FILTER_VALIDATE_IP)) {
              return $realIp;
            }
            $forwardedFor = $_SERVER[\'HTTP_X_FORWARDED_FOR\'] ?? \'\';
            foreach (explode(\',\', $forwardedFor) as $ip) {
              $ip = trim($ip);
              if (filter_var($ip, FILTER_VALIDATE_IP, FILTER_FLAG_NO_PRIV_RANGE | FILTER_FLAG_NO_RES_RANGE)) {
                return $ip;
              }
            }
            return $_SERVER[\'REMOTE_ADDR\'] ?? null;
          }
          // 向统计接口提交日志数据
          function HelloAPILog($api_id, $user_id, $ip, $user_key, $json_header, $json_body, $api_key = null)
          {
            // 后端地址
            $doman = \"http://api.zxz.ee/service\";

            if (!empty($json_header)) {
              json_decode($json_header);
              if (json_last_error() !== JSON_ERROR_NONE) {
                $json_header = null;
              }
            } else {
              $json_header = null;
            }

            if (!empty($json_body)) {
              json_decode($json_body);
              if (json_last_error() !== JSON_ERROR_NONE) {
                $json_body = null;
              }
            } else {
              $json_body = null;
            }
            $payload = json_encode([
              \"api_id\"   => $api_id,
              \"api_key\"  => $api_key,
              \"user_id\"  => $user_id,
              \"user_key\" => $user_key,
              \"ip\"       => $ip,
              \"header\"   => $json_header,
              \"body\"     => $json_body,
            ]);
            $ch = curl_init(\"$doman/api/v1/stat/log\");
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
            curl_setopt($ch, CURLOPT_HTTPHEADER, [\'Content-Type: application/json\']);
            curl_setopt($ch, CURLOPT_TIMEOUT, 3);
            curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 2);
            curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
            curl_setopt($ch, CURLOPT_SSL_VERIFYHOST, false);
            $response = curl_exec($ch);
            return $response;
          }

          // 写入HelloAPI的日志
          $h_api_id = 3;
          $h_ip = getRealIp();
          $h_user_id = 1;
          $h_user_key = \"be42dbd4c5006116487edcec60685d7c\";
          $h_header = json_encode(getallheaders());
          $h_body = json_encode(getAllBody());
          $h_api_key = null;
          // 如果请求失败，会返回原因，其中会有两个参数，msg是给调用API的用户看的，data是给作者/后台看的真实的后端给你的提示
          $h_data = json_decode(HelloAPILog($h_api_id, $h_user_id, $h_ip, $h_user_key, $h_header, $h_body, $h_api_key), true);
          if ($h_data[\'code\'] != 200) {
            // 如果失败拦截返回提示
            header("Content-type:application/json;charset=utf-8");
            echo json_encode([
              \'code\' => 400,
              \'msg\' => $h_data[\'msg\']
            ], 448);

            // 终止，不往下执行了
            die;
          }

          // 通过时，在下面继续执行代码
          echo '我是API的调用结果哈哈哈';
          \`\`\`
----
### ℹ️更详细的接口文档
[HelloAPI 接口文档](https://helloapi.apifox.cn/)
里面包含了所有的接口以及鉴权调用方法等
`
</script>

<template>
  <admin-main-body title="使用文档">
    <template #default>
      <m-d-view :text="text" bg-color="#FFF" font-color="#2C3E50"/>
    </template>
  </admin-main-body>
</template>