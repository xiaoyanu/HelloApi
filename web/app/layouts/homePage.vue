<script setup lang="ts">

</script>

<template>
  <div id="head">
    <img id="logo" src="https://zxz.ee/touxiang.png">
    <a id="title" href="//api.zxz.ee">Hello API</a>
    <img id="tj" src="https://api.zxz.ee/api/api_count/">
    <div id="head-right">
      <a id="wt" href="https://wpa.qq.com/msgrd?v=3&site=qq&menu=yes&uin=1872786834">问题反馈</a>
    </div>
  </div>

  <slot></slot>

  <div id="footer">
    <p>&copy;2025 Hello API | 由<a style='color:blue' href='https://github.com/xiaoyanu/HelloAPI/'
                                   target='_blank'>HelloAPI</a>强力驱动
    </p>
  </div>
  <div id="xf">
    <a ks-tag="left" ks-text="搜索" id="searchButton">
      <svg t="1686984175067" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
           p-id="2531" id="mx_n_1686984175067" width="56" height="56">
        <path
            d="M949.76 884.3264a88.68864 88.68864 0 0 1-25.64096 62.67904 87.14752 87.14752 0 0 1-123.76576 0.16896l-164.29568-160.87552a382.4128 382.4128 0 0 1-26.43968 12.6208 382.83776 382.83776 0 0 1-300.032 0 383.38048 383.38048 0 0 1-122.48064-83.39968 391.296 391.296 0 0 1 0-550.36928 384.56832 384.56832 0 0 1 627.55328 123.648 391.00416 391.00416 0 0 1-40.704 376.57088l150.32832 156.56448a88.576 88.576 0 0 1 25.47712 62.39232z m-153.6512-444.04736c0-186.33216-150.41536-337.92-335.30368-337.92s-335.32928 151.6032-335.32928 337.92S275.89632 778.24 460.8 778.24s335.3088-151.64928 335.3088-337.96096z m-503.61344 168.90368a240.45568 240.45568 0 0 1 0-337.73568l34.63168 40.07424a183.46496 183.46496 0 0 0 0 257.50528z"
            p-id="2532" data-spm-anchor-id="a313x.7781069.0.i5" class="selected" fill="#61677C"></path>
      </svg>
    </a>
    <a ks-tag="left" ks-text="返回顶部" href="#tj">
      <svg t="1680926673355" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
           p-id="2320">
        <path
            d="M188.274526 257.534448l0-97.428983 645.061528 0 0 97.428983L188.274526 257.534448zM252.732583 515.366677l255.82655-226.355331 260.329097 226.355331-192.371332 0 0 192.362122 0 161.89625-130.911562 0 0-354.258373L252.732583 515.366677z"
            p-id="2321" fill="#61677C"></path>
      </svg>
    </a>
  </div>
</template>

<style scoped>
/* 顶部标题栏 */
#head {
  position: relative;
  height: 64px;
  line-height: 64px;
  color: #61677C;
  box-shadow: -5px -5px 20px #fff, 5px 5px 20px #d1d9e6;
  background-color: #ecf0f3;
  text-shadow: 1px 1px 0 #FFF;
}

#head span {
  font-weight: 800;
}

#head #logo {
  vertical-align: middle;
  top: 50%;
  width: 50px;
  height: 50px;
  margin-left: 20px;
  margin-top: -10px;
  border-radius: 10px;
}

#head img {
  display: inline-block;
}

#head #title {
  font-size: 24px;
}

#head #tj {
  vertical-align: middle;
  height: 60px;
  margin-top: -10px;
  margin-left: 50px;
}

#head-right {
  height: 64px;
  width: 150px;
  float: right;
}

#head #wt {
  display: inline-block;
  border-radius: 8px;
  background-color: #ecf0f3;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  transition: all 0.2s ease-in-out;
  width: 100px;
  text-align: center;
  height: 30px;
  line-height: 30px;
  font-weight: 600;
}

#head #wt:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

#head #wt:active {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
}

/* 底部版权声名 */
#footer {
  margin-top: 15px;
  background-color: #ecf0f3;
  border-top: solid 1px #d1d9e6;
  width: auto;
}

#footer p {
  margin-top: 20px;
  padding-bottom: 20px;
  color: #61677C;
  text-align: center;
}

/* 返回 */
#xf {
  position: fixed;
  right: 0;
  bottom: 0;
  height: 100px;
  width: 50px;
}

#xf a {
  display: block;
  height: 40px;
  width: 40px;
  border-radius: 8px;
  background-color: #ecf0f3;
  box-shadow: -5px -5px 20px #FFF, 5px 5px 20px #d1d9e6;
  transition: all 0.2s ease-in-out;
  margin-bottom: 10px;
}

#xf a:hover {
  box-shadow: -2px -2px 5px #FFF, 2px 2px 5px #d1d9e6;
}

#xf a:active {
  box-shadow: inset 1px 1px 2px #d1d9e6, inset -1px -1px 2px #FFF;
}

#xf a svg {
  display: block;
  margin: 0 auto;
  padding-top: 5px;
  width: 28px;
  height: 28px;
}

/* 适配手机 */
@media only screen and (max-width: 639px) {
  #head #logo {
    display: none;
  }

  #head #title {
    display: none;
  }

  #head #tj {
    height: 40px;
  }
}
</style>