<template>
  <div class="forum-container">
    <!-- 排序选择 -->
    <div class="action-bar">
      <a-radio-group v-model:value="sortField" class="sort-radio-group">
        <a-radio-button value="createTime" class="sort-button">最新</a-radio-button>
        <a-radio-button value="viewCount" class="sort-button">热度</a-radio-button>
      </a-radio-group>
      <a-button type="primary" class="add-post-button" @click="goToForum">添加话题</a-button>
    </div>

    <!-- 帖子列表 -->
    <a-list
      :data-source="displayedPosts"
      item-layout="horizontal"
      class="post-list"
      :loading="loading"
    >
      <template #header>
        <h2 class="list-header">论坛帖子列表</h2>
      </template>

      <template #renderItem="{ item }">
        <a-list-item class="post-item">
          <template #extra>
            <div class="post-stats">
              <span class="stat-item">
                <message-outlined class="stat-icon" />
                {{ item.replyCount }}
              </span>
              <span class="stat-item">
                <eye-outlined class="stat-icon" />
                {{ item.viewCount }}
              </span>
              <!-- 模板部分添加 -->
              <a-tooltip :title="item.action  ? '取消点赞' : '点赞'">
                <component
                  :is="item.action  === 'liked' ? HeartFilled : HeartOutlined"
                  :class="['like-icon', { liked: item.action  === 'liked' }]"
                  @click="handleLike(item)"
                />
                <span class="action-count">{{ item.heartCount || 0 }}</span>
              </a-tooltip>
            </div>
          </template>

          <a-list-item-meta class="post-meta">
            <template #avatar>
              <a-avatar
                v-if="item.user.userAvatar"
                :src="item.user.userAvatar"
                class="user-avatar"
              />
              <a-avatar
                v-else
                :style="{ backgroundColor: stringToColor(item.user.userName) }"
                class="user-avatar"
              >
                {{ item.user.userName[0] }}
              </a-avatar>
            </template>

            <template #title>
              <a class="post-title" @click="goToDetail(item.id)">{{ item.title }}</a>
            </template>

            <template #description>
              <div class="meta-content">
                <a-tag
                  v-for="(cat, index) in item.category"
                  :key="index"
                  color="blue"
                  class="category-tag"
                >
                  {{ cat }}
                </a-tag>
                <span class="timestamp">
                  {{ formatTime(item.createTime) }}
                </span>
              </div>
            </template>
          </a-list-item-meta>
        </a-list-item>
      </template>
    </a-list>

    <!-- 分页组件 -->
    <a-pagination
      class="pagination"
      v-model:current="searchParams.current"
      v-model:pageSize="searchParams.pageSize"
      :total="total"
      @change="handlePageChange"
    />
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch, nextTick, reactive } from 'vue'
import { useRouter } from 'vue-router';
import { MessageOutlined, EyeOutlined, HeartOutlined, HeartFilled } from '@ant-design/icons-vue'
import { listPostVoByPageUsingPost, updatePostUsingPost } from '@/api/postController.ts'
import { message } from 'ant-design-vue';

const router = useRouter();
const params = ref('');
// 排序类型
const sortField = ref("createTime");

// 搜索条件
const searchParams = reactive({
  current: 1,
  pageSize: 5,
  sortField: 'createTime',
  sortOrder: 'descend',
});

// 总条数
const total = ref(0);

// 当前显示的帖子列表
const displayedPosts = ref([]);
const loading = ref(true);

// 根据分页获取数据
const fetchData = async () => {
  loading.value = true;
  try {
    // 更新搜索参数中的排序字段
    searchParams.sortField = sortField.value;

    const res = await listPostVoByPageUsingPost(searchParams);
    if (res.data.code === 0 && res.data.data) {
      displayedPosts.value = res.data.data.records || [];
      total.value = res.data.data.total || 0;
    } else {
      message.error('获取帖子数据出错: ' + res.data.msg);
    }
  } catch (error) {
    message.error('获取帖子数据出错: ' + error.message);
  } finally {
    loading.value = false;
  }
};

