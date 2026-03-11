<script setup lang="ts">

import {PhHouseLine} from "@phosphor-icons/vue";
import router from "@/router";
</script>

<template>
  <banner>🎉友人帐</banner>

  <div class="min-h-75 w-full">

    <div class="grid grid-cols-[repeat(auto-fill,325px)] justify-center gap-8 mt-10 text-[#61677C]">
      <div
          class="box flex flex-col justify-between rounded-lg bg-[#ecf0f3] w-81.25 h-40 p-5 font-semibold">
        <div>
          <div class="flex justify-between items-center text-[20px]">
            <div class="w-50 overflow-hidden whitespace-nowrap text-ellipsis">你别急</div>
          </div>
          <div class="text-sm leading-4.5 mt-1.5 wrap-break-word overflow-hidden line-clamp-2">
            在做了
          </div>
        </div>
        <div class="flex items-center justify-end h-10">
          <div class="btn rounded-lg bg-[#ecf0f3] w-20 leading-9.5 text-center relative cursor-pointer">
            查看
          </div>
        </div>
      </div>
    </div>
  </div>

  <div class="backHome" @click="router.push('/')">
    <PhHouseLine size="24" weight="bold"/>
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

.backHome {
  position: fixed;
  right: 20px;
  bottom: 70px;
  height: 40px;
  width: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 5;
  cursor: pointer;
  background-color: #ecf0f3;
  border-radius: 8px;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  color: #61677C;
  transition: all 0.2s ease-in-out;
}

.backHome:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

.backHome:active {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
}
</style>