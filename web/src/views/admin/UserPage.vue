<script setup lang="ts">
import { onMounted, reactive, ref } from 'vue'
import { useUserStore } from "@/stores";
import type { FormInstance, FormRules } from "element-plus";
import { DocumentCopy, Edit } from "@element-plus/icons-vue";
import type { User } from "@/types";
import { GetUserInfo, GetUserKey, RestUserKey, UpdateUserMail, UpdateUserNick, UpdateUserPassword } from "@/api";
import { useRouter } from "vue-router"
import { copyText } from "@/utils";

const router = useRouter()
const userStore = useUserStore()

// --- Loading 状态控制 ---
const globalLoading = ref(false) // 页面初始数据加载
const updateNickLoading = ref(false) // 昵称保存加载
const updateMailLoading = ref(false) // 邮箱保存加载
const resetKeyLoading = ref(false) // 重置密钥加载
const updatePwdLoading = ref(false) // 修改密码加载

// 用户信息
const userInfo = ref<User>({
  id: -1,
  nick: '',
  mode: -1,
  username: '',
  mail: '',
  key: ''
})

// 修改密码对话框
const dialogVisible = ref(false)
// 修复点：将主表单和密码表单的 ref 拆分开，避免冲突覆盖
const userFormRef = ref<FormInstance>()
const pwdFormRef = ref<FormInstance>()

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const clearForm = () => {
  passwordForm.value = {
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
  }
}

