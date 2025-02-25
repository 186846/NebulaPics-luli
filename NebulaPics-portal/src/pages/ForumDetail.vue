<template>
  <div class="detail-container">
    <!-- 加载状态提示 -->
    <div v-if="isLoading" class="loading">加载中...</div>
    <!-- 错误状态提示 -->
    <div v-if="errorMessage" class="error">{{ errorMessage }}</div>
    <!-- 帖子详情 -->
    <a-card v-if="post.id" :bordered="false" class="post-detail">
      <div class="post-header">
        <h1 class="title">{{ post.title }}</h1>
        <div class="meta">
          <!-- 遍历显示多个分类标签 -->
          <a-tag v-for="cat in post.category" :key="cat" color="blue">{{ cat }}</a-tag>
          <span class="timestamp">{{ formatTime(post.createTime) }}</span>
        </div>
      </div>

      <!-- 图片展示区域 -->
      <div class="post-content">
        <div class="image-grid">
          <!-- 新增图片容器 -->
          <div
            v-for="(imageUrl, index) in post.content"
            :key="index"
            class="image-container"
          >
            <a-image
              :src="imageUrl"
              alt="帖子图片"
              :preview="true"
              class="post-image"
            />
          </div>
        </div>
      </div>

      <!-- 用户信息和统计 -->
      <div class="post-footer">
        <div class="user-info">
          <a-avatar v-if="post.user.userAvatar" :src="post.user.userAvatar"></a-avatar>
          <a-avatar v-else :style="{ backgroundColor: stringToColor(post.user.userName) }">
            {{ post.user.userName[0] }}
          </a-avatar>
          <div class="username">{{ post.user.userName }}</div>
        </div>

        <div class="stats">
          <span class="views">
            <EyeOutlined />
            {{ post.viewCount }} 浏览
          </span>
          <span class="replies">
            <MessageOutlined />
            {{ post.replyCount }} 回复
          </span>
        </div>
      </div>
    </a-card>

    <!-- 评论区域 -->
    <div v-if="post.id" class="comments-section">
      <h2>评论</h2>
        <a-list item-layout="vertical" :data-source="comments">
          <template #renderItem="{ item: comment }">
            <a-comment>
              <!-- 动作区域 -->
              <template #actions>
                <span key="comment-like">
                  <a-tooltip title="点赞">
                    <template v-if="comment.action === 'liked'">
                      <LikeFilled @click="handleLike(comment)" />
                    </template>
                    <template v-else>
                      <LikeOutlined @click="handleLike(comment)" />
                    </template>
                  </a-tooltip>
                  <span class="action-count">{{ comment.likeCount || 0 }}</span>
                </span>
                <span key="comment-dislike">
                  <a-tooltip title="点踩">
                    <template v-if="comment.action === 'disliked'">
                      <DislikeFilled @click="handleDislike(comment)" />
                    </template>
                    <template v-else>
                      <DislikeOutlined @click="handleDislike(comment)" />
                    </template>
                  </a-tooltip>
                  <span class="action-count">{{ comment.dislikeCount || 0 }}</span>
                </span>
                <span key="comment-reply" @click="handleReply(comment)">回复</span>
                <a-dropdown>
                  <a-space>
                    <EllipsisOutlined />
                  </a-space>
                  <template #overlay>
                    <a-menu>
                      <a-menu-item @click="handleDeleteComment(comment)">
                        删除
                      </a-menu-item>
                    </a-menu>
                  </template>
                </a-dropdown>
              </template>
              <!-- 用户信息 -->
              <template #author>
                <a>{{ comment.user.userName }}</a>
              </template>

              <!-- 头像 -->
              <template #avatar>
                <a-avatar
                  v-if="comment.user.userAvatar"
                  :src="comment.user.userAvatar"
                  :alt="comment.user.userName"
                />
                <a-avatar
                  v-else
                  :style="{ backgroundColor: stringToColor(comment.user.userName) }"
                >
                  {{ comment.user.userName[0] }}
                </a-avatar>
              </template>

              <!-- 评论内容 -->
              <template #content>
                <div class="comment-content">
                  <a-typography-text>{{ comment.content }}</a-typography-text>
                  <div v-if="comment.replyTo" class="reply-reference">
                    回复 <a>@{{ comment.replyTo.userName }}</a>
                  </div>
                </div>
              </template>

              <!-- 新增回复评论列表 -->
              <div v-if="comment.replyCommentVoList?.length"  class="reply-comments">
                <a-comment
                  v-for="reply in comment.replyCommentVoList"
                  :key="reply.id"
                  class="nested-reply"
                >
                  <template #avatar>
                    <a-avatar :src="reply.user.userAvatar"  :alt="reply.user.userName"  />
                  </template>

                  <template #author>
                    <a>{{ reply.user.userName  }}</a>
                    <span v-if="reply.receiveUser"  class="reply-target">回复 <a>@{{ reply.receiveUser.userName  }}</a></span>
                  </template>
                  <span key="comment-like">
                      <a-tooltip title="点赞">
                        <template v-if="reply.action === 'liked'">
                          <LikeFilled @click="handleLike1(reply)" />
                        </template>
                        <template v-else>
                          <LikeOutlined @click="handleLike1(reply)" />
                        </template>
                      </a-tooltip>
                      <span class="action-count">{{ reply.likeCount || 0 }}</span>
                      </span>
                  <span key="comment-dislike">
                        <a-tooltip title="点踩">
                          <template v-if="reply.action === 'disliked'">
                            <DislikeFilled @click="handleDislike1(reply)" />
                          </template>
                          <template v-else>
                            <DislikeOutlined @click="handleDislike1(reply)" />
                          </template>
                        </a-tooltip>
                        <span class="action-count">{{ reply.dislikeCount || 0 }}</span>
                      </span>
                    <a-dropdown>
                      <a-space>
                        <EllipsisOutlined />
                      </a-space>
                      <template #overlay>
                        <a-menu>
                          <a-menu-item @click="handleDeleteComment1(reply)">
                            删除
                          </a-menu-item>
                        </a-menu>
                      </template>
                    </a-dropdown>
                  <template #content>
                    <div class="reply-content">{{ reply.content  }}
                      <div class="reply-actions">
                            <span @click="handleReplyComment(comment,reply)">
                              <MessageOutlined /> 回复
                            </span>
                      </div>
                    </div>
                  </template>
                  <template #datetime>
                    <a-tooltip :title="dayjs(reply.createTime).format('YYYY-MM-DD HH:mm')">
                      <span>{{ formatTime(reply.createTime) }}</span>
                    </a-tooltip>
                  </template>
                </a-comment>
              </div>
              <!-- 时间显示 -->
              <template #datetime>
                <a-tooltip :title="dayjs(comment.createTime).format('YYYY-MM-DD HH:mm')">
                  <span>{{ formatTime(comment.createTime) }}</span>
                </a-tooltip>
              </template>

            </a-comment>
          </template>
        </a-list>
      <!-- 分页组件 -->
      <a-pagination
        class="comment-pagination"
        v-model:current="commentParams.current"
        v-model:pageSize="commentParams.pageSize"
        :total="totalComments"
        @change="handleCommentPageChange"
        show-less-items
      />

      <!-- 评论输入框 -->
      <div class="comment-input">
        <a-textarea v-model:value="newComment" placeholder="发表评论" rows="4"></a-textarea>
        <a-button type="primary" @click="addComment" :disabled="isSending">发表评论</a-button>
      </div>
    </div>

    <!-- 回复评论的弹窗 -->
    <a-modal
      :visible="replyModalVisible"
      title="回复评论"
      ok-text="提交"
      cancel-text="取消"
      @ok="handleReplySubmit"
      @cancel="handleReplyCancel">
      <a-textarea v-model:value="replyContent" placeholder="输入回复内容" rows="4"></a-textarea>
    </a-modal>

    <!-- 评论区交流对话框 -->
    <a-modal
      :visible="replyCommentModalVisible"
      title="回复评论"
      ok-text="提交"
      cancel-text="取消"
      @ok="handleReplySubmit1"
      @cancel="handleReplyCancel">
      <a-textarea v-model:value="replyContent" placeholder="输入回复内容" rows="4"></a-textarea>
    </a-modal>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRoute } from 'vue-router';
