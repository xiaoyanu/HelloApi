<script setup lang="ts">
defineProps({
  height: { type: String, default: '350px' },
  width: { type: String, default: '100%' }
});
</script>

<template>
  <div class="skeleton-container" :style="{ width, height }">
    <div class="spinner"></div>
    <div class="text">图表加载中...</div>
  </div>
</template>

<style scoped>
.skeleton-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background: #fcfcfc;
  border-radius: 4px;
  /* 添加一个微弱的虚线边框，视觉上更有占位感 */
  border: 1px dashed #ebeef5;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #ebeef5;
  border-top: 4px solid #409EFF;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 15px;
}

.text {
  font-size: 14px;
  color: #909399;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}
</style>