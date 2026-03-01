<!-- ApiCardList 组件 : 用于首页、搜索页的接口列表展示 -->
<script lang="ts" setup>
import type {homeApiList} from "@/types";

const emit = defineEmits(['pageChange']);
const checkID = (id: number) => {
  window.open("/info/" + id)
}
defineProps(
    {
      paging: {
        type: Object,
        required: true,
      },
      dataList: {
        type: Array<homeApiList>,
        required: true
      },
      emptyText: {
        type: String,
        required: true,
      }
    }
)

// 分页改变时触发
function handleNewPageChange(page: number) {
  emit('pageChange', page);
}
</script>
<template>
  <div v-if="paging.total>0" class="grid grid-cols-[repeat(auto-fill,325px)] justify-center gap-8 mt-10 text-[#61677C]">
    <div v-for="item in dataList"
         :key="item.id"
         class="box flex flex-col justify-between rounded-lg bg-[#ecf0f3] w-81.25 h-40 p-5 font-semibold">
      <div>
        <div class="flex justify-between items-center text-[20px]">
          <div class="w-50 overflow-hidden whitespace-nowrap  text-ellipsis">{{ item.title }}</div>
          <div class="flex">
            <div v-if="item.type==1" class="tag tag-buy">收费</div>
            <div
                :class="{'tag tag-success': item.status == 0, 'tag tag-danger': item.status == 1, 'tag tag-error': item.status == 2}">
              {{ item.status == 0 ? '正常' : item.status == 1 ? '异常' : '维护' }}
            </div>
          </div>
        </div>
        <div class="text-sm leading-4.5 mt-1.5 wrap-break-word overflow-hidden line-clamp-2">
          API描述
        </div>
      </div>
      <div class="flex items-center justify-end h-10">
        <div class="btn rounded-lg bg-[#ecf0f3] w-20 leading-9.5 text-center relative" @click="checkID(item.id)">查看
        </div>
      </div>
    </div>
  </div>
  <div v-if="paging.total==0" class="mt-10">
    <el-empty :description="emptyText"/>
  </div>
  <div class="flex items-center justify-center mt-10 mb-10">
    <el-pagination
        :current-page="paging.page"
        :page-size="paging.pageSize"
        :total="paging.total"
        class="pageInfo-diy"
        layout="prev, pager, next"
        @current-change="handleNewPageChange"
    />
  </div>
</template>
<style lang="scss" scoped>
.box {
  user-select: none;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  transition: all 0.2s ease-in-out;

  .tag {
    border-radius: 6px;
    font-size: 12px;
    color: #fff;
    font-weight: 300;
    padding: 2px 4px;
    margin-left: 8px;
  }


  .btn {
    box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
    transition: all 0.2s ease-in-out;
  }

  .btn:hover {
    box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
  }

  .btn:active {
    box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
  }
}

.box:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

.tag-success {
  background-color: #67c23a;
}

.tag-buy {
  background-color: #ff8400;
}

.tag-error {
  background-color: #9e9e9e;
}

.tag-danger {
  background-color: #ff888b;
}
</style>
<style>
:root {
  --pageInfo-diy: 8px;
}

.pageInfo-diy {
  border-radius: var(--pageInfo-diy);
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6 !important;
}

.pageInfo-diy .is-first, .pageInfo-diy .is-last, .pageInfo-diy .el-pager li {
  background-color: #ECF0F3 !important;
}

.pageInfo-diy .is-first {
  border-radius: var(--pageInfo-diy) 0 0 var(--pageInfo-diy);
}

.pageInfo-diy .is-last {
  border-radius: 0 var(--pageInfo-diy) var(--pageInfo-diy) 0;
}
</style>