import { EyeOutlined,
  MessageOutlined,
  LikeFilled,
  LikeOutlined,
  DislikeFilled,
  DislikeOutlined,
  EllipsisOutlined
} from '@ant-design/icons-vue';
import { getPostVoByIdUsingGet } from '@/api/postController.ts';
import {
  addReplyUsingPost,
  deleteReplyUsingPost,
  listReplyVoByPageUsingPost,
  updateReplyUsingPost
} from '@/api/replyController.ts'
import { message } from 'ant-design-vue';

import dayjs from 'dayjs';
import relativeTime from 'dayjs/plugin/relativeTime';
import {
  addReplyCommentUsingPost,
  addReplyToCommentUsingPost, deleteReplyCommentUsingPost,
  updateReplyCommentUsingPost
} from '@/api/replyCommentController.ts'
dayjs.extend(relativeTime);

const route = useRoute();
const postId = ref(null);
const post = ref({});
const isLoading = ref(true);
const errorMessage = ref('');
const comments = ref([]);
const newComment = ref('');
// 新增：控制发送按钮禁用状态的响应式变量
const isSending = ref(false);

// 评论分页参数
const commentParams = ref({
  current: 1,
  pageSize: 5,
});
const totalComments = ref(0);

// 新增：回复弹窗相关变量
const replyModalVisible = ref(false);
const replyCommentModalVisible = ref(false);
const replyContent = ref('');
const replyToComment = ref(null);
const  replyCommentToComment = ref(null);

