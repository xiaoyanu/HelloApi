<script lang="ts" setup>
import {Delete, Search, Refresh, View, Key, Operation} from "@element-plus/icons-vue";
import {DeleteUser, GetUserList, SetUserMode, SetUserPassword, UserListSearch} from "@/api";
import {onMounted, ref} from "vue";
import type {Pagination, SelectFormUser, User} from "@/types";
import {HelloAPIConfig} from "@/config/config.ts";
import {formatNativeDate} from "@/utils/module/more.ts";
import {useUserStore} from "@/stores";

const userStore = useUserStore();
const tableData = ref<User[]>();
const dialogVisible = ref({
  user: false,
  password: false,
  mode: false
})
const tableRow = ref<User>()

// 搜索筛选表单
const searchForm = ref<SelectFormUser>({
  keywords: '',
  mode: -1, // -1 代表不限，0 普通，1 管理
})

// 重置dialog状态
const resetDialog = () => {
  dialogVisible.value = {
    user: false,
    password: false,
    mode: false
  }
}

// 统一数据获取
const fetchData = async () => {
  const isSearching = searchForm.value.keywords !== '' || searchForm.value.mode !== -1;
  const apiCall = isSearching
      ? UserListSearch(searchForm.value.keywords, searchForm.value.mode, paging.value.page, paging.value.pageSize)
      : GetUserList(paging.value.page, paging.value.pageSize);

  try {
    const res = await apiCall;
    if (res.data.code === 200) {
      const {list, total} = res.data.data;
      paging.value.total = total;
      tableData.value = processTableData(list);
    }
  } catch (error) {
    console.error("Failed to fetch data:", error);
  }
}

// 处理表格数据
const processTableData = (rawData: any[]): User[] => {
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
    mode: -1
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

const showUserDetails = (row: User) => {
  tableRow.value = row;
  dialogVisible.value.user = true;
}


const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除吗？删除后将无法恢复！<br /><strong>所有相关的API、APIKey、调用记录等也将被删除</strong>', '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
      }
  ).then(async () => {
    const res = await DeleteUser(id)
    if (res.data.code == 200) {
      await fetchData()
      ElMessage.success('删除成功')
    }
  })
}

const setUserPassword = (userid: number) => {
  ElMessageBox.prompt('请输入要设置的密码', '提示', {
    confirmButtonText: '修改',
    cancelButtonText: '取消',
    inputPattern: /^\S{6,15}$/,
    inputErrorMessage: '密码为6-15位非空字符',
  }).then(async (e) => {
    const res = await SetUserPassword(userid, e.value)
    if (res.data.code == 200) {
      ElMessage.success("设置密码成功")
    }
  })
}

const setUserMode = (userid: number) => {
  ElMessageBox.confirm(
      '请选择该用户的权限级别：<br/><span style="color: #f56c6c">管理员拥有后台管理权限，请谨慎操作。</span>',
      '修改权限',
      {
        confirmButtonText: '设为管理员',
        cancelButtonText: '设为普通用户',
        distinguishCancelAndClose: true,
        type: 'warning',
        dangerouslyUseHTMLString: true,
      }
  ).then(async () => {
    const res = await SetUserMode(userid, 1)
    if (res.data.code == 200) {
      ElMessage.success("设置权限成功")
      if (userid == userStore.user.id) {
        await userStore.refreshUser()
      }
      await fetchData()
    }
  }).catch(async (action) => {
    if (action === 'cancel') {
      const res = await SetUserMode(userid, 0)
      if (res.data.code == 200) {
        ElMessage.success("设置权限成功")
        if (userid == userStore.user.id) {
          await userStore.refreshUser()
        }
        await fetchData()
      }
    }
  })
}

onMounted(() => {
  fetchData();
})
</script>
<template>
  <admin-main-body title="用户管理">
    <div class="pl-2">
      <el-form :inline="true" :model="searchForm" class="searchForm">
        <el-form-item>
          <el-input
              v-model="searchForm.keywords"
              placeholder="ID / 关键词"
              @keyup.enter="handleSearch"
          />
        </el-form-item>

        <el-form-item label="权限">
          <el-select v-model="searchForm.mode" placeholder="请选择">
            <el-option :value="-1" label="不限"/>
            <el-option :value="0" label="普通用户">
              <el-tag size="small" type="primary">普通用户</el-tag>
            </el-option>
            <el-option :value="1" label="管理员">
              <el-tag size="small" type="danger">管理员</el-tag>
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
      <el-table-column label="ID">
        <template #default="{row}">
          {{ row.id }}
        </template>
      </el-table-column>
      <el-table-column label="账号">
        <template #default="{row}">
          {{ row.username }}
        </template>
      </el-table-column>
      <el-table-column label="昵称">
        <template #default="{row}">
          {{ row.nick }}
        </template>
      </el-table-column>
      <el-table-column label="注册时间" minWidth="150">
        <template #default="{ row }">
          {{ row.createdText }}
        </template>
      </el-table-column>
      <el-table-column label="权限">
        <template #default="{ row }">
          <el-tag :type="row.mode === 0 ? 'primary' : 'danger'">
            {{ row.mode === 0 ? '普通用户' : '管理员' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="240">
        <template #default="{ row }">
          <el-tooltip content="详细信息" placement="left">
            <el-button
                :icon="View"
                circle
                plain
                @click="showUserDetails(row)"
            />
          </el-tooltip>
          <el-tooltip content="设置密码" placement="left">
            <el-button
                :icon="Key"
                circle
                plain
                @click="setUserPassword(row.id)"
            />
          </el-tooltip>
          <el-tooltip content="修改权限" placement="left">
            <el-button
                :icon="Operation"
                circle
                plain
                @click="setUserMode(row.id)"
            />
          </el-tooltip>
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


    <!-- 用户信息 -->
    <el-dialog
        v-model="dialogVisible.user"
        title="用户信息"
        align-center
        width="400px"
        @closed="resetDialog"
    >
      <div v-if="tableRow">
        <el-descriptions border :column="1">
          <el-descriptions-item label-align="right" label="ID">{{ tableRow.id }}</el-descriptions-item>
          <el-descriptions-item label-align="right" label="账号">{{ tableRow.username }}</el-descriptions-item>
          <el-descriptions-item label-align="right" label="昵称">{{ tableRow.nick }}</el-descriptions-item>
          <el-descriptions-item label-align="right" label="权限">
            <el-tag :type="tableRow.mode === 0 ? 'primary' : 'danger'" size="small">
              {{ tableRow.mode === 0 ? "普通用户" : "管理员" }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label-align="right" label="邮箱">{{ tableRow.mail }}</el-descriptions-item>
          <el-descriptions-item label-align="right" label="注册时间">{{ tableRow.createdText }}</el-descriptions-item>
          <el-descriptions-item label-align="right" label="修改时间">{{
              formatNativeDate(tableRow.updated as string)
            }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-dialog>
  </admin-main-body>
</template>
<style scoped>
:deep(.searchForm .el-input) {
  --el-input-width: 150px;
}

:deep(.searchForm .el-select) {
  --el-select-width: 100px;
}
</style>