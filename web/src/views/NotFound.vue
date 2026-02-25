<script setup lang="ts">
import {useRouter} from "vue-router";

const router = useRouter()
</script>
<template>
  <div class="flex items-center justify-center h-screen">
    <div class="text-center">
      <h1 class="text-6xl font-bold text-[#409EFF]">404</h1>
      <p class="text-2xl text-[#606266]">页面不存在</p>
      <p class="text-[#909399]">请检查 URL 是否正确</p>
      <button class="mt-5 px-4 py-2 bg-[#409EFF] text-white rounded-md cursor-pointer hover:bg-[#3a8ee6]" @click="router.push('/')">返回首页</button>
    </div>
  </div>
</template>