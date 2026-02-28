<script lang="ts" setup>
import {Plus, Delete, Edit, Close, Search, Refresh} from "@element-plus/icons-vue";
import {GetUserAppList, GetApiInfo, CreateApi, UpdateApi, UserAppListSearch, DeleteApi} from "@/api";
import {onMounted, ref} from "vue";
import type {AppList, App, Pagination, selectFormApi} from "@/types";
import type {FormRules} from "element-plus";
import {dayjs} from "element-plus";
import {useUserStore} from "@/stores";
import {HelloAPIConfig} from "@/config/config.ts";

const userStore = useUserStore();
const tableData = ref<AppList[]>();
const nowTableType = ref()

// 搜索筛选表单
const searchForm = ref<selectFormApi>({
  keywords: '',
  type: -1, // -1 代表不限，0 免费，1 收费
  status: -1, // -1 代表不限，0 正常，1 异常，2 维护
  view_status: -1 // -1 代表不限，0 通过，1 拒绝，2 审核中
})

// 搜索
const handleSearch = async () => {
  if (nowTableType.value != 'listSearch') {
    nowTableType.value = 'listSearch'
    paging.value.page = 1
  }
  const res = await UserAppListSearch(searchForm.value, paging.value.pageSize, paging.value.page);
  if (res.data.code == 200) {
    tableData.value = res.data.data.list;
    paging.value.total = res.data.data.total
  }
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    keywords: '',
    type: -1, // -1 代表不限，0 免费，1 收费
    status: -1, // -1 代表不限，0 正常，1 异常，2 维护
    view_status: -1 // -1 代表不限，0 通过，1 拒绝，2 审核中
  };
  nowTableType.value = 'list'
  handleSearch()
}

// 分页
const paging = ref<Pagination>({
  page: 1,
  pageSize: HelloAPIConfig.website.admin.pageSize,
  total: 0,
})

// 抽屉
const showDrawer = ref(false)
const drawerTitle = ref()
const resizable = ref(true) // 是否可调整大小
const drawerSize = ref('80%') // 抽屉大小
const nowRow = ref()

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
  if (nowTableType.value != 'list') {
    nowTableType.value = 'list'
    paging.value.page = 1
  }
  const res = await GetUserAppList(0, paging.value.page, paging.value.pageSize);
  if (res.data.code == 200) {
    tableData.value = res.data.data.list;
    paging.value.total = res.data.data.total
  }
}

// 分页改变时触发
function handleNewPageChange(page: number) {
  paging.value.page = page
  reloadTable()
}

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
  if (type === 'edit') {
    nowRow.value = row
  }
  drawerTitle.value = type === 'create' ? '发布接口' : '编辑接口'
  showDrawer.value = true
}

// 发布接口
const submitFormCreate = async () => {
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

// 更新接口
const submitFormUpdate = () => {
  ElMessageBox.confirm('确定要编辑并发布吗？编辑后需要重新审核', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    // 校验表单
    const isValid = await formRef.value?.validate().catch(() => false);
    if (isValid) {
      // 提交表单
      if (userStore.user.mode || 0 > 0) {
        formData.value.view_status = 0
      }
      const res = await UpdateApi(nowRow.value.id, formData.value)
      if (res.data.code == 200) {
        reloadTable()
        ElMessage.success('更新成功')
        showDrawer.value = false
      }
    }
  }).catch(() => {
  })
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
  ],
  params: [
    {
      validator: (_: any, value: any, callback: any) => {
        // 如果没有参数，直接通过校验
        if (!value || value.length === 0) {
          callback()
          return
        }
        // 遍历检查数组中每一项的内容
        for (let i = 0; i < value.length; i++) {
          const param = value[i]
          if (!param.name || !param.name.trim()) {
            return callback(new Error(`第 ${i + 1} 行请求参数的 "参数名" 不能为空`))
          }
          if (param.required === undefined || param.required === null) {
            return callback(new Error(`请选择第 ${i + 1} 行请求参数的 "是否必填"`))
          }
          if (!param.type || !param.type.trim()) {
            return callback(new Error(`第 ${i + 1} 行请求参数的 "类型" 不能为空`))
          }
          if (!param.msg || !param.msg.trim()) {
            return callback(new Error(`第 ${i + 1} 行请求参数的 "描述" 不能为空`))
          }
        }
        // 全部通过
        callback()
      }, trigger: ['blur', 'change']
    }
  ]
}

