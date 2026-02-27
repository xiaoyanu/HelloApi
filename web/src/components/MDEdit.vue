<script setup>
import VMdEditor from '@kangc/v-md-editor';
import '@kangc/v-md-editor/lib/style/base-editor.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

VMdEditor.use(vuepressTheme);
const text = defineModel()
</script>

<template>
  <div class="w-full h-auto border border-[#DCDFE6] rounded-sm">
    <v-md-editor
        v-model="text"
        left-toolbar="undo redo clear | h bold italic strikethrough quote | ul ol table hr link image code"
        right-toolbar="preview sync-scroll fullscreen"
    />
  </div>
</template>

<style>
.v-md-editor {
  height: 500px;
  box-shadow: none;
}
</style>
