<script setup lang="ts">
import {HelloAPIConfig} from '@/config/config'
import {getNowYear, getSearchNullMessage} from "@/utils/more.ts";
import {PhMagnifyingGlass} from "@phosphor-icons/vue"
import router from "@/router";
import {ref} from "vue";

const keywords = ref('')

const search = () => {
  if (keywords.value.trim() === '') {
    ElMessage.warning(getSearchNullMessage())
  } else {
    router.push('/search/' + keywords.value.trim())
  }
}
</script>
<template>
  <div class="bg-[#ecf0f3] w-full min-h-screen">
    <div id="head" class="flex items-center justify-between h-16 leading-16 text-[#61677C] bg-[#ecf0f3]">
      <div class="flex items-center ml-5 w-100">
        <img class="h-12.5 w-12.5 rounded-[10px]" src="@/assets/images/logo.png" alt="logo">
        <a class="text-[24px]" href="/">Hello API</a>
        <img class="h-15 ml-12.5" src="https://api.zxz.ee/api/api_count/" alt="统计">
      </div>
      <div id="center" class="flex items-center justify-between">
        <input v-model="keywords" class="w-60 p-2 h-8 text-[#61677c] text-[14px] bg-[#ecf0f3] rounded-lg" type="text"
               placeholder="搜索接口…">
        <div class="flex items-center p-2 rounded-lg h-7.5 ml-3 cursor-pointer" @click="search">
          <PhMagnifyingGlass size="15" weight="duotone"/>
          &nbsp;搜索
        </div>
      </div>
      <div class="mr-12.5">
        <a id="wt" class="h-7.5 leading-7.5 w-25 inline-block text-center font-semibold rounded-lg bg-[#ecf0f3]"
           href="/admin">登录 / 注册</a>
      </div>
    </div>
    <router-view/>
    <div id="footer" class="mt-3.75 h-20 leading-20 text-[#61677C]">
      <p class="text-center">
        &copy; {{ getNowYear() }} All rights reserved.&nbsp;&nbsp;|&nbsp;&nbsp;<span
          v-html="HelloAPIConfig.website.footer"></span>
      </p>
    </div>
  </div>
</template>
<style scoped lang="scss">
a {
  text-decoration: none;
}

#head {
  user-select: none;
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