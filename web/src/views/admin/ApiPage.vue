<script setup lang="ts">
import {Plus, Delete, Edit, Close} from "@element-plus/icons-vue";
import {GetUserAppList} from "@/api/user.ts";
import {onMounted, ref} from "vue";
import type {AppList, App} from "@/types/module/user.ts";
import type {FormRules} from "element-plus";
import {dayjs} from "element-plus";
import {CreateApi} from "@/api/api.ts";
import {useUserStore} from "@/stores";

const userStore = useUserStore();
const tableData = ref<AppList[]>();

// 抽屉
const showDrawer = ref(false)
const drawerType = ref()
const drawerTitle = ref()
const resizable = ref(true) // 是否可调整大小
const drawerSize = ref('80%') // 抽屉大小
const newRow = ref()

// 提交表单
const formRef = ref()
const formData = ref<App>({
  title: '',
  smallTitle: '',
  status: 0,
  type: 0,
  url: '',
  sendType: 0,
  returnType: '',
  returnContent: '',
  params: [],
  view_status: 2,
});

// 添加请求参数
const addParam = () => {
  formData.value.params.push({
    name: '',
    required: 0,
    type: '',
    msg: '',
  });
};

// 删除请求参数
const removeParam = (index: number) => {
  formData.value.params.splice(index, 1);
};

// 获取用户发布的API接口列表
const getTableData = async () => {
  const res = await GetUserAppList();
  if (res.data.code == 200) {
    tableData.value = res.data.data;
  }
}

// 表单校验


// 重置formData
const resetFormData = () => {
  formData.value = {
    title: '',
    smallTitle: '',
    status: 0,
    type: 0,
    url: '',
    sendType: 0,
    returnType: '',
    returnContent: '',
    params: [],
    view_status: 2,
  }
}

// 打开抽屉
const openDrawer = (type: string, row?: object) => {
  resetFormData()
  drawerType.value = type
  if (drawerType.value === 'edit') {
    newRow.value = row
  }
  drawerTitle.value = type === 'create' ? '发布接口' : '编辑接口'
  showDrawer.value = true
}

// 发布插件
const submitForm = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    // 提交表单
    if (userStore.user.mode || 0 > 0) {
      formData.value.view_status = 0
    }
    const res = await CreateApi(formData.value)
    if (res.data.code == 200) {
      void getTableData()
      ElMessage.success('发布成功')
      showDrawer.value = false
    }
  }
}

