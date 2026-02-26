<script setup lang="ts">
import { defineAsyncComponent, ref } from "vue";
import {
  PhUserCircle, PhChartPieSlice, PhChartBar, PhArrowBendRightDown, PhArrowBendLeftUp, PhCalendar
} from "@phosphor-icons/vue";
import { formatNumber } from "@/utils/more";

// 异步按需加载图表组件
const BaseChart = defineAsyncComponent(() => import('@/components/BaseChart.vue'));

const card = [
  {text: '用户数量', value: 10000, icon: PhUserCircle},
  {text: '累计调用', value: 500030000, icon: PhChartBar},
  {text: '今日调用', value: 20200, delta: -100000000, tip: '较昨日', icon: PhChartPieSlice},
  {text: '本周调用', value: 1000, delta: 12200, tip: '较上周', icon: PhCalendar},
];

const getRandomData = (count: number, min: number, max: number) =>
    Array.from({ length: count }, () => Math.floor(Math.random() * (max - min + 1)) + min);

// 图表 1 配置 (近7天调用统计)
const chart1Options = ref({
  tooltip: {
    trigger: 'axis',
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    padding: [10, 15],
    textStyle: { color: '#333' },
    extraCssText: 'box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); border-radius: 4px;'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
  xAxis: {
    type: 'category',
    boundaryGap: false,
    data: ['02-21', '02-22', '02-23', '02-24', '02-25', '02-26', '02-27'],
    axisLine: { show: false },
    axisTick: { show: false },
    axisLabel: { color: '#909399', margin: 15 }
  },
  yAxis: {
    type: 'value',
    axisLabel: { color: '#909399' },
    splitLine: { lineStyle: { color: '#ebeef5', type: 'dashed' } }
  },
  series: [
    {
      name: '调用量',
      type: 'line',
      smooth: true,
      symbol: 'circle',
      symbolSize: 8,
      showSymbol: true,
      data: getRandomData(7, 300000, 900000),
      itemStyle: { color: '#409EFF', borderWidth: 2, borderColor: '#fff' },
      lineStyle: { width: 3, color: '#409EFF' },
      // 巧妙之处：使用纯对象声明渐变，无需引入 echarts.graphic
      areaStyle: {
        color: {
          type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: 'rgba(64, 158, 255, 0.3)' },
            { offset: 1, color: 'rgba(64, 158, 255, 0.05)' }
          ]
        }
      },
      label: {
        show: true,
        position: 'top',
        color: '#1f2d3d',
        fontWeight: 'bold',
        formatter: (params: any) => params.value.toLocaleString()
      }
    }
  ]
});

// 图表 2 配置 (今日热门接口)
const chart2Options = ref({
  tooltip: {
    trigger: 'axis',
    axisPointer: { type: 'shadow' },
    backgroundColor: 'rgba(255, 255, 255, 0.9)',
    padding: [10, 15],
    textStyle: { color: '#333' },
    extraCssText: 'box-shadow: 0 0 10px rgba(0, 0, 0, 0.1); border-radius: 4px;'
  },
  grid: { left: '3%', right: '4%', bottom: '3%', top: '15%', containLabel: true },
  xAxis: {
    type: 'category',
    data: ['/api/login', '/api/getUser', '/api/order/list', '/api/pay', '/api/upload'],
    axisLine: { show: false },
    axisTick: { show: false },
    axisLabel: { color: '#909399', margin: 15, interval: 0, rotate: 15 }
  },
  yAxis: {
    type: 'value',
    axisLabel: { color: '#909399' },
    splitLine: { lineStyle: { color: '#ebeef5', type: 'dashed' } }
  },
  series: [
    {
      name: '请求次数',
      type: 'bar',
      barWidth: '35%',
      data: getRandomData(5, 5000, 50000),
      itemStyle: {
        borderRadius: [4, 4, 0, 0],
        // 同样使用纯对象声明渐变
        color: {
          type: 'linear', x: 0, y: 0, x2: 0, y2: 1,
          colorStops: [
            { offset: 0, color: '#38D677' },
            { offset: 1, color: 'rgba(56, 214, 119, 0.1)' }
          ]
        }
      },
      label: {
        show: true,
        position: 'top',
        color: '#1f2d3d',
        fontWeight: 'bold',
        formatter: (params: any) => params.value.toLocaleString()
      }
    }
  ]
});
</script>

<template>
  <div>
    <div class="grid grid-cols-4 gap-5 mb-5">
      <div class="card" v-for="item in card" :key="item.text">
        <div class="flex items-center justify-center mr-3.75 text-[#38D677]">
          <component :is="item.icon" size="60" weight="duotone" class="stat-icon"/>
        </div>
        <div>
          <h3 class="text-[14px] text-[#606266]">{{ item.text }}</h3>
          <div class="font-bold text-[24px]">{{ formatNumber(item.value) }}</div>
          <div v-if="item?.delta" :class="{'down': item.delta < 0, 'up': item.delta > 0}"
               class="text-[12px] flex items-center">
            <PhArrowBendRightDown v-if="item.delta < 0" class="inline"/>
            <PhArrowBendLeftUp v-else class="inline"/>
            {{ item.tip }} {{ formatNumber(item.delta) }}
          </div>
        </div>
      </div>
    </div>

    <div class="grid grid-cols-2 gap-5">
      <div class="chart-card">
        <div class="chart-header">近7天调用统计</div>
        <BaseChart :options="chart1Options" />
      </div>
      <div class="chart-card">
        <div class="chart-header">今日热门接口</div>
        <BaseChart :options="chart2Options" />
      </div>
    </div>
  </div>
</template>

<style scoped lang="scss">
.card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  display: flex;
  align-items: center;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  border: 1px solid #ebeef5;
  height: 120px;
}
.down { color: #fa6161; }
.up { color: #39b16c; }
.chart-card {
  background: #fff;
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
  border: 1px solid #ebeef5;
  .chart-header {
    font-size: 16px;
    font-weight: bold;
    margin-bottom: 20px;
    color: #303133;
  }
}
</style>