// 初始化帖子 ID
const initPostId = () => {
  const id = route.params.id;
  if (!id) {
    errorMessage.value = '无效的帖子 ID';
    isLoading.value = false;
    return;
  }
  postId.value = id;
};

// 删除评论
const handleDeleteComment = async (comment) => {
  const res = await deleteReplyUsingPost(comment)
  if (res.data.code === 0){
    message.success("删除成功")
    await fetchComments();
  }else {
    message.error("删除失败");
  }
}

//删除评论讨论
const handleDeleteComment1 = async (comment) => {
  const res = await deleteReplyCommentUsingPost(comment)
  if (res.data.code === 0){
    message.success("删除成功")
    await fetchComments();
  }else {
    message.error("删除失败");
  }
}

// 获取帖子详情
const fetchPostsDetail = async () => {
  try {
    if (postId.value!== null) {
      const res = await getPostVoByIdUsingGet({
        id: postId.value,
      });
      if (res.data.code === 0 && res.data.data) {
        post.value = res.data.data;
        // 获取帖子成功后加载评论
        await fetchComments();
      } else {
        errorMessage.value = '帖子未找到';
      }
    }
  } catch (error) {
    errorMessage.value = '加载帖子详情时出现错误';
  } finally {
    isLoading.value = false;
  }
};

// 获取评论列表
const fetchComments = async () => {
  try {
    const res = await listReplyVoByPageUsingPost({
      postId: postId.value,
      current: commentParams.value.current,
      pageSize: commentParams.value.pageSize,
    });
    if (res.data.code === 0) {
      comments.value = res.data.data.records;
      totalComments.value = res.data.data.total;
    }
  } catch (error) {
    message.error('加载评论失败');
  }
};

// 分页改变处理
const handleCommentPageChange = (page, pageSize) => {
  commentParams.value.current = page;
  commentParams.value.pageSize = pageSize;
  fetchComments();
};

// 添加评论
const addComment = async () => {
  if (newComment.value.trim() === '') {
    message.warning('评论内容不能为空');
    return;
  }
  // 发送评论前设置为true，禁用按钮
  isSending.value = true;
  const Reply = {
    postId: postId.value,
    content: newComment.value,
  };
  try {
    const res = await addReplyUsingPost(Reply);
    if (res.data.code === 0) {
      message.success('评论成功');
      newComment.value = '';
      // 添加成功后重新加载第一页
      commentParams.value.current = 1;
      await fetchComments();
    }
  } catch (error) {
    message.error('评论失败');
  } finally {
    // 无论成功失败，都将isSending设置为false，允许再次点击
    isSending.value = false;
  }
};

// 根据帖子 ID 加载数据
onMounted(() => {
  initPostId();
  if (!errorMessage.value) {
    fetchPostsDetail();
  }
});

// 工具函数：根据字符串生成颜色
const stringToColor = (str) => {
  let hash = 0;
  for (let i = 0; i < str.length; i++) {
    hash = str.charCodeAt(i) + ((hash << 5) - hash);
  }
  return `hsl(${hash % 360}, 50%, 80%)`;
};

// 修改时间格式化方法
const formatTime = (dateTime) => {
  return dayjs(dateTime).fromNow();
};

// 新增交互方法
const handleLike = async (comment) => {
  // 实现点赞逻辑
  if (comment.action === 'liked') {
    comment.likeCount--;
    comment.action = null;
  } else {
    if (comment.action === 'disliked') comment.dislikeCount--;
    comment.likeCount++;
    comment.action = 'liked';
  }
  await updateReplyUsingPost(comment);
};

