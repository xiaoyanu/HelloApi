<script lang="ts" setup>
import { onMounted, ref } from "vue";
import { GetApiList } from "@/api";
import type { Pagination } from "@/types";
import { HelloAPIConfig } from "@/config/config.ts";

// 列表数据
const dataList = ref([]);

// 加载状态
const isLoading = ref(false);

// 分页配置
const paging = ref<Pagination>({
  page: 1,
  pageSize: HelloAPIConfig.website.index.pageSize,
  total: 0,
});

// 获取 API 列表
const getApiList = async () => {
  isLoading.value = true; // 开始请求，开启加载状态
  try {
    const res = await GetApiList(paging.value.page, paging.value.pageSize);
    if (res.data.code === 200) {
      dataList.value = res.data.data.list;
      paging.value.total = res.data.data.total;
    }
  } finally {
    isLoading.value = false; // 无论成功或失败，请求结束时关闭加载状态
  }
};

// 分页改变时触发
function handleNewPageChange(page: number) {
  paging.value.page = page;
  getApiList();
}

onMounted(() => {
  getApiList();
});
</script>

<template>
  <banner>Hello API</banner>

  <ApiCardList
      :loading="isLoading"
      :dataList="dataList"
      :paging="paging"
      empty-text="哇塞！一个接口也没有……"
      @pageChange="handleNewPageChange"
  />
</template>