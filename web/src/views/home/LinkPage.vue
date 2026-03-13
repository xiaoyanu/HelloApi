<script setup lang="ts">
import {PhHouseLine} from "@phosphor-icons/vue";
import router from "@/router";
import {GetLinkList, GetSettingValue} from "@/api";
import {onMounted, ref} from "vue";

// 友人帐列表数据
const linkList = ref();
const loading = ref(true);

const linkSetting = ref({
  link_name: "HelloAPI",
  link_desc: "一个简单、稳定、快速的 API 接口站",
  link_url: "https://api.zxz.ee",
  link_icon: "https://api.zxz.ee/favicon.ico",
  link_email: "zxz.ee@qq.com",
})

const getLinkList = async () => {
  loading.value = true;
  try {
    const settingKeys = ['link_name', 'link_desc', 'link_url', 'link_icon', 'link_email'] as const;

    // 并行获取友链列表和所有配置项
    const [listRes, ...settingResults] = await Promise.all([
      GetLinkList(1, 999999, 0),
      ...settingKeys.map(key => GetSettingValue(key))
    ]);

    // 处理友链列表数据
    if (listRes.data.code === 200) {
      linkList.value = listRes.data.data.list;
    }

    // 处理配置项数据
    settingKeys.forEach((key, index) => {
      const res = settingResults[index];
      if (res.data.code === 200 && res.data.data) {
        linkSetting.value[key] = res.data.data;
      }
    });
  } catch (error) {
    console.error('获取友链数据失败:', error);
  } finally {
    loading.value = false;
  }
}

onMounted(async () => {
  await getLinkList();
})
</script>

<template>
  <banner>🎉友人帐</banner>

  <div class="min-h-75 w-full flex flex-col items-center" v-loading="loading" element-loading-background="#ECF0F3">
    <!-- 友人帐卡片网格 -->
    <div
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4 justify-center gap-8 mt-10 text-[#61677C] w-full max-w-360 px-4">
      <a v-for="link in linkList" :key="link.name" :href="link.url" target="_blank"
         class="box flex items-center rounded-2xl bg-[#ecf0f3] w-full h-32 p-4 no-underline text-inherit group">
        <!-- 网站图标 -->
        <div
            class="shrink-0 w-16 h-16 flex items-center justify-center bg-[#ecf0f3] rounded-xl shadow-[inset_-2px_-2px_5px_#FFF,inset_2px_2px_5px_#d1d9e6] p-2 mr-4 overflow-hidden group-hover:shadow-[inset_-1px_-1px_2px_#FFF,inset_1px_1px_2px_#d1d9e6] transition-shadow duration-200">
          <img :src="link.avatar" draggable="false" :alt="link.name" class="w-full h-full object-contain rounded-lg">
        </div>
        <!-- 网站信息 -->
        <div class="grow flex flex-col justify-center min-w-0">
          <div
              class="text-lg font-bold truncate text-[#4a4f5f] mb-1 group-hover:text-[#313541] transition-colors duration-200">
            {{ link.name }}
          </div>
          <div class="text-sm text-[#7e8599] line-clamp-2 leading-snug">
            {{ link.desc }}
          </div>
        </div>
      </a>
    </div>

    <!-- 申请友链说明 -->
    <div
        class="mt-16 mb-20 p-8 rounded-3xl bg-[#ecf0f3] shadow-[-10px_-10px_30px_#FFF,10px_10px_30px_#d1d9e6] max-w-2xl w-[calc(100%-2rem)] mx-4">
      <h3 class="text-xl font-bold mb-6 text-[#4a4f5f] flex items-center">
        <span class="mr-2 text-2xl">📝</span> 申请友情链接
      </h3>
      <div class="space-y-4 text-[#61677C]">
        <p class="leading-relaxed">欢迎各位小伙伴交换友链，只要是内容健康、积极向上的技术类或生活类博客/站点均可。</p>
        <div class="bg-white/40 p-5 rounded-xl border border-white/60 shadow-inner">
          <p class="font-mono text-sm space-y-1">
            <span class="block text-blue-600/70 font-bold mb-1">// 本站信息</span>
            名称：{{ linkSetting.link_name }}<br>
            简介：{{ linkSetting.link_desc }}<br>
            链接：{{ linkSetting.link_url }}<br>
            图标：{{ linkSetting.link_icon }}
          </p>
        </div>
        <div class="pt-2">
          <p class="text-sm opacity-90">
            申请方式：发送邮件至 <a :href="'mailto:' + linkSetting.link_email"
                                   class="text-blue-500 hover:underline font-semibold">{{ linkSetting.link_email }}</a>，并在邮件中附上您的站点信息。
          </p>
        </div>
      </div>
    </div>
  </div>

  <div class="backHome" @click="router.push('/')">
    <PhHouseLine size="24" weight="bold"/>
  </div>
</template>


<style lang="scss" scoped>
.box {
  user-select: none;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  transition: all 0.2s ease-in-out;
}

.box:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
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
</style>