const handleDislike = async (comment) => {
  // 实现点踩逻辑
  if (comment.action === 'disliked') {
    comment.dislikeCount--;
    comment.action = null;
  } else {
    if (comment.action === 'liked') comment.likeCount--;
    comment.dislikeCount++;
    comment.action = 'disliked';
  }
  await updateReplyUsingPost(comment);
};

// 新增交互方法
const handleLike1 = async (replyComment) => {
  // 实现点赞逻辑
  if (replyComment.action === 'liked') {
    replyComment.likeCount--;
    replyComment.action = null;
  } else {
    if (replyComment.action === 'disliked') replyComment.dislikeCount--;
    replyComment.likeCount++;
    replyComment.action = 'liked';
  }
  await updateReplyCommentUsingPost(replyComment);
};

const handleDislike1 = async (replyComment) => {
  // 实现点踩逻辑
  if (replyComment.action === 'disliked') {
    replyComment.dislikeCount--;
    replyComment.action = null;
  } else {
    if (replyComment.action === 'liked') replyComment.likeCount--;
    replyComment.dislikeCount++;
    replyComment.action = 'disliked';
  }
  await updateReplyCommentUsingPost(replyComment);
};

const handleReply = (comment) => {
  // 实现回复定位逻辑
  replyToComment.value = comment;
  replyModalVisible.value = true;
};

const handleReplyComment = (comment,replyComment) => {
  replyToComment.value = comment;
  replyCommentToComment.value = replyComment;
  replyCommentModalVisible.value = true;
}

// 处理回复提交
const handleReplySubmit = async () => {
  if (replyContent.value.trim() === '') {
    message.warning('回复内容不能为空');
    return;
  }
  console.log(replyToComment.value.id);
  isSending.value = true;
  const replyTo = {
    replyId: replyToComment.value.id,
    content: replyContent.value,
  };
  try {
    const res = await addReplyCommentUsingPost(replyTo);
    if (res.data.code === 0) {
      message.success('回复成功');
      replyContent.value = '';
      replyModalVisible.value = false;
      await fetchComments();
    }
  } catch (error) {
    message.error('回复失败');
  } finally {
    isSending.value = false;
  }
};

const handleReplySubmit1 = async () => {
  if (replyContent.value.trim() === '') {
    message.warning('回复内容不能为空');
    return;
  }
  console.log(replyToComment.value.id);
  isSending.value = true;
  const replyCommentTo = {
    replyId: replyToComment.value.id,
    receiveUserId: replyCommentToComment.value.user.id,
    content: replyContent.value,
  };
  console.log(replyCommentTo)
  try {
    const res = await addReplyToCommentUsingPost(replyCommentTo);
    if (res.data.code === 0) {
      message.success('回复成功');
      replyContent.value = '';
      replyCommentModalVisible.value = false;
      await fetchComments();
    }
  } catch (error) {
    message.error('回复失败');
  } finally {
    isSending.value = false;
  }
};

// 处理回复取消
const handleReplyCancel = () => {
  replyContent.value = '';
  replyModalVisible.value = false;
  replyCommentModalVisible.value = false;
};
</script>


<style scoped>
/* 新增嵌套回复样式 */
.reply-comments {
  margin-top: 16px;
  border-left: 3px solid #f0f2f5;
  padding-left: 24px;
}

.nested-reply {
  background: #f8f9fa;
  border-radius: 8px;
  margin: 8px 0;
  padding: 12px;

  :deep(.ant-comment-content) {
    padding: 0;
  }
}

.reply-target {
  color: #8c8c8c;
  margin-left: 8px;
  font-size: 0.9em;

  a {
    color: #1890ff;
  }
}

.reply-actions {
  margin-top: 8px;

  span {
    cursor: pointer;
    color: #1890ff;
    transition: opacity 0.3s;

    &:hover {
      opacity: 0.8;
    }

    .anticon {
      margin-right: 4px;
    }
  }
}

/* 调整原有评论间距 */
.comment-content {
  & > .ant-typography {
    margin-bottom: 8px;
  }
}

.detail-container {
  max-width: 1200px;
  margin: 20px auto;
  padding: 30px;
  background: #f8f9fa;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
}

.post-detail {
  margin-bottom: 30px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  background: white;
}

.post-header {
  padding: 24px;
  background: linear-gradient(135deg, #f8f9fa 0%, #e9ecef 100%);
  border-bottom: 1px solid #e9ecef;
}

.title {
  font-size: 2.2rem;
  color: #2c3e50;
  margin-bottom: 12px;
  font-weight: 600;
  letter-spacing: -0.5px;
}

.meta {
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}

.timestamp {
  color: #6c757d;
  font-size: 0.9rem;
  margin-left: auto;
}

.post-content {
  padding: 30px;
  position: relative;
  min-height: 300px;
}

.image-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(240px, 1fr));
  gap: 8px; /* 间距减少33% */
  grid-auto-rows: 1fr; /* 行高自动填充 */
  margin: 12px 0; /* 减少外边距 */
}

