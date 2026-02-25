<script setup lang="ts">
const checkID = (id: number) => {
  window.open("/info/" + id)
}
</script>
<template>
  <banner>Hello API</banner>
  <div class="grid grid-cols-[repeat(auto-fill,325px)] justify-center gap-8 mt-8 mb-8 text-[#61677C]">
    <div class="box flex flex-col justify-between rounded-lg bg-[#ecf0f3] w-81.25 h-40 p-5 font-semibold"
         v-for="item in 50"
         :key="item">
      <div>
        <div class="flex justify-between items-center text-[20px]">
          <div class="w-50 overflow-hidden whitespace-nowrap  text-ellipsis">哔哩哔哩视频解析</div>
          <div class="flex">
            <div class="tag tag-buy">收费</div>
            <div class="tag tag-success">正常</div>
          </div>
        </div>
        <div class="text-sm leading-4.5 mt-1.5 wrap-break-word overflow-hidden line-clamp-2">
          API描述
        </div>
      </div>
      <div class="flex items-center justify-end h-10">
        <div @click="checkID(item)" class="btn rounded-lg bg-[#ecf0f3] w-20 leading-9.5 text-center relative">查看</div>
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
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
  background-color: #ff4d4f;
}
</style>