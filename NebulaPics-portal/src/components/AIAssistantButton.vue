<template>
  <div id="app">
    <Tooltip placement="topLeft" title="AI机器人">
      <a-float-button
        type="primary"
        size="large"
        :style="{
          shape: 'circle',
          left: '90px',
        }"
        @click="showChat = true"
      >
        <template #icon>
          <RobotOutlined />
        </template>
      </a-float-button>
    </Tooltip>
    <a-modal
      :visible="showChat"
      title="AI对话"
      @cancel="showChat = false"
      :footer="null"
    >
      <ChatComponent @close="showChat = false" />
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { RobotOutlined } from '@ant-design/icons-vue';
import ChatComponent from '@/components/ChatComponent.vue';
import { Tooltip } from 'ant-design-vue';
import { Modal } from 'ant-design-vue';

const showChat = ref(false);
</script>

<style scoped>
/* 自定义按钮大小 */
.ant-float-btn {
  width: 60px;
  height: 60px;
}

/* 自定义图标大小 */
.ant-float-btn-icon {
  font-size: 70px;
}
</style>