const onDrawerLoadOver = async () => {
  if (nowRow.value?.id) {
    const res = await GetApiInfo(nowRow.value.id)
    if (res.data.code == 200) {
      formData.value = res.data.data
      formData.value.view_status = 2
    }
  }
}

const onDrawerClose = () => {
  resetFormData()
  nowRow.value = undefined;
}

const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除吗？删除后将无法恢复！<br /><strong>所有相关的 APIKey、调用记录等也将被删除</strong>', '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
      }
  ).then(async () => {
    const res = await DeleteApi(id)
    if (res.data.code == 200) {
      reloadTable()
      ElMessage.success('删除成功')
    }
  }).catch(() => {
  })
}

// 根据当前表格类型判断是刷新搜索结果还是全部数据
const reloadTable = () => {
  if (nowTableType.value == 'listSearch') {
    handleSearch()
  } else {
    getTableData()
  }
}


onMounted(() => {
  getTableData();
})
</script>
<template>
  <div class="rounded-lg bg-white p-6 shadow-sm">
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-medium">API 管理</h2>
      <el-button :icon="Plus" type="primary" @click="openDrawer('create')">发布 API</el-button>
    </div>
    <hr class="border-[#E5E5E5] m-6"/>
    <div class="pl-2">
      <el-form :inline="true" :model="searchForm" class="searchForm">
        <el-form-item>
          <el-input
              v-model="searchForm.keywords"
              placeholder="Api ID / 关键字"
              @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="接口类型">
          <el-select v-model="searchForm.type" placeholder="请选择">
            <el-option :value="-1" label="不限"/>
            <el-option :value="0" label="免费">
              <el-tag size="small" type="success">免费</el-tag>
            </el-option>
            <el-option :value="1" label="收费">
              <el-tag size="small" type="warning">收费</el-tag>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="接口状态">
          <el-select v-model="searchForm.status" placeholder="请选择">
            <el-option :value="-1" label="不限"/>
            <el-option :value="0" label="正常">
              <el-tag size="small" type="success">正常</el-tag>
            </el-option>

            <el-option :value="1" label="异常">
              <el-tag size="small" type="danger">异常</el-tag>
            </el-option>

            <el-option :value="2" label="维护">
              <el-tag size="small" type="info">维护</el-tag>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="审核状态">
          <el-select v-model="searchForm.view_status" placeholder="请选择">
            <el-option :value="-1" label="不限"/>
            <el-option :value="0" label="通过">
              <el-tag size="small" type="success">通过</el-tag>
            </el-option>

            <el-option :value="1" label="拒绝">
              <el-tag size="small" type="danger">拒绝</el-tag>
            </el-option>

            <el-option :value="2" label="审核中">
              <el-tag size="small" type="warning">审核中</el-tag>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item>
          <el-button :icon="Search" plain type="primary" @click="handleSearch">查询</el-button>
          <el-button :icon="Refresh" @click="resetSearch">重置</el-button>
        </el-form-item>
        <el-form-item>
          <el-tag size="large" type="info">当前条件共 {{ paging.total }} 条数据</el-tag>
        </el-form-item>
      </el-form>
    </div>
    <el-table :data="tableData" class="w-full">
      <el-table-column label="Api ID">
        <template #default="{row}">
          {{ row.id }}
        </template>
      </el-table-column>
      <el-table-column label="接口名称" minWidth="250">
        <template #default="{row}">
          <el-link
              :href="'/info/' + row.id"
              target="_blank"
              type="primary"
              underline="never"
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
      <el-table-column label="创建日期" minWidth="150">
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
      <el-table-column label="审核状态">
        <template #default="{ row }">
          <el-tag :type="row.view_status === 2 ? 'warning' : row.view_status === 1 ? 'danger' : 'success'">
            {{ row.view_status === 2 ? '审核中' : row.view_status === 1 ? '拒绝' : '通过' }}
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
              @click="openDrawer('edit', row)"
          ></el-button>
          <el-button
              :icon="Delete"
              circle
              plain
              type="danger"
              @click="handleDelete(row.id)"
          ></el-button>
        </template>
      </el-table-column>
      <template #empty>
        <el-empty description="空空的什么也没有＞﹏＜" style="user-select: none"/>
      </template>
    </el-table>
    <div class="flex items-center justify-center mt-6">
      <el-pagination
          :current-page="paging.page"
          :page-size="paging.pageSize"
          :total="paging.total"
          layout="prev, pager, next"
          @current-change="handleNewPageChange"
      />
    </div>

    <el-drawer
        v-model="showDrawer"
        :destroy-on-close="true"
        :resizable="resizable"
        :size="drawerSize"
        :title="drawerTitle"
        class="rounded-tl-[20px] rounded-tr-[20px] m-auto custom-btt-drawer"
        direction="btt"
        @closed="onDrawerClose"
        @open="onDrawerLoadOver"
    >
      <div class="m-auto max-w-[90%]">
        <el-form ref="formRef" :model="formData" :rules="rules">
          <el-form-item label="接口类型" prop="type">
            <el-radio-group v-model="formData.type">
              <el-radio-button :value="0" label="免费"/>
              <el-radio-button :value="1" label="收费"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="接口状态" prop="status">
            <el-radio-group v-model="formData.status">
              <el-radio-button :value="0" label="正常"/>
              <el-radio-button :value="1" label="异常"/>
              <el-radio-button :value="2" label="维护"/>
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
              <el-radio-button :value="0" label="Get"/>
              <el-radio-button :value="1" label="Post"/>
              <el-radio-button :value="2" label="Put"/>
              <el-radio-button :value="3" label="Delete"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="返回类型" prop="returnType">
            <el-input v-model="formData.returnType" placeholder="请输入返回类型，例如：Json / Text / Image"/>
          </el-form-item>

          <el-form-item label="请求参数" prop="params">
            <div class="rounded-sm overflow-hidden border border-[#DCDFE6] w-full">
              <el-table :data="formData.params" class="table-4px-border" style="width: 100%">
                <el-table-column label="参数名" width="180">
                  <template #default="{row}">
                    <el-input v-model="row.name" placeholder="参数名"/>
                  </template>
                </el-table-column>
                <el-table-column label="必填" width="85">
                  <template #default="{row}">
                    <el-select v-model="row.required">
                      <el-option :value="0" label="否"/>
                      <el-option :value="1" label="是"/>
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
                    <el-input v-model="row.msg" autosize placeholder="参数描述" type="textarea"/>
                  </template>
                </el-table-column>
                <el-table-column align="center" label="操作" width="70">
                  <template #default="scope">
                    <el-button
                        :icon="Close"
                        circle
                        size="small"
                        type="danger"
                        @click="removeParam(scope.$index)"
                    />
                  </template>
                </el-table-column>
                <template #empty>
                  没有参数
                </template>
              </el-table>
            </div>
            <el-button plain style="margin-top: 4px;" type="primary" @click="addParam">
              添加参数
            </el-button>
          </el-form-item>
          <el-form-item label="返回示例" prop="returnContent">
            <MDEdit v-model="formData.returnContent"/>
          </el-form-item>
        </el-form>
        <div class="flex justify-center">
          <el-button class="w-50" size="large" type="primary" @click="nowRow ? submitFormUpdate() : submitFormCreate()">
            {{ nowRow ? '编辑' : '发布' }} API
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
  background-color: transparent;
}

:deep(.searchForm .el-input) {
  --el-input-width: 150px;
}

:deep(.searchForm .el-select) {
  --el-select-width: 100px;
}

@media (max-width: 768px) {
  :deep(.custom-btt-drawer) {
    width: 100%;
  }
}
</style>