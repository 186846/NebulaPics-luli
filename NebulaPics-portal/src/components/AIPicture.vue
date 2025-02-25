<template>
  <div class="ai-picture-creation">
    <form @submit.prevent="createTask" class="form">
      <div class="form-group">
        <label for="prompt">描述:</label>
        <input type="text" id="prompt" v-model="prompt" required class="input-field" />
      </div>
      <button type="submit" :disabled="loading" class="submit-button">
        {{ loading ? '创建中...' : '创建图片' }}
      </button>
    </form>

    <!-- 展示上传成功的图片 -->
    <div v-if="uploadedImageUrl" class="url-picture-upload">
      <div class="img-wrapper">
        <img :src="uploadedImageUrl" alt="Uploaded Image" />
      </div>
      <div>
        <button @click="copyImageUrl" class="copy-button">复制图片</button>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import {  ref } from 'vue'
import { createPictureTaskUsingPost, getPictureTaskUsingGet } from '@/api/pictureController.ts'
import { message } from 'ant-design-vue'

// 定义创建任务所需参数
const prompt = ref('')
const taskId = ref<string>()
const loading = ref(false)
const uploadedImageUrl = ref<string>() // 只保留上传成功后的图片 URL
let pollingInterval: number | null = null // 用于存储轮询的 interval ID

interface Props {}
const props = defineProps<Props>()

// 创建任务
const createTask = async () => {
  loading.value = true
  try {
    const body = {
      prompt: prompt.value,
      parameters: {
        n: 1,
      },
    }
    const res = await createPictureTaskUsingPost(body)
    if (res.data.code === 0 && res.data.data) {
      message.success('创建任务成功，请耐心等待，不要退出界面')
      taskId.value = res.data.data.output.taskId
      // 开启轮询
      startPolling(taskId.value)
    } else {
      message.error('图片任务失败，' + res.data.msg)
    }
  } catch (error) {
    console.error('创建任务失败', error)
    message.error('创建任务失败，' + (error as Error).message)
  }
  loading.value = false
}

// 轮询任务状态
const pollTaskStatus = async (taskId: string) => {
  try {
    const res = await getPictureTaskUsingGet({ taskId })
    if (res.data.code === 0) {
      if (res.data.data) {
        const taskResult = res.data.data.output

        if (taskResult.taskStatus === 'SUCCEEDED') {
          if (taskResult.results?.[0]?.url) {
            uploadedImageUrl.value = taskResult.results[0].url
          }
          return true
        } else if (taskResult.taskStatus === 'FAILED') {
          message.error('图片创建任务执行失败')
          return true
        }
      }
    }
  } catch (error) {
    console.error('图片创建任务轮询失败', error)
    message.error('图片创建任务轮询失败，' + (error as Error).message)
    return true
  }
  return false
}

// 开始轮询
const startPolling = (taskId: string) => {
  pollingInterval = setInterval(async () => {
    const shouldStop = await pollTaskStatus(taskId)
    if (shouldStop) {
      stopPolling()
    }
  }, 3000)
}

// 停止轮询
const stopPolling = () => {
  if (pollingInterval) {
    clearInterval(pollingInterval)
    pollingInterval = null
  }
}

// 复制图片 URL
const copyImageUrl = () => {
  if (uploadedImageUrl.value) {
    navigator.clipboard.writeText(uploadedImageUrl.value)
      .then(() => {
        message.success('图片 URL 复制成功')
      })
      .catch((error) => {
        console.error('复制图片 URL 失败', error)
        message.error('复制图片 URL 失败，' + (error as Error).message)
      })
  }
}
</script>

<style scoped>
.ai-picture-creation {
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  max-width: 600px;
  margin: 0 auto;
}

.title {
  color: #333;
  margin-bottom: 20px;
}

.form {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.form-group {
  margin-bottom: 20px;
}

label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.input-field {
  width: 100%;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  box-sizing: border-box;
  transition: border-color 0.3s ease;
}

.input-field:focus {
  border-color: #007bff;
  outline: none;
}

.submit-button {
  padding: 10px 20px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.submit-button:hover {
  background-color: #0056b3;
}

.submit-button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

.url-picture-upload {
  margin-bottom: 16px;
  width: 100%;
}

.url-picture-upload img {
  max-width: 100%;
  max-height: 480px;
  border-radius: 4px;
  box-shadow: 0 0 5px rgba(0, 0, 0, 0.3);
  animation: fadeIn 0.5s ease;
}

.url-picture-upload .img-wrapper {
  text-align: center;
  margin-top: 16px;
  position: relative;
}

.copy-button {
  position: absolute;
  bottom: 20px; /* 增加 bottom 的值 */
  right: 20px;  /* 增加 right 的值 */
  padding: 5px 10px;
  background-color: #007bff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s ease;
}

.copy-button:hover {
  background-color: #0056b3;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }
  to {
    opacity: 1;
  }
}
</style>
