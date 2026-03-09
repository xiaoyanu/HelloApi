<script lang="ts" setup>
import { useRoute, useRouter } from 'vue-router'
import { DocumentCopy } from '@element-plus/icons-vue'
import { onMounted, ref, computed } from "vue"
import type { App } from "@/types";
import { GetApiInfo } from "@/api";
import { PhHouseLine } from "@phosphor-icons/vue";
import { copyText } from "@/utils";
import { useUserStore } from "@/stores";

const router = useRouter()
const route = useRoute()
const userStore = useUserStore();
const apiId = route.params.id

// 定义 loading 状态
const loading = ref(true)

const apiInfo = ref<App>({
  title: '',
  smallTitle: '',
  // 接口状态 0正常 1异常 2维护
  status: -1,
  // 接口类型 0免费 1收费
  type: -1,
  url: '',
  // 接口请求类型 GET0 POST1 PUT2 DELETE3
  sendType: -1,
  returnType: '',
  returnContent: '',
  params: [],
  view_status: -1,
  user_id: 0
})

// === 代码示例相关逻辑 ===
const currentLang = ref('php');

const codeLanguages = [
  { id: 'php', name: 'PHP' },
  { id: 'js', name: 'JavaScript' },
  { id: 'go', name: 'Go' },
  { id: 'python', name: 'Python' },
  { id: 'e', name: '易语言' },
];

// 动态生成不同语言的代码，并包裹为 markdown 格式
const currentCodeMarkdown = computed(() => {
  if (!apiInfo.value.url) return '';

  const url = getApiUrl();
  const method = apiInfo.value.sendType == 0 ? 'GET' :
      apiInfo.value.sendType == 1 ? 'POST' :
          apiInfo.value.sendType == 2 ? 'PUT' :
              apiInfo.value.sendType == 3 ? 'DELETE' : 'GET';

  // 简单拼接参数示例
  let queryParams = '';
  if (apiInfo.value.params && apiInfo.value.params.length > 0) {
    const paramsList = apiInfo.value.params.map(p => `${p.name}=${p.type === 'string' ? 'string' : 'value'}`);
    queryParams = '?' + paramsList.join('&');
  }
  const fullUrl = method === 'GET' ? `${url}${queryParams}` : url;

  let code = '';
  switch (currentLang.value) {
    case 'js':
      code = `fetch("${fullUrl}", {\n  method: "${method}"\n})\n  .then(response => response.json())\n  .then(data => console.log(data))\n  .catch(err => console.error(err));`;
      break;
    case 'php':
      if (method === 'GET') {
        code = `<?php\n$url = "${fullUrl}";\n$response = file_get_contents($url);\necho $response;\n?>`;
      } else {
        code = `<?php\n$url = "${url}";\n$data = array('key' => 'value');\n\n$options = array(\n    'http' => array(\n        'header'  => "Content-type: application/x-www-form-urlencoded\\r\\n",\n        'method'  => '${method}',\n        'content' => http_build_query($data)\n    )\n);\n$context  = stream_context_create($options);\n$result = file_get_contents($url, false, $context);\n\nvar_dump($result);\n?>`;
      }
      break;
    case 'go':
      code = `package main\n\nimport (\n\t"fmt"\n\t"io/ioutil"\n\t"net/http"\n)\n\nfunc main() {\n\turl := "${fullUrl}"\n\tmethod := "${method}"\n\n\tclient := &http.Client {}\n\treq, err := http.NewRequest(method, url, nil)\n\n\tif err != nil {\n\t\tfmt.Println(err)\n\t\treturn\n\t}\n\tres, err := client.Do(req)\n\tif err != nil {\n\t\tfmt.Println(err)\n\t\treturn\n\t}\n\tdefer res.Body.Close()\n\n\tbody, err := ioutil.ReadAll(res.Body)\n\tif err != nil {\n\t\tfmt.Println(err)\n\t\treturn\n\t}\n\tfmt.Println(string(body))\n}`;
      break;
    case 'python':
      code = `import requests\n\nurl = "${fullUrl}"\n\nresponse = requests.request("${method}", url)\n\nprint(response.text)`;
      break;
    case 'e':
      code = `易语言的代码`;
      break;
  }

  // 根据语言类型设置 markdown 标识
  const mdLang = currentLang.value === 'js' ? 'javascript' : currentLang.value;
  return `\`\`\`${mdLang}\n${code}\n\`\`\``;
});
// === 代码示例相关逻辑 结束 ===

