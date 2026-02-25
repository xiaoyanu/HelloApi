<script setup lang="ts">
import {useRoute} from "vue-router";

const route = useRoute()
const keywords = route.params.keywords
</script>

<template>
  <banner>
    🔍 {{ keywords }} 的搜索结果
  </banner>

</template>

<style scoped lang="scss">

</style>