<script lang="ts" setup>
import {Plus, Delete, Edit, Search, Refresh} from "@element-plus/icons-vue";
import {GetUserApiKeyList, GetApiKeyInfo, UpdateApiKey, UserApiKeyListSearch, DeleteApiKey, CreateApiKey} from "@/api";
import {onMounted, reactive, ref, watch} from "vue";
import type {Pagination, SelectFormApiKey, APIKey} from "@/types";
import {dayjs, type FormRules} from "element-plus";
import {HelloAPIConfig} from "@/config/config.ts";
import {useUserStore} from "@/stores";
import {copyText, formatDuration, formatNativeDate, isMobile} from "@/utils";

// 分页
const paging = ref<Pagination>({
  page: 1,
  pageSize: HelloAPIConfig.website.admin.pageSize,
  total: 0,
})

// 统一数据获取
const fetchData = async () => {
  const isSearching = searchForm.value.keywords !== '' || searchForm.value.type !== -1 || searchForm.value.status !== -1;
  const apiCall = isSearching
      ? UserApiKeyListSearch(searchForm.value, userStore.user.id, paging.value.page, paging.value.pageSize)
      : GetUserApiKeyList(userStore.user.id, paging.value.page, paging.value.pageSize);

  const res = await apiCall;
  if (res.data.code === 200) {
    const {list, total} = res.data.data;
    paging.value.total = total;
    tableData.value = processTableData(list);
  }
}

// 处理表格数据
const processTableData = (rawData: any[]): APIKey[] => {
  const now = Date.now();
  return rawData.map((item: any) => ({
    ...item,
    isExpired: (Date.parse(item.expired) < now && item.type === 0) || (item.count === 0 && item.type === 1),
    remainingTime: formatDuration(now, item.expired),
    createdText: formatNativeDate(item.created)
  }));
}


// 分页改变时触发
const handlePageChange = () => {
  fetchData()
}

const userStore = useUserStore();
const tableData = ref<APIKey[]>();

// 搜索筛选表单
const searchForm = ref<SelectFormApiKey>({
  keywords: '',
  type: -1, // -1 代表不限，0 免费，1 收费
  status: -1, // -1 代表不限，0 正常，1 异常，2 维护
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
    type: -1, // -1 代表不限，0 免费，1 收费
    status: -1, // -1 代表不限，0 正常，1 异常，2 维护
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

// 日期表单
const dateArray = ref(['', ''])
watch(dateArray, (newVal, _) => {
  if (newVal && newVal[0] && newVal[1]) {
    if (showDrawer.value) {
      formData.value.started = dayjs(newVal[0]).format('YYYY-MM-DDTHH:mm:ss')
      formData.value.expired = dayjs(newVal[1]).format('YYYY-MM-DDTHH:mm:ss')
    }
  }
})

// 提交表单
const formRef = ref()
const formData = ref<APIKey>({
  api_id: null,
  key: '',
  type: 0,
  created: '',
  updated: '',
  started: '',
  expired: '',
  count: 0,
  desc: ''
});

// 重置formData
const resetFormData = () => {
  formData.value = {
    api_id: null,
    key: '',
    type: 0,
    created: '',
    updated: '',
    started: '',
    expired: '',
    count: 0,
    desc: ''
  }
  dateArray.value = ['', '']
}

// 打开抽屉
const openDrawer = (type: string, row?: object) => {
  resetFormData()
  if (type === 'edit') {
    nowRow.value = row
  }
  drawerTitle.value = type === 'create' ? '创建 APIKey' : '编辑 APIKey'
  showDrawer.value = true
}

// 创建APIKey
const submitFormCreate = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    // 提交表单
    if (formData.value.type == 0) {
      formData.value.count = 0
    } else {
      formData.value.started = ''
      formData.value.expired = ''
    }
    const res = await CreateApiKey(formData.value)
    if (res.data.code == 200) {
      await fetchData()
      ElMessage.success('创建成功')
      showDrawer.value = false
    }
  }
}

// 更新APIKey
const submitFormUpdate = async () => {
  // 校验表单
  const isValid = await formRef.value?.validate().catch(() => false);
  if (isValid) {
    // 提交表单
    if (formData.value.type == 0) {
      formData.value.count = 0
    } else {
      formData.value.started = ''
      formData.value.expired = ''
    }
    const res = await UpdateApiKey(nowRow.value.key, formData.value)
    if (res.data.code == 200) {
      ElMessage.success('编辑成功')
      showDrawer.value = false
    }
  }
}

