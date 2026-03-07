<script setup lang="ts">

defineProps(
    {
      title: {
        type: String,
        required: true,
      }
    }
)
</script>

<template>
  <div class="rounded-lg bg-white p-6 shadow-sm">
    <div class="flex items-center justify-between">
      <h2 class="text-lg font-medium">{{ title }}</h2>
      <slot name="header"></slot>
    </div>
    <hr class="border-[#E5E5E5] m-6"/>
    <div>
      <slot></slot>
    </div>
  </div>
</template>