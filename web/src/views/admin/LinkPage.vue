<script lang="ts" setup>
import {Plus, Delete, Edit, Search, Refresh} from "@element-plus/icons-vue";
import {
  GetLinkList,
  UpdateLink,
  GetLinkListSearch,
  DeleteLink,
  CreateLink,
} from "@/api";
import {onMounted, reactive, ref} from "vue";
import type {Pagination} from "@/types";
import {type FormRules} from "element-plus";
import {HelloAPIConfig} from "@/config/config.ts";
import {formatNativeDate, isMobile} from "@/utils";

// --- Loading 状态 ---
const loading = ref(false); // 表格加载状态
const drawerLoading = ref(false); // 抽屉详情加载状态
const submitLoading = ref(false); // 表单提交加载状态

// 分页
const paging = ref<Pagination>({
  page: 1,
  pageSize: HelloAPIConfig.website.admin.pageSize,
  total: 0,
})

// 统一数据获取
const fetchData = async () => {
  loading.value = true; // 开启表格 Loading
  const isSearching = searchForm.value.keywords !== '';
  const apiCall = isSearching
      ? GetLinkListSearch(searchForm.value.keywords, paging.value.page, paging.value.pageSize,)
      : GetLinkList(paging.value.page, paging.value.pageSize, 1);

  try {
    const res = await apiCall;
    if (res.data.code === 200) {
      const {list, total} = res.data.data;
      paging.value.total = total;
      tableData.value = processTableData(list);
    }
  } catch (error) {
    console.error("Failed to fetch data:", error);
  } finally {
    loading.value = false; // 关闭表格 Loading
  }
}

// 处理表格数据
const processTableData = (rawData: any[]) => {
  return rawData.map((item: any) => ({
    ...item,
    createdText: formatNativeDate(item.created)
  }));
}


// 分页改变时触发
const handlePageChange = () => {
  fetchData()
}

const tableData = ref();

// 搜索筛选表单
const searchForm = ref({
  keywords: '',
})

// 搜索
const handleSearch = () => {
  paging.value.page = 1;
  fetchData();
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    keywords: '',
  };
  paging.value.page = 1
  fetchData()
}


// 抽屉
const showDrawer = ref(false)
const drawerTitle = ref()
const resizable = ref(true) // 是否可调整大小
const drawerSize = ref(isMobile ? '100%' : '30%') // 抽屉大小
const nowRow = ref()

// 提交表单
const formRef = ref()
const formData = ref({
  link_id: '',
  name: '',
  url: '',
  avatar: '',
  desc: ''
});

// 重置formData
const resetFormData = () => {
  formData.value = {
    link_id: '',
    name: '',
    url: '',
    avatar: '',
    desc: ''
  }
}

// 打开抽屉
const openDrawer = (type: string, row?: object) => {
  resetFormData()
  if (type === 'edit') {
    nowRow.value = row
  }
  drawerTitle.value = type === 'create' ? '新增友人' : '编辑友人'
  showDrawer.value = true
}

// 创建Link
const submitFormCreate = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    submitLoading.value = true; // 开启提交 Loading
    try {
      const res = await CreateLink(formData.value)
      if (res.data.code == 200) {
        await fetchData()
        ElMessage.success('新增成功')
        showDrawer.value = false
      }
    } finally {
      submitLoading.value = false; // 关闭提交 Loading
    }
  }
}

// 更新Link
const submitFormUpdate = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    submitLoading.value = true; // 开启提交 Loading
    try {
      const res = await UpdateLink(nowRow.value.link_id, formData.value)
      if (res.data.code == 200) {
        await fetchData()
        ElMessage.success('编辑成功')
        showDrawer.value = false
      }
    } finally {
      submitLoading.value = false; // 关闭提交 Loading
    }
  }
}

// 表单校验
const rules: FormRules = reactive({
  url: [
    {max: 255, message: 'URL长度不得超过 255 个字符', trigger: 'blur'},
    {type: 'url', message: '请输入正确的网址格式', trigger: 'blur'}
  ],
  name: [
    {max: 255, message: '名称长度不得超过 255 个字符', trigger: 'blur'}
  ],
  desc: [
    {max: 255, message: '描述长度不得超过 255 个字符', trigger: 'blur'}
  ],
  avatar: [
    {max: 255, message: '图标URL长度不得超过 255 个字符', trigger: 'blur'},
    {type: 'url', message: '请输入正确的图标网址格式', trigger: 'blur'}
  ]
})

