<template>
  <div class="dialogue-container">
    <div class="chat-history">
      <div
        v-for="(message, index) in conversationHistory"
        :key="index"
        class="message"
        :class="message.role === user.userAccount? 'user-message' : message.role === 'AI'? 'ai-message' :'system-message'"
      >
        <span class="message-role">{{ message.role }}</span>
        <div class="message-content" v-html="renderMarkdown(message.content)"></div>
      </div>
    </div>
    <div class="input-container">
      <input v-model="userInput" placeholder="输入你的问题" class="input-field" />
      <button @click="sendMessage" class="send-button">发送</button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { callDashScopeUsingPost, getLoginUserUsingGet } from '@/api/userController.ts'
import MarkdownIt from'markdown-it'

const md = new MarkdownIt()

const userInput = ref('')
const conversationHistory = ref<{ role: string; content: string }[]>([])

// 定义用户信息对象
const user = reactive({
  id: '',
  userAccount: '',
  userName: '',
  userAvatar: '',
  userRole: '',
  createTime: '',
});

const renderMarkdown = (content: string) => {
  return md.render(content)
}

// 递归查找指定 key 的值
const findKeyInObject = (obj: any, key: string): any => {
  if (typeof obj === 'object' && obj!== null) {
    if (key in obj) {
      return obj[key]
    }
    for (const prop in obj) {
      const result = findKeyInObject(obj[prop], key)
      if (result!== undefined) {
        return result
      }
    }
  }
  return undefined
}

const sendMessage = async () => {
  if (userInput.value.trim() === '') {
    return
  }
  const originalInput = userInput.value // 保存原始输入内容
  try {
    const res1 = await getLoginUserUsingGet();
    if (res1.data.code === 0) {
      Object.assign(user, res1.data.data);
    }
    console.log(user.userAccount);
    const userMessage = { role: user.userAccount, content: userInput.value }
    conversationHistory.value.push(userMessage)

    const param = { userMessage: userInput.value }
    userInput.value = ''

    // 添加 AI 正在思考中的消息
    const thinkingMessage = { role:'system', content: 'AI正在思考中...' }
    conversationHistory.value.push(thinkingMessage)

    const res = await callDashScopeUsingPost(param)
    console.log(res.data)

    // 移除 AI 正在思考中的消息
    const thinkingMessageIndex = conversationHistory.value.findIndex(msg => msg === thinkingMessage)
    if (thinkingMessageIndex!== -1) {
      conversationHistory.value.splice(thinkingMessageIndex, 1)
    }

    // 删除所有 system-message
    conversationHistory.value = conversationHistory.value.filter(msg => msg.role!=='system')

    // 使用 findKeyInObject 查找 content 的值
    const contentValue = findKeyInObject(res.data, 'content')
    if (contentValue!== undefined) {
      const aiMessage = {
        role: 'AI',
        content: contentValue
      }
      conversationHistory.value.push(aiMessage)
    } else {
      const errorMessage = {
        role:'system',
        content: '未找到 content 字段的值'
      }
      conversationHistory.value.push(errorMessage)
    }
    userInput.value = '' // 正常响应时清空输入框
  } catch (error) {
    console.error(error)
    // 移除 AI 正在思考中的消息
    const thinkingMessageIndex = conversationHistory.value.findIndex(msg => msg.content === 'AI正在思考中...')
    if (thinkingMessageIndex!== -1) {
      conversationHistory.value.splice(thinkingMessageIndex, 1)
    }
    const errorMessage = {
      role:'system',
      content: '请求出现错误，请稍后再试'
    }
    conversationHistory.value.push(errorMessage)
    userInput.value = originalInput // 未正常响应时恢复原始输入内容
  }
}
</script>

<style scoped>
.dialogue-container {
  width: 100%;
  padding: 20px;
  border-radius: 5px;

  overflow: auto; /* 内容超出时显示滚动条 */
}

.chat-history {
  height: 300px;
  overflow-y: scroll;
  margin-bottom: 10px;
}

.message {
  margin-bottom: 10px;
  padding: 5px 10px;
  border-radius: 5px;
}

.user-message {
  background-color: #e6f7ff;
  align-self: flex-end;
}

.ai-message {
  background-color: #f0f9ff;
  align-self: flex-start;
}

.system-message {
  background-color: #f8d7da;
  align-self: center;
}

.message-role {
  font-weight: bold;
  margin-right: 5px;
}

.input-container {
  display: flex;
  align-items: center;
}

.input-field {
  flex: 1;
  padding: 5px;
  border: 1px solid #ccc;
  border-radius: 5px;
}

.send-button {
  margin-left: 10px;
  padding: 5px 10px;
  border: none;
  border-radius: 5px;
  background-color: #007bff;
  color: white;
  cursor: pointer;
}
</style>
