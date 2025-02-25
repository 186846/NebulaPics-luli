<template>
  <div>
    <a-modal v-model:visible="visible" :title="title" :footer="false" @cancel="closeModal">
      <h4>复制分享链接</h4>
      <a-typography-link copyable>
        {{ link }}
      </a-typography-link>
      <div style="margin-bottom: 16px" />
      <h4>手机扫码查看</h4>
      <a-qrcode error-level="H" :value="link" icon="https://luli-1319967679.cos.ap-guangzhou.myqcloud.com/public/1887797628540932097/2025-02-08_kgwntQOL65VBfevq.webp"></a-qrcode>
    </a-modal>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
interface Props {
  title: string;
  link: string;
}
const props = withDefaults(defineProps<Props>(), {
  title: "分享图片",
  link: 'https://www.codefather.cn'
})
// 是否可见
const visible = ref(false)
// 打开弹窗
const openModal = () => {
  visible.value = true
}
// 关闭弹窗
const closeModal = () => {
  visible.value = false;
}
// 暴露函数给父组件
defineExpose({
  openModal,
})
</script>
