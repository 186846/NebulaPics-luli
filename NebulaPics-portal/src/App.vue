<template>
  <div id="app">
    <BasicLayout />
  </div>
</template>

<script setup lang="ts">
import BasicLayout from '@/layouts/BasicLayout.vue'
import { healthUsingGet } from '@/api/mainController.ts'
import {useLoginUserStore} from "@/stores/useLoginUserStore.ts";
// import { healthUsingGet } from '@/api/mainController.ts'
// import {useLoginUserStore} from "@/stores/useLoginUserStore.ts";
const loginUserStore = useLoginUserStore()
loginUserStore.fetchLoginUser()
// 已经改为在权限校验文件中获取
// const loginUserStore = useLoginUserStore()
// loginUserStore.fetchLoginUser()
healthUsingGet().then((res) => {
  console.log(res)
})
// healthUsingGet().then((res) => {
//   console.log(res)
// })
</script>

<style scoped></style>
