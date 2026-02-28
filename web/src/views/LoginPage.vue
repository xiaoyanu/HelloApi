<script setup lang="ts">
import {ref, watch, onMounted} from 'vue'
import {GetCaptchaUrl, Login, Register} from "@/api";
import {useUserStore} from "@/stores";
import {useRouter} from "vue-router";
import type {FormRules} from "element-plus";

const userStore = useUserStore();
const router = useRouter()

const isLogin = ref(true)
// 校验表单
const formRef = ref()
const userForm = ref({
  name: '',
  password: '',
  confirmPassword: '',
  code: '',
  mail: ''
})

// 验证码响应式 URL
const captchaUrl = ref('')

// 刷新验证码的方法
const refreshCaptcha = () => {
  captchaUrl.value = GetCaptchaUrl()
}

// 表单校验规则
const rules: FormRules = {
  name: [
    {required: true, message: '请输入账号', trigger: 'blur'},
    {pattern: /^[a-zA-Z0-9]{4,10}$/, message: '账号为4-10位数字或字母', trigger: 'change'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {pattern: /^\S{6,15}$/, message: '密码为6-15位非空字符', trigger: 'change'},
    {
      validator: (_: any, val: any, cb: any) => (val === userForm.value.name ? cb(new Error('账号密码不能相同')) : cb()),
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    {required: true, message: '请确认密码', trigger: 'blur'},
    {
      validator: (_: any, val: any, cb: any) => (val !== userForm.value.password ? cb(new Error('两次输入密码不一致')) : cb()),
      trigger: 'change'
    }
  ],
  code: [
    {required: true, message: '请输入验证码', trigger: 'blur'},
    // 固定的 4 位长度直接写在正则里
    {pattern: /^[a-zA-Z0-9]{4}$/, message: '验证码为4位数字或字母', trigger: 'change'}
  ],
  mail: [
    {required: true, message: '请输入邮箱', trigger: 'blur'},
    // Element 内置支持 type: 'email'，无需手写长正则
    {type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change']}
  ]
}

// 初始化加载验证码
onMounted(() => {
  refreshCaptcha()
})

// 监控登录/注册状态切换
watch(isLogin, () => {
  // 重置表单
  userForm.value = {
    name: '',
    password: '',
    confirmPassword: '',
    code: '',
    mail: ''
  }
  // 清除校验状态，避免切换时爆红
  formRef.value?.clearValidate()
  // 切换模式时自动刷新验证码
  refreshCaptcha()
})

// 注册提交
const register = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    const res = await Register(userForm.value)
    if (res.data.code !== 200) {
      userForm.value.code = ''
      refreshCaptcha()
      return
    }
    isLogin.value = !isLogin.value
    ElMessage.success('注册成功')
  }
}

// 登录提交
const login = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    const res = await Login(userForm.value)
    if (res.data.code !== 200) {
      userForm.value.code = ''
      refreshCaptcha()
      return
    }
    userStore.setToken(res.data.data.token)
    userStore.setUser(res.data.data.user)
    // 跳转到后台首页
    void router.push('/admin')
    ElMessage.success('登录成功')
  }
}
</script>

