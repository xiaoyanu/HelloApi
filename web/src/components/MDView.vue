<script setup>
import VMdPreview from '@kangc/v-md-editor/lib/preview';
import '@kangc/v-md-editor/lib/style/preview.css';
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js';
import '@kangc/v-md-editor/lib/theme/style/vuepress.css';

VMdPreview.use(vuepressTheme);
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
  <v-md-preview :text="text">
  </v-md-preview>
</template>