//进行点赞
const handleLike = async (item) => {
  if (item.action === 'liked'){
    item.action = '';
    item.heartCount--;
  }else {
    item.action = 'liked';
    item.heartCount++;
  }
  await updatePostUsingPost(item);
};

// 监听分页变化
const handlePageChange = async (current, pageSize) => {
  searchParams.current = current;
  searchParams.pageSize = pageSize;
  await nextTick();
  await fetchData();
};

// 初始化加载数据
onMounted(() => {
  fetchData();
});

watch(
  () => sortField.value,
  () => {
    searchParams.current = 1;
    fetchData();
  },
  { immediate: true }  // 初始化时立即执行
);

// 跳转到帖子详情页
const goToDetail = (postId) => {
  router.push(`/post/${postId}`).catch((error) => {
    console.error('路由跳转出错:', error);
  });
};

// 跳转到添加帖子页
const goToForum = () => {
  router.push('/add_post').catch((error) => {
    console.error('路由跳转出错:', error);
  });
};

// 工具函数：根据字符串生成颜色
const stringToColor = (str) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  return `hsl(${hash % 360}, 50%, 80%)`;
};

// 工具函数：格式化时间
const formatTime = (dateTime) => {
  const date = new Date(dateTime);
  return date.toLocaleString();
};
</script>

<style scoped>
/* 核心容器 */
.forum-container {
  max-width: 1200px;
  margin: 2rem auto;
  padding: 2rem;
  background: linear-gradient(145deg, #f8f9fa 0%, #f1f3f5 100%);
  border-radius: 16px;
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(241, 243, 245, 0.6);
}

/* 操作栏 */
.action-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 1rem 1.5rem;
  margin-bottom: 1.5rem;
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(4px);
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.03);
}

.sort-radio-group {
  display: inline-flex;
  gap: 0.5rem;
  background: rgba(241, 243, 245, 0.6);
  padding: 0.25rem;
  border-radius: 8px;
}

.sort-button {
  border: none !important;
  background: rgba(24, 144, 255, 0.1) !important;
  color: #1890ff !important;
  transition: all 0.2s ease;
  border-radius: 6px !important;
}

.sort-button.ant-radio-button-wrapper-checked  {
  background: #1890ff !important;
  color: white !important;
  box-shadow: 0 2px 6px rgba(24, 144, 255, 0.2);
}

.add-post-button {
  background: linear-gradient(135deg, #1890ff 0%, #096dd9 100%);
  box-shadow: 0 4px 12px rgba(24, 144, 255, 0.3);
  border: none;
  border-radius: 8px;
  height: 40px;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  transition: transform 0.2s, box-shadow 0.2s;
}

.add-post-button:hover {
  transform: translateY(-1px);
  box-shadow: 0 6px 16px rgba(24, 144, 255, 0.3);
}

/* 帖子列表 */
.post-list {
  background: transparent;
  border-radius: 12px;
  overflow: hidden;
}

.list-header {
  font-size: 1.75rem;
  font-weight: 600;
  color: #2d3436;
  margin-bottom: 1.5rem;
  padding-left: 0.5rem;
  position: relative;
}

.list-header::before {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  transform: translateY(-50%);
  width: 4px;
  height: 70%;
  background: #1890ff;
  border-radius: 2px;
}

.post-item {
  padding: 1.5rem;
  margin-bottom: 0.75rem;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 12px;
  border: 1px solid rgba(240, 240, 240, 0.6);
  transition: all 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.post-item:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.08);
}

/* 帖子元信息 */
.post-meta {
  align-items: flex-start;
}

.user-avatar {
  width: 44px;
  height: 44px;
  margin-right: 1rem;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  border: 2px solid white;
  transition: transform 0.2s ease;
}

.user-avatar:hover {
  transform: scale(1.08);
}

