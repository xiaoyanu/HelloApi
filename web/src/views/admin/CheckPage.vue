<script lang="ts" setup>
import {Delete, Search, Refresh, Close, Check} from "@element-plus/icons-vue";
import {GetCheckAppList, CheckAppListSearch, DeleteApi, CheckAppChange} from "@/api";
import {onMounted, ref} from "vue";
import type {AppList, Pagination, SelectFormApi} from "@/types";
import {HelloAPIConfig} from "@/config/config.ts";
import {formatNativeDate} from "@/utils/module/more.ts";

const tableData = ref<AppList[]>();

// --- Loading 状态 ---
const loading = ref(false); // 表格加载状态

// 搜索筛选表单
const searchForm = ref<SelectFormApi>({
  keywords: '',
  type: -1, // -1 代表不限，0 免费，1 收费
  status: -1, // -1 代表不限，0 正常，1 异常，2 维护
  view_status: -1 // -1 代表不限，0 通过，1 拒绝，2 审核中
})

// 统一数据获取
const fetchData = async () => {
  loading.value = true; // 开启表格 Loading
  const isSearching = searchForm.value.keywords !== '' || searchForm.value.type !== -1 || searchForm.value.status !== -1 || searchForm.value.view_status !== -1;
  const apiCall = isSearching
      ? CheckAppListSearch(searchForm.value, paging.value.pageSize, paging.value.page)
      : GetCheckAppList(paging.value.page, paging.value.pageSize);

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
const processTableData = (rawData: any[]): AppList[] => {
  return rawData.map((item: any) => ({
    ...item,
    createdText: formatNativeDate(item.created),
  }));
}

// 搜索
const handleSearch = () => {
  paging.value.page = 1;
  fetchData();
}

// 重置搜索
const resetSearch = () => {
  searchForm.value = {
    keywords: '',
    type: -1,
    status: -1,
    view_status: -1
  };
  paging.value.page = 1;
  fetchData();
}

// 分页
const paging = ref<Pagination>({
  page: 1,
  pageSize: HelloAPIConfig.website.admin.pageSize,
  total: 0,
})

// 分页改变时触发
const handlePageChange = (page: number) => {
  paging.value.page = page;
  fetchData();
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

const handleCheckAppChange = (api_id: number, view_status: number) => {
  let messages
  if (view_status == 0) {
    messages = '确定要通过吗？<br /><strong>通过后所有人可以查看和调用</strong>'
  } else {
    messages = '确定要拒绝吗？<br /><strong>拒绝需要重新编辑并提交审核</strong>'
  }
  ElMessageBox.confirm(messages, '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
      }
  ).then(async () => {
    const res = await CheckAppChange(api_id, view_status)
    if (res.data.code == 200) {
      if (tableData.value && tableData.value.length === 1 && paging.value.page > 1) {
        paging.value.page--;
      }
      await fetchData()
      ElMessage.success('操作成功')
    }
  })
}

onMounted(() => {
  fetchData();
})
</script>
<template>
  <admin-main-body title="审核 API">
    <template #default>
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

              <el-option :value="3" label="隐藏">
                <el-tag effect="dark" size="small" type="info">隐藏</el-tag>
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

      <el-table :data="tableData" class="w-full" v-loading="loading">
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
            {{ row.createdText }}
          </template>
        </el-table-column>
        <el-table-column label="状态">
          <template #default="{ row }">
            <el-tag
                :effect="row.status === 3 ? 'dark' : 'light'"
                :type=" row.status === 3 ? 'info' :row.status === 2 ? 'info' : row.status === 1 ? 'danger' : 'success'">
              {{ row.status === 3 ? '隐藏' : row.status === 2 ? '维护' : row.status === 1 ? '异常' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button
                :icon="Check"
                circle
                plain
                type="success"
                @click="handleCheckAppChange(row.id,0)"
            />
            <el-button
                :icon="Close"
                circle
                plain
                type="warning"
                @click="handleCheckAppChange(row.id,1)"
            />
            <el-button
                :icon="Delete"
                circle
                plain
                type="danger"
                @click="handleDelete(row.id)"
            />
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
            @current-change="handlePageChange"
        />
      </div>
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