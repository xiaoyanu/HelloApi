<script setup lang="ts">
import {onMounted, ref, reactive} from "vue";
import {GetSettingList, UpdateSettingValue} from "@/api";

const setting = ref({
  register: true,
  api: true,
  api_key: true,
  user_key: true,
  link_name: "HelloAPI",
  link_desc: "一个简单、稳定、快速的 API 接口站",
  link_url: "https://api.zxz.ee/",
  link_icon: "https://api.zxz.ee/favicon.ico",
  link_email: "zxz.ee@qq.com",
})

// --- Loading 状态控制 ---
// 全局页面初次渲染的加载状态
const globalLoading = ref(false)

// 各个 Switch 开关独立操作时的加载状态
const switchLoadings = reactive({
  register: false,
  api: false,
  api_key: false,
  user_key: false,
})

const linkDialogVisible = ref(false)
const linkFormLoading = ref(false)

const getSettingValue = async () => {
  globalLoading.value = true
  try {
    const res = await GetSettingList()
    if (res.data.code == 200) {
      const data = res.data.data
      for (const item of data) {
        if (item.key in setting.value) {
          const key = item.key as keyof typeof setting.value;
          // 处理布尔值和字符串
          if (typeof setting.value[key] === 'boolean') {
            (setting.value as any)[key] = (item.value === 'true' || item.value === true);
          } else {
            (setting.value as any)[key] = item.value;
          }
        }
      }
    }
  } finally {
    globalLoading.value = false
  }
}

const updateSettingValue = async (key: keyof typeof setting.value, value: any) => {
  if (key in switchLoadings) {
    (switchLoadings as any)[key] = true
  }
  try {
    const res = await UpdateSettingValue(key, value)
    if (res.data.code == 200) {
      ElMessage.success("设置成功")
      return true
    } else {
      if (typeof value === 'boolean') {
        (setting.value as any)[key] = !value
      }
    }
  } catch (error) {
    if (typeof value === 'boolean') {
      (setting.value as any)[key] = !value
    }
  } finally {
    if (key in switchLoadings) {
      (switchLoadings as any)[key] = false
    }
  }
  return false
}

const handleUpdateLinkInfo = async () => {
  linkFormLoading.value = true
  try {
    const keys: (keyof typeof setting.value)[] = ['link_name', 'link_desc', 'link_url', 'link_icon', 'link_email']
    const promises = keys.map(key => UpdateSettingValue(key, setting.value[key]))
    await Promise.all(promises)
    ElMessage.success("友人帐配置成功")
    linkDialogVisible.value = false
  } catch (e) {
    ElMessage.error("配置失败")
  } finally {
    linkFormLoading.value = false
  }
}

onMounted(() => {
  void getSettingValue()
})
</script>

<template>
  <admin-main-body title="全局设置">
    <el-form v-loading="globalLoading" label-width="120px" label-position="left" class="mt-4">
      <el-form-item label="开放注册">
        <div class="flex items-center w-full justify-between">
          <span class="text-gray-400 text-sm">开启后，允许新用户在登录页自助注册账号</span>
          <el-switch
              @change="updateSettingValue('register', setting.register)"
              v-model="setting.register"
              :loading="switchLoadings.register"
          />
        </div>
      </el-form-item>
      <el-form-item label="发布API">
        <div class="flex items-center w-full justify-between">
          <span
              class="text-gray-400 text-sm">开启后，允许用户发布、修改、删除用户自己所发布的API  (已有的不受此设置约束)</span>
          <el-switch
              @change="updateSettingValue('api', setting.api)"
              v-model="setting.api"
              :loading="switchLoadings.api"
          />
        </div>
      </el-form-item>
      <el-form-item label="创建APIKey">
        <div class="flex items-center w-full justify-between">
          <span
              class="text-gray-400 text-sm">开启后，允许用户创建、修改、删除用户自己所发布的APIKey (已有的不受此设置约束)</span>
          <el-switch
              @change="updateSettingValue('api_key', setting.api_key)"
              v-model="setting.api_key"
              :loading="switchLoadings.api_key"
          />
        </div>
      </el-form-item>
      <el-form-item label="用户密钥">
        <div class="flex items-center w-full justify-between">
          <span class="text-gray-400 text-sm">开启后，允许用户重置用户密钥</span>
          <el-switch
              @change="updateSettingValue('user_key', setting.user_key)"
              v-model="setting.user_key"
              :loading="switchLoadings.user_key"
          />
        </div>
      </el-form-item>
      <el-form-item label="友人帐配置">
        <div class="flex flex-col sm:flex-row sm:items-center w-full justify-between gap-2">
          <span class="text-gray-400 text-sm">设置友人帐页面的申请友链显示的信息（名称、简介、链接、图标、邮箱）</span>
          <el-button type="primary" class="w-fit" @click="linkDialogVisible = true">设置</el-button>
        </div>
      </el-form-item>
    </el-form>

    <el-dialog v-model="linkDialogVisible" title="友人帐配置" width="90%" class="max-w-125">
      <el-form :model="setting" label-position="top" v-loading="linkFormLoading">
        <el-form-item label="本站名称">
          <el-input v-model="setting.link_name" placeholder="请输入名称"/>
        </el-form-item>
        <el-form-item label="本站简介">
          <el-input v-model="setting.link_desc" placeholder="请输入简介"/>
        </el-form-item>
        <el-form-item label="本站链接">
          <el-input v-model="setting.link_url" placeholder="请输入链接"/>
        </el-form-item>
        <el-form-item label="本站图标">
          <el-input v-model="setting.link_icon" placeholder="请输入图标链接"/>
        </el-form-item>
        <el-form-item label="联系邮箱">
          <el-input v-model="setting.link_email" placeholder="请输入邮箱"/>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="flex justify-end gap-2">
          <el-button @click="linkDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleUpdateLinkInfo">确认</el-button>
        </div>
      </template>
    </el-dialog>
  </admin-main-body>
</template>

<style scoped lang="scss">

</style>