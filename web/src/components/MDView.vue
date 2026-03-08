<script setup>
import VMdEditor from '@kangc/v-md-editor';

defineProps(
    {
      text: {
        type: String,
        default: ''
      },
      bgColor: {
        type: String,
        default: '#ECF0F3'
      },
      fontColor: {
        type: String,
        default: '#61677c'
      },
    }
)
</script>
<template>
  <v-md-editor :model-value="text" mode="preview"/>
</template>
<style scoped>
:deep(.vuepress-markdown-body) {
  background-color: v-bind(bgColor);
  color: v-bind(fontColor);
  padding: 10px;
}
</style>