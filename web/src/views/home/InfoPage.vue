<script setup lang="ts">
import {useRoute} from 'vue-router'
import {DocumentCopy} from '@element-plus/icons-vue'

const route = useRoute()
const id = route.params.id

const copyAddress = () => {
  ElMessage.success('复制成功')
}
</script>
<template>
  <banner>接口名称</banner>
  <div id="box" class="rounded-[15px] mx-auto mt-8 mb-8 w-275 flex p-3.75 flex-col text-[#61677c] text-[18px]">
    <div id="name" class="pb-3.75 text-[27px] font-bold">接口名称</div>
    <div id="request" class="pb-3.75 pt-3.75">
      <div class="flex items-center">请求地址：
        <a class="link" target="_blank" href="//">https://api.zxz.ee/api/mhycos/?type=&num=</a>
        <el-tooltip
            content="复制地址"
            placement="top-start"
        >
          <div @click="copyAddress" id="copy"
               class="bg-[#ecf0f3] ml-2 rounded-lg flex items-center justify-center w-7 h-7">
            <el-icon size="16">
              <DocumentCopy/>
            </el-icon>
          </div>
        </el-tooltip>
      </div>
      <div>请求方式：<span class="link">GET</span></div>
      <div>返回格式：<span class="link">JSON</span></div>
    </div>
    <div class="pt-3.75 pb-3.75">
      <div class="text-[20px] border-l-[5px] border-[#61677C] pl-1 mb-2">请求参数</div>
      <table>
        <tbody>
        <tr>
          <th class="th-r">参数名</th>
          <th class="th-r">必填</th>
          <th class="th-r">类型</th>
          <th>描述</th>
        </tr>
        <tr>
          <td class="td-r">
            <div class="tag-box">
              <span class="tag tag-name">type</span>
            </div>
          </td>
          <td class="td-r">
            <div class="tag-box">
              <span class="tag tag-required-yes">是</span>
            </div>
          </td>
          <td class="td-r">
            <div class="tag-box">
              <span class="tag tag-type">Int</span>
            </div>
          </td>
          <td>
            操作类型，1：查询，2：新增，3：修改，4：删除
          </td>
        </tr>
        </tbody>
      </table>
    </div>
    <div class="pt-3.75">
      <div class="text-[20px] border-l-[5px] border-[#61677C] pl-1 mb-2">返回示例</div>
      <div id="response">
        <m-d-view :text="`# 你好`" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
#box {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
  user-select: none;

  #name, #request {
    border-bottom: solid 1px #d1d9e6;
  }

  #request {
    .link {
      border-bottom: solid 1px #000;
    }

    #copy {
      cursor: pointer;
      box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
      transition: all 0.2s ease-in-out;
    }

    #copy:hover {
      box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
    }

    #copy:active {
      box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
    }
  }

  table {
    border-radius: 8px;
    border: solid 1px #d1d9e6;
    width: 100%;
    text-align: center;
    border-spacing: 0;
    border-collapse: separate;
    align-items: center;

    td {
      user-select: text;

    }

    th, td {
      padding: 4px;
      border: none;
      vertical-align: middle;
    }

    .th-r, .td-r {
      border-right: solid .5px #d1d9e6;
    }

    td {
      max-width: 100px;
      border-top: solid .5px #d1d9e6;
      word-break: break-all;
      display: table-cell;
    }
  }

  #response {
    border-radius: 8px;
    border: solid 1px #d1d9e6;
    width: 100%;
    padding: 10px;
  }
}

.tag {
  font-size: 14px;
  padding: 0 8px;
  border-radius: 6px;
  border-width: 1px;
  border-style: solid;
}

.tag-required-yes {
  background: #FEF0F0;
  color: #F56C6C;
  border-color: #FDE2E2;
}

.tag-required-no {
  background: #F4F4F5;
  color: #909399;
  border-color: #E9E9EB;
}

.tag-name {
  background: #ECF5FF;
  color: #409EFF;
  border-color: #D9ECFF;
}

.tag-type {
  background: #FDF6EC;
  color: #e6a32e;
  border-color: #FAECD8;
}

.tag-box {
  display: flex;
  align-items: center;
  justify-content: center;
}

</style>