.image-container {
  width: 100%;
  height: 0;
  padding-bottom: 80%; /* 容器比例调整 */
  position: relative;
  border-radius: 6px;
  overflow: hidden;
  border: 1px solid #f0f0f0; /* 添加浅色边框 */
  background: #fff; /* 填充空白背景 */
}

.post-image {
  position: absolute;
  top: 50%;
  left: 50%;
  width: 100%;
  height: 100%;
  transform: translate(-50%, -50%);
  object-fit: contain; /* 完整显示图片 */
  transition: transform 0.25s cubic-bezier(0.4, 0, 0.2, 1);
}

.post-image:hover {
  transform: scale(1.02);
}

.post-footer {
  padding: 20px 24px;
  background: #f8f9fa;
  border-top: 1px solid #e9ecef;
  display: flex;
  flex-wrap: wrap;
  gap: 15px;
  align-items: center;
  position: relative;
  z-index: 1;
  margin-top: 20px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 8px 16px;
  background: white;
  border-radius: 24px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
}

.username {
  font-weight: 500;
  color: #2c3e50;
}

.stats {
  display: flex;
  gap: 24px;
  margin-left: auto;
}

.stats span {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #6c757d;
  font-size: 0.95rem;
}

.comments-section {
  margin-top: 30px;
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.05);
}

.comments-section h2 {
  font-size: 1.5rem;
  color: #2c3e50;
  margin-bottom: 20px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f1f3f5;
}

/* 新增样式调整 */
.comments-section {
  :deep(.ant-comment) {
    padding: 16px;
    transition: background 0.3s;

    &:hover {
      background: #f8f9fa;
    }
  }

  .action-count {
    display: inline-block;
    min-width: 24px;
    text-align: center;
    padding-left: 4px;
    color: #8c8c8c;
  }

  .reply-reference {
    margin-top: 8px;
    padding: 4px 8px;
    background: #f0f2f5;
    border-radius: 4px;

    a {
      color: #1890ff;
    }
  }

  /* 保持原有分页样式... */
}

.comment-item {
  display: flex;
  gap: 16px;
  padding: 18px;
  margin-bottom: 16px;
  background: #f8f9fa;
  border-radius: 8px;
  transition: transform 0.2s ease;
}

.comment-item:hover {
  transform: translateX(5px);
}

.comment-content {
  flex: 1;
}

.comment-username {
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 4px;
}

.comment-text {
  color: #495057;
  line-height: 1.6;
  margin-bottom: 6px;
}

.comment-timestamp {
  color: #868e96;
  font-size: 0.85rem;
}

.comment-pagination {
  margin: 24px 0;
  display: flex;
  justify-content: flex-end;
}

.comment-input {
  margin-top: 24px;
  position: relative;
}

.comment-input.ant-btn {
  margin-top: 12px;
  padding: 8px 24px;
  height: auto;
  border-radius: 6px;
  font-weight: 500;
  transition: all 0.3s ease;
}

.comment-input.ant-btn:hover {
  transform: translateY(-1px);
}

.loading,
.error {
  text-align: center;
  padding: 40px;
  font-size: 1.2rem;
  border-radius: 12px;
  margin: 20px 0;
}

.loading {
  background: rgba(255, 255, 255, 0.9);
  color: #4a90e2;
}

.error {
  background: #fff5f5;
  color: #ff6b6b;
  border: 1px solid #ffd8d8;
}

/* 其他关联调整 */
.post-content {
  padding: 20px; /* 减少容器内边距 */
}

@media (max-width: 768px) {
  .detail-container {
    padding: 15px;
    margin: 10px;
  }

  .title {
    font-size: 1.8rem;
  }

  .image-grid {
    grid-template-columns: repeat(2, 1fr); /* 强制双列布局 */
    gap: 4px;
  }

  .image-container {
    padding-bottom: 100%; /* 保持正方形比例 */
    border-width: 0.5px;
  }

  .post-content {
    padding: 16px; /* 移动端内边距减少 */
  }

  .post-footer {
    flex-direction: column;
    align-items: flex-start;
  }

  .stats {
    margin-left: 0;
    width: 100%;
    justify-content: space-between;
  }
}
</style>
