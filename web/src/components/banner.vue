<script setup lang="ts">
defineProps({
  title: {
    type: String,
    required: true,
  },
})
</script>

<template>
  <div>
    <h1 class="text-center text-white leading-100 text-[32px]">{{ title }}</h1>
  </div>
</template>
<style lang="scss" scoped>
div {
  height: 400px;
  background-image: url(@/assets/images/Top_Picture.gif);
  background-position-x: center;
  background-position-y: center;
  background-size: cover;

  h1 {
    text-shadow: rgb(0 0 0 / 50%) 0 3px 5px;
  }
}
</style>