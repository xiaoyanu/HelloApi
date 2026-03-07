<script setup lang="ts">
import { onMounted, ref, reactive } from "vue";
import { GetSettingValue, UpdateSettingValue } from "@/api";

const setting = ref({
  register: true,
  api: true,
  api_key: true,
  user_key: true,
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

const getSettingValue = async () => {
  globalLoading.value = true
  try {
    const res = await GetSettingValue()
    if (res.data.code == 200) {
      const data = res.data.data
      for (const item of data) {
        if (item.key in setting.value) {
          const key = item.key as keyof typeof setting.value;
          console.log(item.value)
          setting.value[key] = item.value;
        }
      }
    }
  } finally {
    globalLoading.value = false
  }
}

const updateSettingValue = async (key: keyof typeof setting.value, value: boolean) => {
  switchLoadings[key] = true
  try {
    const res = await UpdateSettingValue(key, value)
    if (res.data.code == 200) {
      ElMessage.success("设置成功")
    } else {
      // 如果后端返回非 200 状态，回滚开关的 UI 状态
      setting.value[key] = !value
    }
  } catch (error) {
    setting.value[key] = !value
  } finally {
    switchLoadings[key] = false
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
          <span class="text-gray-400 text-sm">开启后，允许用户发布、修改、删除用户自己所发布的API  (已有的不受此设置约束)</span>
          <el-switch
              @change="updateSettingValue('api', setting.api)"
              v-model="setting.api"
              :loading="switchLoadings.api"
          />
        </div>
      </el-form-item>
      <el-form-item label="创建APIKey">
        <div class="flex items-center w-full justify-between">
          <span class="text-gray-400 text-sm">开启后，允许用户创建、修改、删除用户自己所发布的APIKey (已有的不受此设置约束)</span>
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
    </el-form>
  </admin-main-body>
</template>

<style scoped lang="scss">

</style>