.post-title {
  font-size: 1.15rem;
  font-weight: 600;
  background: linear-gradient(90deg, #03090e, #05131e);
  -webkit-background-clip: text;
  background-clip: text;
  -webkit-text-fill-color: transparent;
  transition: opacity 0.2s;
}

.post-title:hover {
  opacity: 0.85;
}

/* 分类标签 */
.category-tag {
  border-radius: 6px;
  padding: 0.25rem 0.75rem;
  font-size: 0.85em;
  background: rgba(24, 144, 255, 0.08) !important;
  border: 1px solid rgba(24, 144, 255, 0.15) !important;
  color: #1890ff !important;
  margin-right: 0.5rem;
}

/* 统计信息 */
.post-stats {
  display: flex;
  gap: 1.5rem;
  min-width: 160px;
  justify-content: flex-end;
}

.stat-item {
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  padding: 0.5rem 1rem;
  background: rgba(241, 243, 245, 0.5);
  border-radius: 20px;
  font-size: 0.95em;
  color: #666;
  transition: background 0.2s;
}

.stat-item:hover {
  background: rgba(241, 243, 245, 0.8);
}

.stat-icon {
  font-size: 1.1em;
  color: #8c8c8c;
}

/* 时间戳 */
.timestamp {
  display: inline-flex;
  align-items: center;
  padding: 0.25rem 0.75rem;
  background: rgba(153, 153, 153, 0.08);
  border-radius: 12px;
  font-size: 0.85em;
  color: #666;
  margin-left: auto;
}

/* 分页 */
.pagination {
  margin-top: 2rem;
  padding: 1rem;
  background: rgba(255, 255, 255, 0.9);
  border-radius: 12px;
  display: flex;
  justify-content: center;
}

/* 基础图标容器 */
.like-icon {
  font-size: 1.25em;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  cursor: pointer;
  position: relative;
}

/* 点赞图标容器 */
.like-icon {
  font-size: 1.25em;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 未点赞状态 */
:deep(.anticon-heart-outlined) {
  color: #8c8c8c;
  stroke-width: 2px;
}

/* 已点赞状态 */
:deep(.anticon-heart-filled).liked {
  color: #e30a0b;
  animation: heartbeat 0.6s ease;
}

/* 悬停交互 */
.like-icon:hover {
  &:not(.liked) {
    color: #ff7875;
  }
  &.liked {
    filter: brightness(1.2);
  }
}

/* 点击涟漪效果 */
.like-icon::after {
  content: '';
  position: absolute;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: rgba(255, 77, 79, 0.1);
  transform: scale(0);
  transition: transform 0.3s;
  left: -8px;
  top: -8px;
}

.like-icon:active::after {
  transform: scale(1);
  opacity: 0;
}

/* 心跳动画 */
@keyframes heartbeat {
  0% { transform: scale(1); }
  25% { transform: scale(1.15); }
  50% { transform: scale(0.95); }
  75% { transform: scale(1.05); }
  100% { transform: scale(1); }
}

/* 数字指示器 */
.action-count {
  margin-left: 6px;
  font-size: 0.95em;
  color: #8c8c8c;
  transition: color 0.3s;
  &.liked {
    color: #ff4d4f;
    animation: countPop 0.4s ease;
  }
}

@keyframes countPop {
  0% { transform: scale(1); }
  50% { transform: scale(1.2); }
  100% { transform: scale(1); }
}

/* 响应式设计 */
@media (max-width: 768px) {
  .forum-container {
    padding: 1rem;
    margin: 1rem;
  }

  .action-bar {
    flex-direction: column;
    gap: 1rem;
    padding: 1rem;
  }

  .post-item {
    flex-direction: column;
    align-items: flex-start;
    padding: 1.25rem;
  }

  .post-stats {
    width: 100%;
    justify-content: space-between;
    margin-top: 1rem;
  }

  .meta-content {
    flex-direction: column;
    gap: 0.5rem;
  }

  .timestamp {
    margin-top: 0.5rem;
  }
}
</style>
