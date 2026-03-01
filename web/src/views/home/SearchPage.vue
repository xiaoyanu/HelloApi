<script lang="ts" setup>
import {useRoute, useRouter} from "vue-router";
import type {Pagination} from "@/types";
import {onMounted, ref, watch} from "vue";
import {SearchApi} from "@/api";
import {HelloAPIConfig} from "@/config/config.ts";
import {PhHouseLine} from "@phosphor-icons/vue";

const router = useRouter()
const route = useRoute()
const keywords = ref(route.params.keywords)

// 数组
const dataList = ref([])

// 获取 API 列表
const getApiList = async () => {
  const res = await SearchApi(keywords.value as string, paging.value.page, paging.value.pageSize);
  if (res.data.code == 200) {
    dataList.value = res.data.data.list;
    paging.value.total = res.data.data.total
  }
}


// 分页
const paging = ref<Pagination>({
  page: 1,
  pageSize: HelloAPIConfig.website.index.pageSize,
  total: 0,
})

function handleNewPageChange(page: number) {
  paging.value.page = page
  getApiList();
}


onMounted(() => {
  getApiList();
})
// 监听路由参数变化
watch(() => route.params.keywords, (newKeywords) => {
  keywords.value = newKeywords
  getApiList();
})
</script>
<template>
  <banner>🔍{{ keywords }}</banner>
  <ApiCardList :dataList="dataList" :paging="paging" empty-text="没有找到相关的接口" @pageChange="handleNewPageChange"/>
  <div class="backHome" @click="router.push('/')">
    <PhHouseLine size="24" weight="bold"/>
  </div>
</template>

<style lang="scss" scoped>
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
</style>