// 校验规则
const rulesPwd = reactive<FormRules>({
  oldPassword: [{ required: true, message: '请输入旧密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    {
      validator: (_, value, callback) => {
        if (value == passwordForm.value.oldPassword) {
          callback(new Error('新密码旧密码不能相同'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (_, value, callback) => {
        if (value !== passwordForm.value.newPassword) {
          callback(new Error('两次输入的密码不一致'))
        } else {
          callback()
        }
      },
      trigger: 'blur'
    }
  ]
})

const rulesUser = reactive<FormRules>({
  nick: [
    { required: true, message: '不能为空', trigger: 'blur' },
    { max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur' }
  ],
  mail: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: ['blur', 'change'] }
  ]
})

// 打开修改密码对话框
const openPasswordDialog = () => {
  dialogVisible.value = true
}

// resetUserKey
const resetUserKey = (userId: number) => {
  ElMessageBox.confirm('确定要重置用户密钥吗？之前的密钥会立即失效，请谨慎操作！', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    resetKeyLoading.value = true
    try {
      const res = await RestUserKey(userId)
      if (res.data.code == 200) {
        await getUserKey()
        ElMessage.success('密钥重置成功')
      }
    } finally {
      resetKeyLoading.value = false
    }
  })
}

// 获取用户密钥
const getUserKey = async () => {
  const res = await GetUserKey()
  if (res.data.code == 200) {
    userInfo.value.key = res.data.data.key;
  }
}

// 提交修改密码
const submitPasswordForm = async () => {
  const isValid = await pwdFormRef.value?.validate().catch(() => false);
  if (isValid) {
    ElMessageBox.confirm('确定要修改密码吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }).then(async () => {
      updatePwdLoading.value = true
      try {
        const res = await UpdateUserPassword(passwordForm.value.oldPassword, passwordForm.value.newPassword)
        if (res.data.code == 200) {
          ElMessage.success('密码修改成功！请重新登录')
          dialogVisible.value = false
          userStore.removeAll()
          void router.push({ name: 'AdminLogin' })
        }
      } finally {
        updatePwdLoading.value = false
      }
    })
  }
}

// 获取用户信息
const getUserInfo = async () => {
  const res = await GetUserInfo()
  if (res.data.code === 200) {
    // 防止响应式修改
    userStore.setUser({ ...res.data.data })
    userInfo.value = { ...res.data.data }
  }
}

// 更新昵称
const updateUserNick = async () => {
  // 可以优化为部分校验：仅校验 'nick' 字段，这里先沿用全表单校验
  const isValid = await userFormRef.value?.validate().catch(() => false);
  if (isValid) {
    updateNickLoading.value = true
    try {
      const res = await UpdateUserNick(userInfo.value.nick || '')
      if (res.data.code === 200) {
        await getUserInfo()
        ElMessage.success('昵称修改成功')
      }
    } finally {
      updateNickLoading.value = false
    }
  }
}

// 更新邮箱
const updateUserMail = async () => {
  const isValid = await userFormRef.value?.validate().catch(() => false);
  if (isValid) {
    updateMailLoading.value = true
    try {
      const res = await UpdateUserMail(userInfo.value.mail || '')
      if (res.data.code === 200) {
        await getUserInfo()
        ElMessage.success('邮箱修改成功')
      }
    } finally {
      updateMailLoading.value = false
    }
  }
}

onMounted(async () => {
  globalLoading.value = true
  try {
    await Promise.all([getUserInfo(), getUserKey()])
  } finally {
    globalLoading.value = false
  }
})
</script>

<template>
  <admin-main-body title="我的信息">
    <template #default>
      <el-form
          v-loading="globalLoading"
          :model="userInfo"
          label-position="right"
          label-width="50px"
          class="max-w-lg lg:ml-5"
          :rules="rulesUser"
          ref="userFormRef"
      >
        <el-form-item label="权限">
          <el-tag :type="userInfo.mode==0?'primary':'danger'" size="large">{{
              userInfo.mode == 0 ? "普通用户" : "管理员"
            }}
          </el-tag>
        </el-form-item>
        <el-form-item label="UID">
          <el-input v-model="userInfo.id" disabled/>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="userInfo.username" disabled/>
        </el-form-item>
        <el-form-item label="头像">
          <el-input disabled placeholder="根据邮箱获取Gravatar头像"/>
        </el-form-item>
        <el-form-item label="昵称" prop="nick">
          <el-input v-model="userInfo.nick">
            <template #append v-if="userInfo.nick !== userStore.user.nick" class="save-btn-container">
              <el-button @click="updateUserNick" :loading="updateNickLoading">
                <el-icon v-if="!updateNickLoading"><Edit/></el-icon>
                &nbsp;保存
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="mail">
          <el-input v-model="userInfo.mail">
            <template #append v-if="userInfo.mail !== userStore.user.mail" class="save-btn-container">
              <el-button @click="updateUserMail" :loading="updateMailLoading">
                <el-icon v-if="!updateMailLoading"><Edit/></el-icon>
                &nbsp;保存
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="密钥" prop="key">
          <el-input placeholder="重置密钥生成" v-model="userInfo.key" disabled>
            <template #append>
              <el-button @click="copyText(userInfo.key)">
                <el-icon><DocumentCopy/></el-icon>
                &nbsp;复制
              </el-button>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item></el-form-item>
        <el-form-item>
          <el-button
              type="warning"
              plain
              @click="resetUserKey(userStore.user.id as number)"
              :loading="resetKeyLoading"
          >
            重置密钥
          </el-button>
          <el-button type="primary" plain @click="openPasswordDialog">修改密码</el-button>
        </el-form-item>
      </el-form>

      <el-dialog
          v-model="dialogVisible"
          title="修改密码"
          width="500px"
          align-center
          @closed="clearForm"
      >
        <el-form
            ref="pwdFormRef"
            :model="passwordForm"
            :rules="rulesPwd"
            label-position="right"
            label-width="100px"
            status-icon
        >
          <el-form-item label="旧密码" prop="oldPassword">
            <el-input v-model="passwordForm.oldPassword" type="password" show-password/>
          </el-form-item>
          <el-form-item label="新密码" prop="newPassword">
            <el-input v-model="passwordForm.newPassword" type="password" show-password/>
          </el-form-item>
          <el-form-item label="确认新密码" prop="confirmPassword">
            <el-input v-model="passwordForm.confirmPassword" type="password" show-password/>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取消</el-button>
            <el-button type="primary" @click="submitPasswordForm" :loading="updatePwdLoading">
              确定
            </el-button>
          </span>
        </template>
      </el-dialog>
    </template>
  </admin-main-body>
</template>

<style scoped>
.el-input-group__append .el-button {
  background: var(--el-input-bg-color, var(--el-fill-color-blank));
  border-radius: 0 var(--el-input-border-radius) var(--el-input-border-radius) 0;
  border-color: #DCDFE6;
  border-left: 0;
}

/* 隐藏表单标签的伪元素 */
:deep(.el-form-item__label:before) {
  display: none;
}
</style>