<script lang="ts" setup>
import {ref} from 'vue'
import {
  PhGithubLogo,
  PhArrowCounterClockwise,
  PhHouse,
  PhRocketLaunch,
  PhKey,
  PhIdentificationCard,
  PhPresentationChart,
  PhUsers,
  PhBookBookmark,
  PhSignOut,
  PhSlidersHorizontal,
  PhList
} from '@phosphor-icons/vue'
import {getGravatarHash} from "@/utils/more.ts";
import {useRoute, useRouter} from "vue-router";
import {useUserStore} from "@/stores";
import {HelloAPIConfig} from "@/config/config.ts";

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()
//  控制移动端菜单显隐
const showMobileMenu = ref(false)
const menus = [
  {label: '首页', path: '/admin', icon: PhHouse},
  {label: 'API 管理', path: '/admin/api', icon: PhRocketLaunch},
  {label: 'APIKey 管理', path: '/admin/key', icon: PhKey},
  {label: '我的信息', path: '/admin/user', icon: PhIdentificationCard},
  {label: '数据概括', path: '/admin/stat', icon: PhPresentationChart},
  {label: '用户管理', path: '/admin/manage', icon: PhUsers},
  {label: '全局设置', path: '/admin/setting', icon: PhSlidersHorizontal},
  {label: '使用文档', path: '/admin/doc', icon: PhBookBookmark},
]
const handleNav = (path: string) => {
  router.push(path)
  showMobileMenu.value = false // 移动端点击菜单后自动收起
}

const logout = () => {
  userStore.removeAll()
  router.push('/admin/login')
  ElMessage.info('已退出登录')
}

const watermark = (): string => {
  if (HelloAPIConfig.website.admin.watermark.view) {
    if (HelloAPIConfig.website.admin.watermark.content.length > 0) {
      return HelloAPIConfig.website.admin.watermark.content
    } else {
      return userStore.user.nick || ''
    }
  }
  return ''
}
</script>
<template>
  <el-watermark :content="watermark()">
    <div class="bg-[#F5F7FA] w-full min-h-screen">
      <div class="flex h-screen overflow-hidden">

        <div
            v-if="showMobileMenu"
            class="fixed inset-0 bg-black/50 z-40 md:hidden transition-opacity"
            @click="showMobileMenu = false"
        ></div>

        <aside :class="[
          'flex flex-col shrink-0 w-55 bg-white border-r border-solid border-[#E4E7ED]',
          'fixed inset-y-0 left-0 z-50 transform transition-transform duration-300 ease-in-out md:relative md:translate-x-0',
          showMobileMenu ? 'translate-x-0' : '-translate-x-full'
        ]">
          <div class="
            bg-linear-to-l from-[#ff4e50] to-[#f9d423]
            bg-clip-text
            text-transparent
            select-none h-15
            flex items-center
            justify-center
            text-[28px]
            font-bold">
            <img alt="logo" class="w-9" draggable="false" src="@/assets/images/logo.png">
            HelloAPI
          </div>
          <ul class="select-none flex-1 list-none pt-2.5 overflow-y-auto">
            <li
                v-for="item in menus"
                :key="item.path"
                :class="{ 'active': route.path === item.path }"
                class="
              h-12.5
              flex items-center
              pl-6.25
              cursor-pointer
              text-[#606266]
              transition-all duration-300
              font-[14px]
              border-r-[3px] border-transparent
              hover:bg-[#ecf5ff]
              "
                @click="handleNav(item.path)"
            >
              <component :is="item.icon" :size="18" class="mr-3" weight="duotone"/>
              {{ item.label }}
            </li>
          </ul>
          <div class="p-5 border-t border-[#E4E7ED] border-solid">
            <button class="
          w-full p-2 bg-white
          border border-[#dcdfe6] rounded-sm
          text-[#409eff] cursor-pointer text-[13px]
          flex items-center justify-center gap-1.25 mb-3.75
          hover:border-[#c6e2ff]
          hover:bg-[#ecf5ff]
          " @click="router.push('/')">
              <PhArrowCounterClockwise size="15"/>
              返回首页
            </button>
            <div class="text-[12px] text-[#909399] flex items-center justify-center gap-1.25">
              <PhGithubLogo size="15" weight="duotone"/>
              Powered By 周星星
            </div>
          </div>
        </aside>

        <main class="main-container relative w-full">
          <header class="header justify-between md:justify-end">
            <div class="md:hidden flex items-center">
              <button class="text-[#606266] hover:text-[#409EFF] flex items-center" @click="showMobileMenu = true">
                <PhList size="24" weight="bold"/>
              </button>
            </div>

            <div class="user-info">
              <span class="sm:inline-block">Hi, {{ userStore.user.nick }}</span>
              <img
                  :src="`https://cn.cravatar.com/avatar/${getGravatarHash(userStore.user.mail || 'HelloAPI')}?s=100&d=wavatar`"
                  alt="Avatar"
                  class="avatar w-10 h-10 rounded-full">
              <el-tooltip content="退出登录" placement="bottom-end">
                <PhSignOut class="text-[#909399] hover:text-red-400 cursor-pointer" size="18" weight="duotone"
                           @click="logout"/>
              </el-tooltip>
            </div>
          </header>
          <div class="p-4 md:p-10 flex-1 overflow-auto">
            <router-view/>
          </div>
        </main>
      </div>
    </div>
  </el-watermark>
</template>
<style lang="scss" scoped>
.active {
  color: #409EFF;
  background-color: #ecf5ff;
  border-right-color: #409EFF
}

.avatar {
  border: 1px solid #dcdfe6;
}

.main-container {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;

  .header {
    height: 60px;
    background-color: #fff;
    border-bottom: 1px solid #E4E7ED;
    display: flex;
    align-items: center;
    padding: 0 20px;
    flex-shrink: 0;

    .user-info {
      display: flex;
      align-items: center;
      gap: 15px;
      font-size: 14px;
      color: #606266;
    }
  }
}
</style>