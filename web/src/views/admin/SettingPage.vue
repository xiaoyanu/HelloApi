<script setup lang="ts">
import {onMounted, ref} from "vue";
import {GetSettingValue, UpdateSettingValue} from "@/api";

const setting = ref({
  register: true,
  api: true,
  api_key: true,
  user_key: true,
})

const getSettingValue = async () => {
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
}

const updateSettingValue = async (key: string, value: boolean) => {
  const res = await UpdateSettingValue(key, value)
  if (res.data.code == 200) {
    ElMessage.success("设置成功")
  }
}

onMounted(() => {
  getSettingValue()
})
</script>

<template>
  <div class="rounded-lg bg-white p-6 shadow-sm">
    <h2 class="text-lg font-medium">全局设置</h2>
    <el-divider/>
    <el-form label-width="120px" label-position="left" class="mt-4">
      <el-form-item label="开放注册">
        <div class="flex items-center w-full justify-between">
          <span class="text-gray-400 text-sm">开启后，允许新用户在登录页自助注册账号</span>
          <el-switch @change="updateSettingValue('register',setting.register)" v-model="setting.register"/>
        </div>
      </el-form-item>
      <el-form-item label="发布API">
        <div class="flex items-center w-full justify-between">
          <span
              class="text-gray-400 text-sm">开启后，允许用户发布、修改、删除用户自己所发布的API  (已有的不受此设置约束)</span>
          <el-switch @change="updateSettingValue('api',setting.api)" v-model="setting.api"/>
        </div>
      </el-form-item>
      <el-form-item label="创建APIKey">
        <div class="flex items-center w-full justify-between">
          <span
              class="text-gray-400 text-sm">开启后，允许用户创建、修改、删除用户自己所发布的APIKey (已有的不受此设置约束)</span>
          <el-switch @change="updateSettingValue('api_key',setting.api_key)" v-model="setting.api_key"/>
        </div>
      </el-form-item>
      <el-form-item label="用户密钥">
        <div class="flex items-center w-full justify-between">
          <span class="text-gray-400 text-sm">开启后，允许用户重置用户密钥</span>
          <el-switch @change="updateSettingValue('user_key',setting.user_key)" v-model="setting.user_key"/>
        </div>
      </el-form-item>
    </el-form>
  </div>
</template>

<style scoped lang="scss">

</style>