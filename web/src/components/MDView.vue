<script setup>
import VMdEditor from '@kangc/v-md-editor';
defineProps(
    {
      text: {
        type: String,
        default: ''
      }
    }
)
</script>
<template>
  <v-md-editor :model-value="text" mode="preview" />
</template>
<style scoped>
:deep(.vuepress-markdown-body) {
  background-color: #ECF0F3;
}
</style>