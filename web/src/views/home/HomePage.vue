<script lang="ts" setup>
import {onMounted, ref} from "vue";
import {GetApiList} from "@/api";
import type {Pagination} from "@/types";
import {HelloAPIConfig} from "@/config/config.ts";

// 跳转详情页
const checkID = (id: number) => {
  window.open("/info/" + id)
}

// 数组
const dataList = ref([])

// 获取 API 列表
const getApiList = async () => {
  const res = await GetApiList(paging.value.page, paging.value.pageSize);
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

// 分页改变时触发
function handleNewPageChange(page: number) {
  paging.value.page = page
  getApiList();
}

onMounted(() => {
  getApiList();
})
</script>
<template>
  <banner>Hello API</banner>
  <ApiCardList :dataList="dataList" :paging="paging" empty-text="哇塞！一个接口也没有……"
               @pageChange="handleNewPageChange"/>
</template>