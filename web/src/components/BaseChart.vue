<script setup lang="ts">
import { ref, onMounted, onUnmounted, watch, markRaw } from "vue";
// 1. 引入 echarts 核心模块
import * as echarts from "echarts/core";
// 2. 引入用到的图表（按需引入：折线图、柱状图）
import { LineChart, BarChart } from "echarts/charts";
// 3. 引入提示框、网格等组件
import { TooltipComponent, GridComponent } from "echarts/components";
// 4. 引入 Canvas 渲染器（必须）
import { CanvasRenderer } from "echarts/renderers";

// 5. 注册必须的组件
echarts.use([
  TooltipComponent,
  GridComponent,
  LineChart,
  BarChart,
  CanvasRenderer
]);

const props = defineProps({
  options: {
    type: Object,
    required: true
  },
  width: {
    type: String,
    default: "100%"
  },
  height: {
    type: String,
    default: "350px"
  }
});

const chartRef = ref<HTMLElement | null>(null);
let chartInstance: echarts.ECharts | null = null;

const initChart = () => {
  if (!chartRef.value) return;
  // markRaw 避免 Vue 响应式代理造成的性能卡顿
  chartInstance = markRaw(echarts.init(chartRef.value));
  chartInstance.setOption(props.options);
};

const handleResize = () => {
  chartInstance?.resize();
};

onMounted(() => {
  initChart();
  window.addEventListener("resize", handleResize);
});

onUnmounted(() => {
  window.removeEventListener("resize", handleResize);
  chartInstance?.dispose();
});

// 监听 options 变化，实现数据动态响应
watch(
    () => props.options,
    (newOptions) => {
      chartInstance?.setOption(newOptions, true);
    },
    { deep: true }
);
</script>

<template>
  <div ref="chartRef" :style="{ width: props.width, height: props.height }"></div>
</template>