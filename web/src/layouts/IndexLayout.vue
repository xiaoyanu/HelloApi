<script setup lang="ts">
import {HelloAPIConfig} from '@/config/config'
import bannerBox from '@/components/banner.vue'
import '@/assets/css/IndexStyle.css'
</script>
<template>

  <div style="height: 100vh;background-color: #ecf0f3;">
  <div id="head" class="flex items-center justify-between h-16 leading-16 text-[#61677C] bg-[#ecf0f3]">
    <div class="flex items-center justify-between ml-5">
      <img class="h-12.5 w-12.5 rounded-[10px]" src="@/assets/images/logo.png" alt="logo">
      <a class="text-[24px]" :href="`//${HelloAPIConfig.domain}`">Hello API</a>
      <img class="h-15 ml-12.5" src="https://api.zxz.ee/api/api_count/" alt="统计">
    </div>
    <div id="center">
      🔍搜索框
    </div>
    <div class="mr-12.5">
      <a id="wt" class="h-7.5 leading-7.5 w-25 inline-block text-center font-semibold rounded-lg bg-[#ecf0f3]"
         href="https://wpa.qq.com/msgrd?v=3&amp;site=qq&amp;menu=yes&amp;uin=1872786834">问题反馈</a>
    </div>
  </div>
  <bannerBox title="Hello API"/>
  <div id="main">
    <router-view/>
  </div>
  <div id="footer" class="mt-3.75 ">
    <p>
      {{ HelloAPIConfig.website.footer }}
    </p>
  </div>
  </div>
</template>
<style scoped lang="scss">
a {
  text-decoration: none;
}

#head {
  position: relative;
  box-shadow: -5px -5px 20px #fff, 5px 5px 20px #d1d9e6;
  text-shadow: 1px 1px 0 #FFF;
}

#wt {
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  transition: all 0.2s ease-in-out;
}

#wt:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

</style>