// 表单校验
const rules: FormRules = reactive({
  type: [
    {required: true, message: '请选择接口类型', trigger: 'change'}
  ],
  api_id: [
    {required: true, message: '请输入接口 Api ID', trigger: 'blur'}
  ],
  // 备注长度校验
  desc: [
    {max: 255, message: '备注长度不得超过 255 个字符', trigger: 'blur'}
  ],
  // 自定义校验逻辑
  validateDynamicField: [
    {
      validator: (_, __, callback) => {
        if (formData.value.type == 0) {
          if (!dateArray.value || dateArray.value[0] === '' || dateArray.value[1] === '' || dateArray.value[0] == null || dateArray.value[1] == null) {
            return callback(new Error('请选择有效期限'));
          }
        }
        if (formData.value.type == 1) {
          if (formData.value.count === undefined || formData.value.count === null || formData.value.count < 0) {
            return callback(new Error('请输入可用次数'));
          }
        }
        callback();
      },
      trigger: ['blur', 'change']
    }
  ]
})

const onDrawerLoadOver = async () => {
  if (nowRow.value?.api_id) {
    const res = await GetApiKeyInfo(nowRow.value.key)
    if (res.data.code == 200) {
      formData.value = res.data.data
      dateArray.value = [res.data.data.started, res.data.data.expired]
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

const handleDelete = (key: string) => {
  ElMessageBox.confirm('确定要删除吗？删除后将无法恢复！', '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
        dangerouslyUseHTMLString: true,
      }
  ).then(async () => {
    const res = await DeleteApiKey(key)
    if (res.data.code == 200) {
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
          {{ row.api_id }}
        </template>
      </el-table-column>
      <el-table-column label="APIKey" minWidth="160">
        <template #default="{row}">
          <el-link
              target="_blank"
              type="info"
              @click="copyText(row.key)"
          >
            {{ row.key }}
          </el-link>
        </template>
      </el-table-column>
      <el-table-column label="类型">
        <template #default="{ row }">
          <el-tag :type="row.type === 1 ? 'warning' : 'primary'">
            {{ row.type === 1 ? '计次' : '期限' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建日期" minWidth="150">
        <template #default="{ row }">
          {{ row.createdText }}
        </template>
      </el-table-column>
      <el-table-column label="有效期 / 调用次数" minWidth="150">
        <template #default="{ row }">
          <el-tag type="primary">
            <span v-if="row.started">{{ row.remainingTime }}</span>
            <span v-else>-</span>
          </el-tag>&nbsp;
          <el-tag type="warning">
            <span v-if="row.type==1">{{ row.count }}</span>
            <span v-else>-</span>
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态">
        <template #default="{ row }">
          <el-tag :type="row.isExpired ? 'danger' : 'success'">
            {{ row.isExpired ? '失效' : '有效' }}
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
              @click="handleDelete(row.key)"
          ></el-button>
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
    >
      <div>
        <el-form ref="formRef" :model="formData" :rules="rules">
          <el-form-item label="接口类型" prop="type">
            <el-radio-group v-model="formData.type">
              <el-radio-button :value="0" label="期限"/>
              <el-radio-button :value="1" label="计次"/>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="所属接口" prop="api_id">
            <el-input v-model="formData.api_id" placeholder="请输入接口Api ID，例如：1"/>
          </el-form-item>
          <el-form-item v-if="formData.type==0" label="有效期限" prop="validateDynamicField">
            <el-date-picker
                v-model="dateArray"
                :shortcuts="shortcuts"
                end-placeholder="过期日期"
                range-separator="至"
                size="default"
                start-placeholder="开始日期"
                type="daterange"
                unlink-panels
                value-format="YYYY-MM-DD HH:mm:ss"
            />
          </el-form-item>
          <el-form-item v-if="formData.type==1" label="可用次数" prop="validateDynamicField">
            <el-input-number v-model="formData.count" :max="9999999999" :min="0" :step="20"/>
          </el-form-item>
          <el-form-item label="备&emsp;&emsp;注" prop="desc">
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