// 获取 API 详情
const getApiInfo = async () => {
  loading.value = true;
  try {
    const res = await GetApiInfo(Number(apiId));
    if (res.data.code == 200) {
      apiInfo.value = res.data.data
      if (apiInfo.value.view_status != 0 || apiInfo.value.status == 3) {
        if (!userStore.token) {
          await router.push('/')
          ElMessage.error('🚀呼噜~ 这个接口逃离了地球🌏')
          return
        }
        await userStore.refreshUser()
        if (userStore.user.mode != 1) {
          if (userStore.user.id !== apiInfo.value.user_id) {
            await router.push('/')
            ElMessage.error('🚀呼噜~ 这个接口逃离了地球🌏')
            return
          }
        }

        if (apiInfo.value.status == 3) {
          ElMessage.info("此接口目前处于 [ 隐藏 ] 状态，仅您和管理员可见")
        } else {
          if (apiInfo.value.view_status == 1) {
            ElMessage.error("此接口目前处于 [ 拒绝 ] 状态，仅您和管理员可见")
          } else {
            ElMessage.warning("此接口目前处于 [ 审核 ] 状态，仅您和管理员可见")
          }
        }
      }

      if (apiInfo.value.status == 1) {
        ElMessage.error('此接口目前处于异常状态，可能无法正常响应')
      }
      if (apiInfo.value.status == 2) {
        ElMessage.warning('此接口目前处于维护状态，可能无法正常响应')
      }
    }
  } catch (error) {
    console.error("获取接口详情失败:", error);
    ElMessage.error("网络异常，获取接口详情失败");
  } finally {
    loading.value = false;
  }
}

const getApiUrl = () => {
  const url = apiInfo.value.url;
  if (/^https?:\/\//i.test(url)) {
    return url;
  }
  const domain = window.location.origin;
  return domain + url;
};

onMounted(() => {
  getApiInfo()
})
</script>

<template>
  <banner>{{ apiInfo.title || '加载中...' }}</banner>

  <div id="box"
       v-loading="loading"
       element-loading-background="#ECF0F3"
       class="rounded-[15px] mx-auto mt-4 mb-4 md:mt-8 md:mb-8 w-[95%] max-w-275 flex p-3 md:p-3.75 flex-col text-[#61677c] text-[14px] md:text-[18px] min-h-100">

    <template v-if="!loading">
      <div id="name" class="pb-3 md:pb-3.75 text-[20px] md:text-[27px] font-bold">{{ apiInfo.title }}</div>
      <div id="request" class="pb-3.75 pt-3.75 space-y-2">
        <div class="flex items-center flex-wrap gap-y-2">
          <span class="whitespace-nowrap">请求地址：</span>
          <a :href="getApiUrl()" class="link break-all" target="_blank">{{ getApiUrl() }}</a>
          <el-tooltip
              content="复制地址"
              placement="top-start"
          >
            <div id="copy" class="bg-[#ecf0f3] ml-2 shrink-0 rounded-lg flex items-center justify-center w-7 h-7"
                 @click="copyText(getApiUrl())">
              <el-icon size="16">
                <DocumentCopy/>
              </el-icon>
            </div>
          </el-tooltip>
        </div>
        <div>请求方式：<span class="link">{{
            apiInfo.sendType == 0 ? 'GET' :
                apiInfo.sendType == 1 ? 'POST' :
                    apiInfo.sendType == 2 ? 'PUT' :
                        apiInfo.sendType == 3 ? 'DELETE' : '...'
          }}</span></div>
        <div>返回格式：<span class="link">{{ apiInfo.returnType }}</span></div>
      </div>
      <div class="pt-3.75 pb-3.75 w-full">
        <div class="text-[18px] md:text-[20px] border-l-[5px] border-[#61677C] pl-1 mb-2">请求参数</div>
        <div class="w-full overflow-x-auto rounded-lg border border-[#d1d9e6]">
          <table class="min-w-125">
            <tbody>
            <tr>
              <th class="th-r">参数名</th>
              <th class="th-r">必填</th>
              <th class="th-r">类型</th>
              <th>描述</th>
            </tr>
            <tr v-for="(item, index) in apiInfo.params" :key="index">
              <td class="td-r">
                <div class="tag-box">
                  <span class="tag tag-name">{{ item.name }}</span>
                </div>
              </td>
              <td class="td-r">
                <div class="tag-box">
                  <span :class="{'tag tag-required-yes': item.required == 1, 'tag tag-required-no': item.required == 0}">
                    {{ item.required == 1 ? '是' : '否' }}
                  </span>
                </div>
              </td>
              <td class="td-r">
                <div class="tag-box">
                  <span class="tag tag-type">{{ item.type }}</span>
                </div>
              </td>
              <td class="whitespace-pre-wrap">
                {{ item.msg }}
              </td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
      <div class="pt-3.75 pb-3.75 w-full">
        <div class="text-[18px] md:text-[20px] border-l-[5px] border-[#61677C] pl-1 mb-2">返回示例</div>
        <div id="response" class="overflow-x-auto">
          <m-d-view :text="apiInfo.returnContent"/>
        </div>
      </div>
      <div class="pt-3.75 w-full">
        <div class="text-[18px] md:text-[20px] border-l-[5px] border-[#61677C] pl-1 mb-2">代码示例</div>
        <div class="flex gap-4 mb-2 overflow-x-auto py-4 px-2 -mx-2">
          <div
              v-for="lang in codeLanguages"
              :key="lang.id"
              @click="currentLang = lang.id"
              :class="['lang-btn', currentLang === lang.id ? 'active' : '']"
          >
            {{ lang.name }}
          </div>
        </div>

        <div id="response" class="overflow-x-auto">
          <m-d-view :text="currentCodeMarkdown" />
        </div>
      </div>

    </template>
  </div>

  <div class="backHome" @click="router.push('/')">
    <PhHouseLine size="24" weight="bold"/>
  </div>