<template>
  <div class="min-h-screen flex w-full bg-white font-sans antialiased">
    <div
        class="select-none hidden lg:flex lg:w-1/2 bg-[#fafafa] relative items-center justify-center overflow-hidden border-r border-[#e4e4e7]">
      <div class="absolute inset-0 z-0 opacity-[0.5]"
           style="background-image: linear-gradient(#e4e4e7 1px, transparent 1px), linear-gradient(90deg, #e4e4e7 1px, transparent 1px); background-size: 40px 40px;">
      </div>

      <div class="absolute top-0 left-1/2 -translate-x-1/2 w-full h-125 bg-linear-to-b from-white to-transparent"></div>

      <div class="relative z-10 px-16 max-w-2xl">
        <div class="flex items-center gap-3 mb-8">
          <img src="@/assets/images/logo.png" alt="logo" class="w-10 h-10" draggable="false"/>
          <span
              class="bg-linear-to-r from-[#ff4e50] to-[#f9d423] bg-clip-text text-transparent h-15 flex items-center justify-center text-[28px] font-bold">
            HelloAPI
          </span>
        </div>

        <h1 class="text-5xl font-extrabold text-[#18181B] leading-[1.1] mb-6">
          轻量化API管理<br>快捷操控。
        </h1>
        <p class="text-[#71717B] text-lg leading-relaxed font-light max-w-md">
          专为开发者设计的 API 基础设施，稳定、快速。
        </p>

        <div class="mt-12 flex items-center gap-8">
          <div class="flex flex-col">
            <span class="text-[#18181B] font-mono text-2xl font-bold">99.9%</span>
            <span class="text-[#9F9FA9] text-xs uppercase tracking-widest font-semibold mt-1">可用性保证</span>
          </div>
          <div class="w-px h-10 bg-zinc-200"></div>
          <div class="flex flex-col">
            <span class="text-[#18181B] font-mono text-2xl font-bold">&lt;30ms</span>
            <span class="text-[#9F9FA9] text-xs uppercase tracking-widest font-semibold mt-1">平均延迟</span>
          </div>
        </div>
      </div>
    </div>

    <div class="select-none w-full lg:w-1/2 flex items-center justify-center p-8 sm:p-16 bg-white">
      <div class="w-full max-w-100">

        <div class="mb-10">
          <h2 class="text-3xl font-bold text-[#18181B] tracking-tight">
            {{ isLogin ? '欢迎回来' : '创建新账户' }}
          </h2>
          <p class="mt-3 text-[#71717B] text-sm">
            {{ isLogin ? '请登录您的账号以继续。' : '只需几秒钟即可开启您的 API 旅程。' }}
          </p>
        </div>

        <div class="relative">
          <div :key="isLogin ? 'login' : 'register'" class="w-full">
            <el-form
                :model="userForm"
                ref="formRef"
                :rules="rules"
                label-position="top"
            >
              <el-form-item prop="name">
                <template #label>
                  <span class="text-[#71717a] font-bold text-[12px] uppercase tracking-wider ml-1">账号</span>
                </template>
                <el-input
                    v-model="userForm.name"
                    placeholder="请输入账号"
                />
              </el-form-item>

              <el-form-item v-if="!isLogin" prop="mail">
                <template #label>
                  <span class="text-[#71717a] font-bold text-[12px] uppercase tracking-wider ml-1">邮箱</span>
                </template>
                <el-input
                    v-model="userForm.mail"
                    placeholder="请输入邮箱"
                />
              </el-form-item>

              <el-form-item prop="password">
                <template #label>
                  <span class="text-[#71717a] font-bold text-[12px] uppercase tracking-wider ml-1">密码</span>
                </template>
                <el-input
                    v-model="userForm.password"
                    type="password"
                    placeholder="请输入密码"
                    show-password
                />
              </el-form-item>

              <el-form-item prop="confirmPassword" v-if="!isLogin">
                <template #label>
                  <span class="text-[#71717a] font-bold text-[12px] uppercase tracking-wider ml-1">确认密码</span>
                </template>
                <el-input
                    v-model="userForm.confirmPassword"
                    type="password"
                    placeholder="请再输入一次密码"
                    show-password
                />
              </el-form-item>

              <el-form-item prop="code">
                <template #label>
                  <span class="text-[#71717a] font-bold text-[12px] uppercase tracking-wider ml-1">验证码</span>
                </template>
                <div class="flex items-center gap-3 w-full">
                  <el-input
                      v-model="userForm.code"
                      placeholder="验证码"
                      class="flex-1"
                      @keyup.enter="isLogin ? login() : register()"
                  />
                  <img :src="captchaUrl" alt="验证码" @click="refreshCaptcha"
                       draggable="false"
                       class="h-10 border border-[#e4e4e7] cursor-pointer rounded-[10px] shrink-0 hover:opacity-80 transition-opacity">
                </div>
              </el-form-item>

              <el-button
                  @click="isLogin ? login() : register()"
                  class="w-full h-12! bg-[#18181b]! hover:bg-black! text-white! border-none! rounded-xl! text-base font-bold shadow-[0_20px_25px_-5px_rgba(228,228,231,0.5)] transition-all mt-4"
              >
                {{ isLogin ? '登 录' : '注 册' }}
              </el-button>
            </el-form>
          </div>
        </div>

        <div class="mt-10 text-center border-t border-[#F4F4F5] pt-8">
          <p class="text-sm text-[#71717B]">
            {{ isLogin ? '还没有账号？' : '已有账号？' }}
            <button
                class="font-bold text-[#18181B] hover:underline underline-offset-4 ml-1 transition-all"
                @click="isLogin = !isLogin"
            >
              {{ isLogin ? '立即创建' : '返回登录' }}
            </button>
          </p>
        </div>

      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">

/* 输入框基础样式与边框：参考 custom-input */
:deep(.el-input__wrapper) {
  border-radius: 10px !important;
  background-color: #ffffff !important;
  box-shadow: 0 0 0 1px #e4e4e7 inset !important;
  padding: 0 12px !important;
  transition: all 0.2s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 输入框悬浮态 */
:deep(.el-input__wrapper:hover) {
  box-shadow: 0 0 0 1px #d4d4d8 inset !important;
}

/* 输入框聚焦态：深灰边框 + 外侧浅灰发光 */
:deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #18181b inset !important;
}

:deep(.el-form-item__label:before) {
  display: none;
}

:deep(.el-form-item__error) {
  padding-left: 4px;
}

/* 控制内部输入内容的高度和字体 */
:deep(.el-input__inner) {
  height: 40px !important;
  font-size: 14px !important;
  color: #18181b !important;
}
</style>