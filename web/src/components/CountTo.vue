<template>
  <!-- 已经进行了超级优化 -->
  <span ref="spanRef"></span>
</template>

<script setup lang="ts">
import {ref, watch, onMounted, onUnmounted} from 'vue';

interface Props {
  // 数值
  value: number;
  // 滚动持续时间
  duration?: number;
  // 格式化
  formatter?: (val: number) => string | number;
}

const props = withDefaults(defineProps<Props>(), {
  duration: 1500,
});

// 1. 获取原生 DOM 节点，彻底绕开 Vue 的 VDOM Diff 和响应式渲染
const spanRef = ref<HTMLSpanElement | null>(null);

// 2. 纯内存变量，零响应式开销
let animationFrameId: number = 0;
let startTime: number | null = null;
let startValue = 0;
let currentValue = 0; // 实时记录当前帧的数值，动画被打断时能作为新起点平滑衔接

// 3. 全局复用 Intl 实例，避免高频创建 Formatter 的内存开销
const numberFormatter = new Intl.NumberFormat();

// 4. 缓动函数：EaseOutExpo (先快后慢，适合数字滚动)
const easeOutExpo = (x: number): number => {
  return x === 1 ? 1 : 1 - Math.pow(2, -10 * x);
};

const startAnimation = () => {
  if (animationFrameId) cancelAnimationFrame(animationFrameId);

  // 【核心优化 1：闭包快照隔离 Proxy】
  // 在动画开始前提取所有的 props。
  // 这样在 60FPS 的 requestAnimationFrame 循环中，就不会再触发 Vue Proxy 的 getter
  const targetValue = props.value;
  const durationVal = props.duration ?? 1500;
  const customFormatter = props.formatter;
  const fromValue = startValue;

  startTime = null;

  // 内部声明 animate，捕获上述快照变量
  const animate = (timestamp: number) => {
    if (!startTime) startTime = timestamp;

    const progress = timestamp - startTime;
    const percent = Math.min(progress / durationVal, 1);

    currentValue = fromValue + (targetValue - fromValue) * easeOutExpo(percent);

    // 【核心优化 2：局部变量缓存 Ref 引用】
    const el = spanRef.value;
    if (!el) return;

    if (progress < durationVal) {
      // 动画进行中：
      // 【安全保障】：使用 Math.trunc 替代按位或(|0)，完美支持超过 21.4 亿的超大数字，且正确处理负数截断
      const intVal = Math.trunc(currentValue);
      // 有 formatter 时输出纯数字字符串以极速渲染，无 formatter 走原生高并发格式化
      const text = customFormatter ? String(intVal) : numberFormatter.format(intVal);

      // 【核心优化 3：DOM 重绘拦截】只有当文本真正改变时，才操作 DOM
      if (el.textContent !== text) el.textContent = text;

      animationFrameId = requestAnimationFrame(animate);
    } else {
      // 动画结束：
      // 修正精度误差，强行对齐目标值，并应用完整的自定义格式化
      currentValue = targetValue;
      const intVal = Math.trunc(currentValue);
      const text = customFormatter
          ? String(customFormatter(intVal))
          : numberFormatter.format(intVal);

      if (el.textContent !== text) el.textContent = text;
    }
  };

  animationFrameId = requestAnimationFrame(animate);
};

watch(
    () => props.value,
    (newVal, oldVal) => {
      // 【核心优化 4：防卫拦截】值未变时短路，避免父组件无意义重渲导致的冗余动画
      if (newVal === oldVal) return;

      // 将当前滚动位置设为新起点，保证高频打断时不跳帧
      startValue = currentValue;
      startAnimation();
    }
);

onMounted(() => {
  if (props.value !== 0) {
    // 组件挂载时，从 0 滚动到目标值
    startValue = 0;
    startAnimation();
  } else {
    // 目标值本身就是 0，直接渲染跳过动画
    const customFormatter = props.formatter;
    const text = customFormatter ? String(customFormatter(0)) : numberFormatter.format(0);
    if (spanRef.value) spanRef.value.textContent = text;
  }
});

onUnmounted(() => {
  if (animationFrameId) cancelAnimationFrame(animationFrameId);
});
</script>