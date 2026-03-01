<script lang="ts" setup>
import {useRoute, useRouter} from 'vue-router'
import {DocumentCopy} from '@element-plus/icons-vue'
import {onMounted, ref} from "vue"
import type {App} from "@/types";
import {GetApiInfo} from "@/api";
import {PhHouseLine} from "@phosphor-icons/vue";

const router = useRouter()
const route = useRoute()
const apiId = route.params.id

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
  view_status: -1
})

// 获取 API 详情
const getApiInfo = async () => {
  const res = await GetApiInfo(Number(apiId));
  if (res.data.code == 200) {
    apiInfo.value = res.data.data
    if (apiInfo.value.status == 1) {
      ElMessage.error('此接口目前处于异常状态，可能无法正常响应')
    }
    if (apiInfo.value.status == 2) {
      ElMessage.warning('此接口目前处于维护状态，可能无法正常响应')
    }
  }
}

const copyAddress = async () => {
  await navigator.clipboard.writeText(getApiUrl());
  ElMessage.success('复制成功')
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
  <banner>{{ apiInfo.title }}</banner>
  <div id="box"
       class="rounded-[15px] mx-auto mt-4 mb-4 md:mt-8 md:mb-8 w-[95%] max-w-275 flex p-3 md:p-3.75 flex-col text-[#61677c] text-[14px] md:text-[18px]">
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
               @click="copyAddress">
            <el-icon size="16">
              <DocumentCopy/>
            </el-icon>
          </div>
        </el-tooltip>
      </div>
      <div>请求方式：<span class="link">{{ apiInfo.sendType }}</span></div>
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
    <div class="pt-3.75 w-full">
      <div class="text-[18px] md:text-[20px] border-l-[5px] border-[#61677C] pl-1 mb-2">返回示例</div>
      <div id="response" class="overflow-x-auto">
        <m-d-view :text="apiInfo.returnContent"/>
      </div>
    </div>
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
  font-size: 13px; /* 移动端字号稍微改小 */
  padding: 2px 6px;
  border-radius: 6px;
  border-width: 1px;
  border-style: solid;
  white-space: nowrap; /* 防止标签换行 */
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