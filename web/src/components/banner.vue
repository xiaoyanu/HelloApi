<template>
  <div>
    <h1 class="tracking-[1px] text-center text-white leading-100 text-[32px] font-bold"><slot></slot></h1>
  </div>
</template>
<style lang="scss" scoped>
div {
  user-select: none;
  height: 400px;
  background-image: url(@/assets/images/Top_Picture.gif);
  background-position-x: center;
  background-position-y: center;
  background-size: cover;

  h1 {
    text-shadow: rgb(0 0 0 / 50%) 0 3px 5px;
  }
}

@media only screen and (max-width: 639px) {
  div {
    height: 150px;
  }
  h1{
    line-height: 150px;
    font-size: 24px;
  }
}
</style>