</template>

<style lang="scss" scoped>
/* 针对局部横向滚动容器定制滚动条 */
.overflow-x-auto::-webkit-scrollbar {
  height: 6px;
}

.overflow-x-auto::-webkit-scrollbar-track {
  border-radius: 10px;
  background: transparent;
}

.overflow-x-auto::-webkit-scrollbar-thumb {
  background-color: rgba(0, 0, 0, 0.2);
  border-radius: 10px;
}

.overflow-x-auto::-webkit-scrollbar-thumb:hover {
  background: #909399;
}

.backHome {
  position: fixed;
  right: 20px;
  bottom: 70px;
  height: 40px;
  width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
  cursor: pointer;
  background-color: #ecf0f3;
  border-radius: 8px;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  color: #61677C;
  transition: all 0.2s ease-in-out;
}

.backHome:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

.backHome:active {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
}

/* 语言切换按钮样式（新拟态风格） */
.lang-btn {
  padding: 6px 14px;
  border-radius: 8px;
  cursor: pointer;
  background-color: #ecf0f3;
  color: #61677c;
  font-size: 14px;
  white-space: nowrap;
  box-shadow: -3px -3px 10px #FFF, 3px 3px 10px #d1d9e6;
  transition: all 0.2s ease-in-out;
  user-select: none;
}

.lang-btn:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

.lang-btn.active {
  box-shadow: inset 2px 2px 5px #d1d9e6, inset -2px -2px 5px #FFF;
  color: #409EFF;
  font-weight: bold;
}

#box {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
  user-select: none;

  #name, #request {
    border-bottom: solid 1px #d1d9e6;
  }

  #request {
    .link {
      border-bottom: solid 1px #000;
    }

    #copy {
      cursor: pointer;
      box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
      transition: all 0.2s ease-in-out;
    }

    #copy:hover {
      box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
    }

    #copy:active {
      box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
    }
  }

  table {
    width: 100%;
    text-align: center;
    border-spacing: 0;
    border-collapse: separate;
    align-items: center;

    th, td {
      padding: 4px;
      border: none;
      vertical-align: middle;
      user-select: text;
    }

    .th-r, .td-r {
      border-right: solid .5px #d1d9e6;
    }

    td {
      max-width: 100px;
      border-top: solid .5px #d1d9e6;
      word-break: break-all;
      display: table-cell;
    }
  }

  #response {
    border-radius: 8px;
    border: solid 1px #d1d9e6;
    width: 100%;
    padding: 10px;
    user-select: text;
  }
}

.tag {
  font-size: 13px;
  padding: 2px 6px;
  border-radius: 6px;
  border-width: 1px;
  border-style: solid;
  white-space: nowrap;
}

@media (min-width: 768px) {
  .tag {
    font-size: 14px;
    padding: 0 8px;
  }
}

.tag-required-yes {
  background: #FEF0F0;
  color: #F56C6C;
  border-color: #FDE2E2;
}

.tag-required-no {
  background: #F4F4F5;
  color: #909399;
  border-color: #E9E9EB;
}

.tag-name {
  background: #ECF5FF;
  color: #409EFF;
  border-color: #D9ECFF;
}

.tag-type {
  background: #FDF6EC;
  color: #e6a32e;
  border-color: #FAECD8;
}

.tag-box {
  display: flex;
  align-items: center;
  justify-content: center;
}
</style>