// 表单校验
const rules: FormRules = {
  type: [
    {required: true, message: '请选择接口类型', trigger: 'change'}
  ],
  status: [
    {required: true, message: '请选择接口状态', trigger: 'change'}
  ],
  title: [
    {required: true, message: '请输入接口名称', trigger: 'blur'},
    {min: 1, max: 20, message: '长度在 1 到 20 个字符', trigger: 'blur'}
  ],
  smallTitle: [
    {required: true, message: '请输入接口描述', trigger: 'blur'}
  ],
  url: [
    {required: true, message: '请输入接口地址', trigger: 'blur'},
    {
      pattern: /^(https?:\/\/|\/|www\.).+/,
      message: '请输入正确的 URL 或相对路径 (如 /api/xxx)',
      trigger: 'blur'
    }
  ],
  sendType: [
    {required: true, message: '请选择请求方式', trigger: 'change'}
  ],
  returnType: [
    {required: true, message: '请输入返回类型', trigger: 'blur'}
  ],
  returnContent: [
    {required: true, message: '请输入返回示例内容', trigger: 'change'}
  ]
}
onMounted(() => {
  getTableData();
})
</script>
<template>
  <div class="rounded-lg bg-white p-6 shadow-sm">
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-medium">管理API接口</h2>
      <el-button type="primary" :icon="Plus" @click="openDrawer('create')">发布API</el-button>
    </div>
    <hr class="border-[#E5E5E5] m-6"/>
    <el-table :data="tableData" class="w-full">
      <el-table-column label="接口名称">
        <template #default="{row}">
          <el-link
              :href="'/info/' + row.title"
              target="_blank"
              type="primary"
              :underline="false"
          >
            {{ row.title }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="类型">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'warning' : 'success'">
            {{ row.type === 1 ? '收费' : '免费' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建日期">
        <template #default="{ row }">
          {{ dayjs(row.created).format('YYYY年MM月DD日 HH:mm') }}
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 2 ? 'info' : row.status === 1 ? 'danger' : 'success'">
            {{ row.status === 2 ? '维护' : row.status === 1 ? '异常' : '正常' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="120">
        <template #default="{ row }">
          <el-button
              :icon="Edit"
              circle
              plain
              type="primary"
          ></el-button>
          <el-button
              :icon="Delete"
              circle
              plain
              type="danger"
          ></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty style="user-select: none" description="没有发布过API接口"/>
      </template>
    </el-table>
    <div class="flex items-center justify-center mt-6">
      <el-pagination
          layout="prev, pager, next"
          :total="50"
      />
    </div>


    <!-- 抽屉 -->
    <el-drawer
        class="rounded-tl-[20px] rounded-tr-[20px] m-auto custom-btt-drawer"
        v-model="showDrawer"
        :title="drawerTitle"
        direction="btt"
        :size="drawerSize"
        :resizable="resizable"
        :destroy-on-close="true"
    >
      <div class="m-auto max-w-[90%]">
        <el-form :model="formData" :rules="rules" ref="formRef">
          <el-form-item label="接口类型" prop="type">
            <el-radio-group v-model="formData.type">
              <el-radio-button label="免费" :value="0"/>
              <el-radio-button label="收费" :value="1"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="接口状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio-button label="正常" :value="0"/>
              <el-radio-button label="异常" :value="1"/>
              <el-radio-button label="维护" :value="2"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="接口名称" prop="title">
            <el-input v-model="formData.title" placeholder="请输入接口名称，例如：随机一言"/>
          </el-form-item>
          <el-form-item label="接口描述" prop="smallTitle">
            <el-input v-model="formData.smallTitle" placeholder="请输入接口描述，例如：返回随机一条名言、台词"/>
          </el-form-item>
          <el-form-item label="接口地址" prop="url">
            <el-input v-model="formData.url" placeholder="请输入接口地址，例如：/api/demo 或 https://example.com/random"/>
          </el-form-item>
          <el-form-item label="请求方式" prop="sendType">
            <el-radio-group v-model="formData.sendType">
              <el-radio-button label="Get" :value="0"/>
              <el-radio-button label="Post" :value="1"/>
              <el-radio-button label="Put" :value="2"/>
              <el-radio-button label="Delete" :value="3"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="返回类型" prop="returnType">
            <el-input v-model="formData.returnType" placeholder="请输入返回类型，例如：Json / Text / Image"/>
          </el-form-item>

          <el-form-item label="请求参数">
            <div class="rounded-sm overflow-hidden border border-[#DCDFE6] w-full">
              <el-table class="table-4px-border" :data="formData.params" style="width: 100%">
                <el-table-column label="参数名" width="180">
                  <template #default="{row}">
                    <el-input v-model="row.name" placeholder="参数名"/>
                  </template>
                </el-table-column>
                <el-table-column label="必填" width="85">
                  <template #default="{row}">
                    <el-select v-model="row.required">
                      <el-option label="否" :value="0"/>
                      <el-option label="是" :value="1"/>
                    </el-select>
                  </template>
                </el-table-column>
                <el-table-column label="类型" width="150">
                  <template #default="{row}">
                    <el-input v-model="row.type" placeholder="参数类型"/>
                  </template>
                </el-table-column>
                <el-table-column label="描述">
                  <template #default="{row}">
                    <el-input type="textarea" autosize v-model="row.msg" placeholder="参数描述"/>
                  </template>
                </el-table-column>
                <el-table-column label="操作" width="70" align="center">
                  <template #default="scope">
                    <el-button
                        type="danger"
                        :icon="Close"
                        circle
                        size="small"
                        @click="removeParam(scope.$index)"
                    />
                  </template>
                </el-table-column>
                <template #empty>
                  没有参数
                </template>
              </el-table>
            </div>
            <el-button type="primary" plain @click="addParam" style="margin-top: 10px;">
              添加参数
            </el-button>
          </el-form-item>
          <el-form-item label="返回示例" prop="returnContent">
            <MDEdit v-model="formData.returnContent"/>
          </el-form-item>
        </el-form>
        <div class="flex justify-center">
          <el-button type="primary" size="large" class="w-50" @click="submitForm">
            提交
          </el-button>
        </div>
      </div>

    </el-drawer>
  </div>
</template>
<style scoped>
/* 隐藏表单标签的伪元素 */
:deep(.el-form-item__label:before) {
  display: none;
}

:deep(.custom-btt-drawer) {
  width: 80%;
}

/* 移除表格 hover 时的背景颜色变化 */
:deep(.el-table--enable-row-hover .el-table__row:hover > td.el-table__cell) {
  background-color: transparent !important;
}

@media (max-width: 768px) {
  :deep(.custom-btt-drawer) {
    width: 100%;
  }
}
</style>