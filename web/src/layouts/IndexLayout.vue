<script lang="ts" setup>
import {HelloAPIConfig} from '@/config/config'
import {getNowYear, getSearchNullMessage} from "@/utils/more.ts";
import {PhArrowLineUp, PhMagnifyingGlass} from "@phosphor-icons/vue"
import router from "@/router";
import {ref} from "vue";

const keywords = ref('')

const search = () => {
  if (keywords.value.trim() === '') {
    ElMessage.warning(getSearchNullMessage())
  } else {
    router.push('/search/' + encodeURIComponent(keywords.value.trim()))
  }
}
</script>
<template>
  <div class="bg-[#ecf0f3] w-full min-h-screen">
    <div id="head"
         class="flex flex-wrap md:flex-nowrap items-center justify-between min-h-16 py-3 px-4 md:py-0 md:px-0 md:h-16 md:leading-16 text-[#61677C] bg-[#ecf0f3]">

      <div class="flex items-center justify-between w-full md:w-auto md:ml-5">
        <div class="flex items-center">
          <img alt="logo" class="h-10 w-10 md:h-12.5 md:w-12.5 rounded-[10px]" draggable="false"
               src="@/assets/images/logo.png">
          <a class="text-[20px] md:text-[24px] ml-3 md:ml-0 md:pl-2" href="/">Hello API</a>
        </div>
        <img alt="统计" class="hidden lg:block h-15 ml-12.5" draggable="false" src="https://api.zxz.ee/api/api_count/">

        <div class="block md:hidden">
          <a class="wt-btn h-8 leading-8 px-4 inline-block text-center font-semibold rounded-lg bg-[#ecf0f3] text-[14px]"
             href="/admin" target="_blank">登录</a>
        </div>
      </div>

      <div id="center" class="flex items-center justify-between w-full md:w-auto mt-4 md:mt-0 order-last md:order-0">
        <input v-model="keywords"
               class="flex-1 md:flex-none md:w-60 p-2 h-8 text-[#61677c] text-[14px] bg-[#ecf0f3] rounded-lg"
               placeholder="搜索接口…" type="text"
               @keyup.enter="search">
        <div class="flex items-center p-2 rounded-lg h-8 ml-3 cursor-pointer shrink-0 text-[14px]" @click="search">
          <PhMagnifyingGlass size="15" weight="duotone"/>
          <span class="ml-1">搜索</span>
        </div>
      </div>

      <div class="hidden md:block mr-5 md:mr-12.5">
        <a class="wt-btn h-8 leading-8 w-25 inline-block text-center font-semibold rounded-lg bg-[#ecf0f3] text-[14px]"
           href="/admin" target="_blank">登录 / 注册</a>
      </div>
    </div>
    <router-view/>

    <div id="footer" class="mt-3.75 min-h-20 py-4 text-[#61677C] flex items-center justify-center px-4">
      <p class="text-center text-[12px] md:text-[14px] leading-relaxed">
        &copy; {{ getNowYear() }} All rights reserved.&emsp;|&emsp;
        <span v-html="HelloAPIConfig.website.footer"></span>
      </p>
    </div>
  </div>
  <el-backtop :bottom="20" :right="20" class="backtop-info">
    <PhArrowLineUp size="24" weight="bold"/>
  </el-backtop>
</template>
<style lang="scss" scoped>
a {
  text-decoration: none;
}

#head {
  user-select: none;
  position: relative;
  box-shadow: -5px -5px 20px #fff, 5px 5px 20px #d1d9e6;
  text-shadow: 1px 1px 0 #FFF;
}

.wt-btn {
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  transition: all 0.2s ease-in-out;
}

.wt-btn:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

#center {
  input {
    border: 0;
    text-shadow: 1px 1px 0 #FFF;
    box-shadow: inset 2px 2px 5px #d1d9e6, inset -5px -5px 10px #FFF;
    transition: all 0.2s ease-in-out;
  }

  input:hover, input:focus {
    box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
  }

  div {
    box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
    transition: all 0.2s ease-in-out;
  }

  div:hover {
    box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
  }

  div:active {
    box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
  }
}

#footer {
  user-select: none;
  border-top: solid 1px #d1d9e6;

  :deep() a {
    color: #ff627c;
  }
}
</style>
<style>
.backtop-info {
  background-color: #ecf0f3 !important;
  border-radius: 8px !important;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6 !important;
  color: #61677C !important;
  transition: all 0.2s ease-in-out !important;
}

.backtop-info:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6 !important;
}

.backtop-info:active {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF !important;
}
</style>