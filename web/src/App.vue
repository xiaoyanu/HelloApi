<script setup>
console.info(
    '%c HelloAPI %c 轻量化API系统 %c\n PoweredBy %c 周星星 %c\n GitHub %c https://github.com/xiaoyanu/HelloAPI ',
    'margin: 1em 0;padding: 5px 0px; border-radius: 3px 0 0 3px; color: #fff; background: #FF6699; font-weight: bold;',
    'padding: 5px 0px; border-radius: 0 3px 3px 0; color: #fff; background: #FF9999; font-weight: bold;',
    'color: #fff;  padding: 5px 0; background: #3498db;',
    'margin-bottom: 1em; padding: 5px 0; background: #efefef;',
    'color: #fff;  padding: 5px 0; background: #000;',
    'margin-bottom: 1em; padding: 5px 0; background: #efefef;',
)
</script>
<template>
  <router-view/>
</template>