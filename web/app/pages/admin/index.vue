<script setup lang="ts">
const layout = 'admin-page'
</script>

<template>
<div>
  <NuxtLayout :name="layout">
    <slot></slot>
  </NuxtLayout>
</div>
</template>

<style scoped>

</style>