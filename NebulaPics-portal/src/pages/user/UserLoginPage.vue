<template>
  <div id="userLoginPage" class="login-container">
    <div class="header">
      <h2 class="title">合创图库空间</h2>
      <div class="desc">开启您的智能视觉资产管理</div>
    </div>

    <!-- 登录方式切换 -->
    <a-radio-group v-model:value="loginMethod" class="login-method">
      <a-radio-button value="password">密码登录</a-radio-button>
      <a-radio-button value="captcha">验证码登录</a-radio-button>
    </a-radio-group>

    <a-form :model="formState" name="basic" autocomplete="off" @finish="handleSubmit">
      <a-form-item name="userAccount" :rules="[{ required: true, message: '请输入邮箱账号' }, { type: 'email', message: '请输入有效的邮箱地址' }]">
        <a-input
          v-model:value="formState.userAccount"
          placeholder="请输入邮箱账号"
          class="custom-input"
          size="large"
        >
          <template #prefix>
            <mail-outlined class="input-icon" />
          </template>
        </a-input>
      </a-form-item>

      <!-- 密码登录 -->
      <a-form-item
        v-if="loginMethod === 'password'"
        name="userPassword"
        :rules="[{ required: true, message: '请输入密码' }, { min: 8, message: '密码长度不能小于 8 位' }]"
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

      <!-- 验证码登录 -->
      <template v-else>
        <a-form-item name="captcha" :rules="[{ required: true, message: '请输入验证码' }, { len: 6, message: '验证码为6位' }]">
          <a-input
            v-model:value="formState.captcha"
            placeholder="请输入6位验证码"
            class="custom-input"
            size="large"
          >
            <template #prefix>
              <SafetyOutlined class="input-icon" />
            </template>
            <template #suffix>
              <a-button
                type="link"
                :disabled="countdown > 0"
                @click="sendCaptcha"
              >
                {{ countdown > 0 ? `${countdown}秒后重试` : '获取验证码' }}
              </a-button>
            </template>
          </a-input>
        </a-form-item>
      </template>

      <div class="tips">
        没有账号？
        <RouterLink to="/user/register" class="register-link">去注册</RouterLink>
      </div>
      <a-form-item>
        <a-button type="primary" html-type="submit" style="width: 100%" class="login-button" size="large">
          {{ loginMethod === 'password' ? '登录' : '验证码登录' }}
        </a-button>
      </a-form-item>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref } from 'vue';
import { userLoginUsingPost, sendMailCodeUsingPost } from '@/api/userController.ts';
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts';
import { message } from 'ant-design-vue';
import router from '@/router';
import { MailOutlined, KeyOutlined, SafetyOutlined } from '@ant-design/icons-vue';

const loginMethod = ref<'password' | 'captcha'>('password');
const countdown = ref(0);
let timer: number | null = null;
const loginUserStore = useLoginUserStore();

const formState = reactive({
  userAccount: '',
  userPassword: '',
  captcha: ''
});

// 发送验证码
const sendCaptcha = async () => {
  if (!formState.userAccount) {
    message.warning('请输入邮箱地址');
    return;
  }

  try {
    const res = await sendMailCodeUsingPost({ email: formState.userAccount });
    if (res.data.code === 0) {
      message.success('验证码已发送');
      startCountdown();
    }
  } catch (e) {
    message.error('发送失败，请稍后重试');
  }
};

// 倒计时处理
const startCountdown = () => {
  countdown.value = 60;
  timer = setInterval(() => {
    countdown.value--;
    if (countdown.value <= 0 && timer) {
      clearInterval(timer);
      timer = null;
    }
  }, 1000);
};

// 提交处理
const handleSubmit = async () => {
  if (loginMethod.value === 'password') {
    // 密码登录逻辑
    const res = await userLoginUsingPost({
      userAccount: formState.userAccount,
      userPassword: formState.userPassword
    });
    await handleLoginResponse(res);
  } else {
    // 验证码登录逻辑（需要添加对应的API）
    const res = await userLoginUsingPost({
      userAccount: formState.userAccount,
      captcha: formState.captcha
    });
    await handleLoginResponse(res);
  }
};

// 公共登录处理
const handleLoginResponse = async (res: any) => {
  if (res.data.code === 0 && res.data.data) {
    await loginUserStore.fetchLoginUser();
    message.success('登录成功');
    router.push('/');
  } else {
    message.error('登录失败：' + res.data.msg);
  }
};
</script>

<style scoped>
/* 新增登录方式切换样式 */
.login-method {
  width: 100%;
  margin-bottom: 24px;
}

.login-method :deep(.ant-radio-button-wrapper) {
  width: 50%;
  text-align: center;
  transition: all 0.3s;
}

.login-container {
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

.login-button {
  background: linear-gradient(135deg, #4a90e2 0%, #8e44ad 100%);
  height: 48px;
  font-weight: 500;
  border-radius: 8px;
  margin-top: 16px;
  transition: all 0.3s ease;
}

.login-button:hover {
  opacity: 0.9;
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(74, 144, 226, 0.2);
}

.tips {
  margin: 16px 0;
  text-align: center;
}

.register-link {
  color: #4a90e2;
  font-weight: 500;
  transition: color 0.2s;
}

.register-link:hover {
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

.login-container {
  animation: fadeIn 0.6s ease;
}
</style>
