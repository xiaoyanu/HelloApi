<script lang="ts" setup>
import {Plus, Delete, Edit, Search, Refresh} from "@element-plus/icons-vue";
import {GetUserAppList, GetApiInfo, CreateApi, UpdateApi, UserAppListSearch, DeleteApi} from "@/api";
import {onMounted, ref} from "vue";
import type {AppList, Pagination, selectFormApiKey, APIKey} from "@/types";
import type {FormRules} from "element-plus";
import {HelloAPIConfig} from "@/config/config.ts";

const tableData = ref<AppList[]>();
const nowTableType = ref()

// 搜索筛选表单
const searchForm = ref<selectFormApiKey>({
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
const drawerSize = ref('30%') // 抽屉大小
const nowRow = ref()

// 提交表单
const formRef = ref()
const formData = ref<APIKey>({
  apiId: 0,
  key: '',
  created: 0,
  type: 0,
  started: [
    new Date(),
    new Date()
  ],
  expired: 0,
  count: 0,
  desc: ''
});

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
    apiId: 0,
    key: '',
    created: 0,
    type: 0,
    started: [
      new Date(),
      new Date()
    ],
    expired: 0,
    count: 0,
    desc: ''
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
    {required: true, message: '类型', trigger: 'change'}
  ]
}

const onDrawerLoadOver = async () => {
  if (nowRow.value?.id) {
    const res = await GetApiInfo(nowRow.value.id)
    if (res.data.code == 200) {
      formData.value = res.data.data
    }
  }
}

// 时间范围快捷选择
const shortcuts = [
  {
    text: '设置7天',
    value: () => {
      const end = new Date()
      const start = new Date()
      end.setTime(start.getTime() + 3600 * 1000 * 24 * 7)
      return [start, end]
    },
  },
  {
    text: '设置30天',
    value: () => {
      const end = new Date()
      const start = new Date()
      end.setTime(start.getTime() + 3600 * 1000 * 24 * 30)
      return [start, end]
    },
  },
  {
    text: '设置180天',
    value: () => {
      const end = new Date()
      const start = new Date()
      end.setTime(start.getTime() + 3600 * 1000 * 24 * 180)
      return [start, end]
    },
  },
  {
    text: '设置1年',
    value: () => {
      const end = new Date()
      const start = new Date()
      end.setTime(start.getTime() + 3600 * 1000 * 24 * 365)
      return [start, end]
    },
  },
]

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
      <h2 class="text-lg font-medium">APIKey 管理</h2>
      <el-button :icon="Plus" type="primary" @click="openDrawer('create')">创建 APIKey</el-button>
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

        <el-form-item label="类型">
          <el-select v-model="searchForm.type" placeholder="请选择">
            <el-option :value="-1" label="不限"/>
            <el-option :value="0" label="期限">
              <el-tag size="small" type="primary">期限</el-tag>
            </el-option>
            <el-option :value="1" label="计次">
              <el-tag size="small" type="warning">计次</el-tag>
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择">
            <el-option :value="-1" label="不限"/>
            <el-option :value="0" label="有效">
              <el-tag size="small" type="success">有效</el-tag>
            </el-option>

            <el-option :value="1" label="失效">
              <el-tag size="small" type="danger">失效</el-tag>
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
      <el-table-column label="APIKey" minWidth="250">
        <template #default="{row}">
          {{ row.title }}
        </template>
      </el-table-column>
      <el-table-column label="类型">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'warning' : 'primary'">
            {{ row.type === 1 ? '计次' : '期限' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="有效期 / 调用次数" minWidth="150">
        <template #default="{ row }">
          <el-tag type="primary">
            1年
          </el-tag>&nbsp;
          <el-tag type="warning">
            2026
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template #default="{ row }">
          <el-tag :type="row.status === 1 ? 'danger' : 'success'">
            {{ row.status === 1 ? '失效' : '有效' }}
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
        class="rounded-tl-[20px] rounded-bl-[20px] m-auto custom-btt-drawer"
        direction="rtl"
        @closed="onDrawerClose"
        @open="onDrawerLoadOver"
    >
      <div>
        <el-form ref="formRef" :model="formData" :rules="rules">
          <el-form-item label="接口类型" prop="type">
            <el-radio-group v-model="formData.type">
              <el-radio-button :value="0" label="期限"/>
              <el-radio-button :value="1" label="计次"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="所属接口" prop="url">
            <el-input v-model="formData.apiId" placeholder="请输入接口Api ID，例如：1"/>
          </el-form-item>
          <el-form-item v-if="formData.type==0" label="有效期限" prop="started">
            <el-date-picker
                v-model="formData.started"
                :shortcuts="shortcuts"
                end-placeholder="过期日期"
                range-separator="至"
                size="default"
                start-placeholder="开始日期"
                type="daterange"
                unlink-panels
            />
          </el-form-item>
          <el-form-item v-if="formData.type==1" label="可用次数" prop="count">
            <el-input-number v-model="formData.count" :max="9999999999" :min="0" :step="20"/>
          </el-form-item>
          <el-form-item label="备&emsp;&emsp;注" prop="smallTitle">
            <el-input v-model="formData.desc" :maxlength="255" autosize placeholder="可填写备注，例如：测试用"
                      type="textarea"/>
          </el-form-item>
        </el-form>
        <div class="flex justify-center">
          <el-button class="w-50" size="large" type="primary" @click="nowRow ? submitFormUpdate() : submitFormCreate()">
            {{ nowRow ? '编辑' : '创建' }} APIKey
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