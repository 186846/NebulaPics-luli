<template>
  <div id="userRegisterPage" class="register-container">
    <div class="header">
      <h2 class="title">合创图库空间</h2>
      <div class="desc">开启您的智能视觉资产管理</div>
    </div>

    <a-form class="form" :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入账号' }]">
        <a-input
          v-model:value="formState.userAccount"
          placeholder="请输入邮箱账号"
          class="custom-input"
          size="large"
        >
          <template #prefix>
            <mail-outlined class="input-icon" />
          </template>
          <template #suffix>
            <a-button
              type="link"
              :disabled="isSendingCode"
              @click="sendVerificationCode"
              class="code-btn"
              size="large"
            >
              {{ isSendingCode ? `${countdown}秒后重试` : '获取验证码' }}
            </a-button>
          </template>
        </a-input>
      </a-form-item>
      <a-form-item name="code" :rules="[{ required: true, message: '请输入短信验证码' }]">
        <a-input
          v-model:value="formState.code"
          placeholder="请输入短信验证码"
          class="custom-input"
          size="large"
        >
          <template #prefix>
            <lock-outlined class="input-icon" />
          </template>
        </a-input>
      </a-form-item>

      <a-form-item
        name="userPassword"
        :rules="[
          { required: true, message: '请输入密码' },
          { min: 8, message: '密码长度不能小于 8 位' },
        ]"
      >
        <a-input-password
          v-model:value="formState.userPassword"
          placeholder="请输入密码"
          class="custom-input"
          size="large"
        >
          <template #prefix>
            <key-outlined class="input-icon" />
          </template>
        </a-input-password>
      </a-form-item>

      <a-form-item
        name="checkPassword"
        :rules="[
          { required: true, message: '请输入确认密码' },
          { min: 8, message: '确认密码长度不能小于 8 位' },
        ]"
      >
        <a-input-password
          v-model:value="formState.checkPassword"
          placeholder="请输入确认密码"
          class="custom-input"
          size="large"
        >
          <template #prefix>
            <key-outlined class="input-icon" />
          </template>
        </a-input-password>
      </a-form-item>

      <div class="tips">
        已有账号？
        <RouterLink to="/user/login" class="login-link">去登录</RouterLink>
      </div>

      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%" class="register-button" size="large">
          注册
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { userRegisterUsingPost, sendMailCodeUsingPost } from '@/api/userController.ts';
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts';
import { message } from 'ant-design-vue';
import router from '@/router';
import { MailOutlined, LockOutlined, KeyOutlined } from '@ant-design/icons-vue';

// 用于接受表单输入的值
const formState = reactive<{
  userAccount: string;
  userPassword: string;
  code: string;
  checkPassword: string;
}>({
  userAccount: '',
  userPassword: '',
  code: '',
  checkPassword: '',
});

const loginUserStore = useLoginUserStore();

// 发送验证码状态管理
const isSendingCode = ref(false);
const countdown = ref(60);
let timer: number | null = null;

/**
 * 发送验证码
 */
const sendVerificationCode = async () => {
  if (isSendingCode.value) return;
  if (!formState.userAccount) {
    message.error('请先输入账号');
    return;
  }

  const email = {
    email: formState.userAccount,
  };

  isSendingCode.value = true;
  try {
    const res = await sendMailCodeUsingPost(email);
    if (res.data.code === 0) {
      message.success('验证码发送成功');
      timer = setInterval(() => {
        if (countdown.value > 0) {
          countdown.value--;
        } else {
          clearInterval(timer!);
          isSendingCode.value = false;
          countdown.value = 60;
        }
      }, 1000);
    } else {
      message.error('验证码发送失败，' + res.data.msg);
      isSendingCode.value = false;
    }
  } catch (error) {
    message.error('验证码发送失败，请稍后重试');
    isSendingCode.value = false;
  }
};

/**
 * 提交表单
 * @param values
 */
const handleSubmit = async (values: any) => {
  if (values.userPassword !== values.checkPassword) {
    message.error('两次输入的密码不一致');
    return;
  }
  const res = await userRegisterUsingPost(values);
  if (res.data.code === 0 && res.data.data) {
    message.success('注册成功');
    router.push({
      path: '/user/login',
      replace: true,
    });
  } else {
    message.error('注册失败，' + res.data.msg);
  }
};
</script>

<style scoped>
.register-container {
  max-width: 440px;
  margin: 40px auto;
  padding: 40px;
  background: #ffffff;
  border-radius: 16px;
  box-shadow: 0 12px 28px rgba(0, 0, 0, 0.08);
}

.header {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 28px;
  font-weight: 600;
  color: #1a1a1a;
  margin-bottom: 8px;
  background: linear-gradient(135deg, #4a90e2 0%, #8e44ad 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.desc {
  color: #666;
  font-size: 15px;
  letter-spacing: 0.5px;
}

.form {
  margin-top: 24px;
}

.custom-input {
  border-radius: 8px;
  padding: 12px 16px;
  border: 1px solid #e0e0e0;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

.custom-input:hover {
  border-color: #4a90e2;
}

.custom-input:focus {
  border-color: #4a90e2;
  box-shadow: 0 0 0 2px rgba(74, 144, 226, 0.1);
}

.code-btn {
  background: linear-gradient(135deg, #4a90e2 0%, #8e44ad 100%);
  color: white;
  border: none;
  border-radius: 8px;
  transition: all 0.3s ease;
}

.code-btn:not([disabled]):hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.2);
}

.code-btn[disabled] {
  background: #f5f5f5;
  color: #999;
}

.register-button {
  background: linear-gradient(135deg, #4a90e2 0%, #8e44ad 100%);
  height: 48px;
  font-weight: 500;
  border-radius: 8px;
  margin-top: 16px;
  transition: all 0.3s ease;
}

.register-button:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.2);
}

.tips {
  margin: 16px 0;
  text-align: center;
}

.login-link {
  color: #4a90e2;
  font-weight: 500;
  transition: color 0.2s;
}

.login-link:hover {
  color: #357abd;
}

.input-icon {
  color: #999;
  margin-right: 8px;
}

@keyframes fadeIn {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.register-container {
  animation: fadeIn 0.6s ease;
}
</style>