const onDrawerLoadOver = async () => {
  if (nowRow.value?.name) {
    drawerLoading.value = true; // 开启详情 Loading
    try {
      formData.value = nowRow.value;
    } finally {
      drawerLoading.value = false; // 关闭详情 Loading
    }
  }
}

const onDrawerClose = () => {
  resetFormData()
  nowRow.value = undefined;
}

const handleDelete = (linkID: number) => {
  ElMessageBox.confirm('确定要删除吗？删除后将无法恢复！', '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
      }
  ).then(async () => {
    const res = await DeleteLink(linkID)
    if (res.data.code == 200) {
      // 如果当前页只有一条数据，并且不是第一页，则删除后请求前一页
      if (tableData.value && tableData.value.length === 1 && paging.value.page > 1) {
        paging.value.page--;
      }
      await fetchData()
      ElMessage.success('删除成功')
    }
  }).catch(() => {
  })
}


onMounted(() => {
  fetchData();
})
</script>
<template>
  <admin-main-body title="友人帐">
    <template #header>
      <el-button :icon="Plus" type="primary" @click="openDrawer('create')">新增友人</el-button>

    </template>
    <template #default>
      <div class="pl-2">
        <el-form :inline="true" :model="searchForm" class="searchForm">
          <el-form-item>
            <el-input
                v-model="searchForm.keywords"
                placeholder="ID / 关键字"
                @keyup.enter="handleSearch"
            />
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

      <el-table :data="tableData" class="w-full" v-loading="loading">
        <el-table-column label="ID">
          <template #default="{row}">
            {{ row.link_id }}
          </template>
        </el-table-column>
        <el-table-column label="名称" minWidth="150">
          <template #default="{row}">
            {{ row.name }}
          </template>
        </el-table-column>
        <el-table-column label="描述" minWidth="160">
          <template #default="{row}">
            {{ row.desc }}
          </template>
        </el-table-column>
        <el-table-column label="链接" minWidth="120">
          <template #default="{row}">
            <el-link
                target="_blank"
                type="info"
            >
              {{ row.url }}
            </el-link>
          </template>
        </el-table-column>
        <el-table-column label="创建日期" minWidth="150">
          <template #default="{ row }">
            {{ row.createdText }}
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
            />
            <el-button
                :icon="Delete"
                circle
                plain
                type="danger"
                @click="handleDelete(row.link_id)"
            />
          </template>
        </el-table-column>
        <template #empty>
          <el-empty description="空空的什么也没有＞﹏＜" style="user-select: none"/>
        </template>
      </el-table>
      <div class="flex items-center justify-center mt-6">
        <el-pagination
            v-model:current-page="paging.page"
            :page-size="paging.pageSize"
            :total="paging.total"
            layout="prev, pager, next"
            @current-change="handlePageChange"
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
          :close-on-press-escape="false"
      >
        <div v-loading="drawerLoading">
          <el-form ref="formRef" :model="formData" :rules="rules">
            <el-form-item label="图标" prop="avatar">
              <el-input v-model="formData.avatar" placeholder="网站图标，填图片链接"/>
            </el-form-item>
            <el-form-item label="名称" prop="name">
              <el-input v-model="formData.name" placeholder="网站名称"/>
            </el-form-item>
            <el-form-item label="描述" prop="desc">
              <el-input v-model="formData.desc" placeholder="网站描述"/>
            </el-form-item>
            <el-form-item label="链接" prop="url">
              <el-input v-model="formData.url" placeholder="网站URL"/>
            </el-form-item>
          </el-form>
          <div class="flex justify-center">
            <el-button class="w-50" size="large" type="primary"
                       :loading="submitLoading"
                       @click="nowRow ? submitFormUpdate() : submitFormCreate()">
              {{ nowRow ? '编辑' : '新增' }}友人
            </el-button>
          </div>
        </div>

      </el-drawer>
    </template>
  </admin-main-body>
</template>
<style scoped>
/* 隐藏表单标签的伪元素 */
:deep(.el-form-item__label:before) {
  display: none;
}

:deep(.custom-btt-drawer) {
  width: 80%;
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