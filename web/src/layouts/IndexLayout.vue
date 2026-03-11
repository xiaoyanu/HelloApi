<script lang="ts" setup>
import {HelloAPIConfig} from '@/config/config'
import {getNowYear, getSearchNullMessage} from "@/utils";
import {PhArrowLineUp, PhHouse, PhLink, PhList, PhMagnifyingGlass, PhSignIn} from "@phosphor-icons/vue"
import router from "@/router";
import {ref} from "vue";

const keywords = ref('')
// 统计图片地址
const imgSrc = '/api/api_count/'
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

      <div class="flex items-center md:ml-5">
        <img alt="logo" class="h-10 w-10 md:h-12.5 md:w-12.5 rounded-[10px]" draggable="false"
             src="@/assets/images/logo.png">
        <a class="text-[20px] md:text-[24px] ml-3 md:ml-0 md:pl-2" href="/">Hello API</a>
        <img alt="统计" class="hidden lg:block h-15 ml-12.5" draggable="false" :src="imgSrc">
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

      <div class="flex items-center justify-center leading-none md:mr-12.5">
        <el-dropdown popper-class="neu-popper" placement="bottom-end">
          <div
              class="wt-btn h-8 w-10 flex items-center justify-center font-semibold rounded-lg bg-[#ecf0f3] text-[14px] cursor-pointer outline-none">
            <PhList size="18" weight="bold"/>
          </div>

          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item v-if="$route.path !== '/'">
                <router-link to="/" class="flex items-center justify-center w-full h-full custom-link">
                  <PhHouse weight="duotone"/>
                  首页
                </router-link>
              </el-dropdown-item>
              <el-dropdown-item>
                <router-link to="/admin" target="_blank"
                             class="flex items-center justify-center w-full h-full custom-link">
                  <PhSignIn weight="duotone"/>
                  登录 / 注册
                </router-link>
              </el-dropdown-item>
              <el-dropdown-item>
                <router-link to="/links" class="flex items-center justify-center w-full h-full custom-link">
                  <PhLink weight="duotone"/>
                  友人帐
                </router-link>
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
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

/* 自定义下拉菜单样式 */
.neu-popper.el-dropdown__popper {
  background-color: #ecf0f3 !important;
  border: 1px solid rgba(255, 255, 255, 0.6) !important;
  border-radius: 12px !important;
  box-shadow: -8px -8px 20px rgba(255, 255, 255, 0.8), 8px 8px 20px rgba(209, 217, 230, 0.8) !important;
}

.neu-popper.el-popper .el-popper__arrow::before {
  display: none !important;
}

.neu-popper .el-dropdown-menu {
  background-color: transparent !important;
  border: none !important;
  padding: 8px !important;
  min-width: 120px;
}

.neu-popper .el-dropdown-menu__item {
  color: #61677C !important;
  border-radius: 8px !important;
  margin-bottom: 6px !important;
  justify-content: center !important;
  font-size: 14px !important;
  font-weight: 500 !important;
  transition: all 0.2s ease-in-out !important;
}

.neu-popper .el-dropdown-menu__item:last-child {
  margin-bottom: 0 !important;
}

.neu-popper .el-dropdown-menu__item:hover,
.neu-popper .el-dropdown-menu__item:focus {
  background-color: transparent !important;
  color: #ff627c !important;
  box-shadow: inset 3px 3px 6px #d1d9e6, inset -3px -3px 6px #FFF !important;
}
</style>