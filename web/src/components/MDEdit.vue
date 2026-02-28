<script setup>
import VMdEditor from '@kangc/v-md-editor';

const text = defineModel()
</script>

<template>
  <div class="deleteStyle w-full h-auto border border-[#DCDFE6] rounded-sm">
    <v-md-editor
        v-model="text"
        left-toolbar="undo redo clear | h bold italic strikethrough quote | ul ol table hr link image code"
        right-toolbar="preview sync-scroll fullscreen"
    />
  </div>
</template>

<style scoped>
:deep(.v-md-editor) {
  height: 500px;
  box-shadow: none
}
</style>
