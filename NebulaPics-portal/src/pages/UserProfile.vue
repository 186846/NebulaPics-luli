<template>
  <div class="user-profile-container">
    <!-- 个人信息展示 -->
    <a-card
      title="个人信息"
      style="width: 600px; margin: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
      class="user-info-card"
    >
      <a-list item-layout="horizontal">
        <a-list-item>
          <a-image
            :width="200"
            :src="user.userAvatar || defaultAvatar"
            @error="handleImageError"
          />
        </a-list-item>
        <a-list-item>
          <a-list-item-meta
            :description="'账号：' + user.userAccount"
            :title="user.userName"
          >
          </a-list-item-meta>
        </a-list-item>
        <a-list-item>
          <span class="user-role">用户角色：
            <div v-if="user.userRole === 'admin'">
              <a-tag color="green">管理员</a-tag>
            </div>
            <div v-else>
              <a-tag color="blue">普通用户</a-tag>
            </div>
          </span>
          <span class="register-time">注册时间：{{ formatDate(user.createTime) }}</span>
        </a-list-item>
      </a-list>
      <template #actions>
        <a-button type="primary" @click="showEditModal" class="edit-button">
          编辑信息
        </a-button>
      </template>
    </a-card>

    <a-card
      title="个人贡献"
      style="width: 600px; margin: 20px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);"
      class="blank-card"
    >
      <!-- 表格 -->
      <a-table
        :columns="tableColumns"
        :data-source="tableData"
        :pagination="false"
      />
      <a-divider style="height: 2px; background-color: #7cb305" />
      <!-- 用于绘制饼图的容器 -->
      <div ref="chartRef" style="width: 100%; height: 400px;"></div>

    </a-card>

    <!-- 编辑弹窗 -->
    <a-modal
      v-model:visible="visible"
      title="修改个人信息"
      @ok="handleSubmit"
      @cancel="handleCancel"
      class="edit-modal"
    >
      <a-form :model="editForm" layout="vertical">
        <a-form-item label="用户名" name="userName">
          <a-input v-model:value="editForm.userName" />
        </a-form-item>
        <a-form-item label="头像地址" name="userAvatar">
          <a-input
            v-model:value="editForm.userAvatar"
            placeholder="请输入图片URL"
          />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive, watchEffect } from 'vue';
import { message } from 'ant-design-vue';
import dayjs from 'dayjs';
import * as echarts from 'echarts';
import { getLoginUserUsingGet, updateUserUsingPost } from '@/api/userController.ts';
import { getPictureVoByUserIdUsingGet } from '@/api/pictureController.ts';

// 定义用户信息对象
const user = reactive({
  id: '',
  userAccount: '',
  userName: '',
  userAvatar: '',
  userRole: '',
  createTime: '',
});

// 响应式数据
const visible = ref(false);
// 使用 reactive 创建 editForm
const editForm = reactive({
  id: '',
  userName: '',
  userAvatar: '',
});
const defaultAvatar = ref('https://luli-1319967679.cos.ap-guangzhou.myqcloud.com//public/1874994224110247938/2025-01-23_51e0tau3yafrYgGU.png'); // 设置默认头像
const contributionData = ref([]);
const chartRef = ref(null);
const tableData = ref([]);
const tableColumns = ref([
  {
    title: '图库名称',
    dataIndex: 'galleryName',
    key: 'galleryName',
  },
  {
    title: '图片分类',
    dataIndex: 'category',
    key: 'category',
  },
  {
    title: '数量',
    dataIndex: 'count',
    key: 'count',
  },
]);

// 生命周期
onMounted(async () => {
  await fetchUserInfo();
});

// 获取用户信息
const fetchUserInfo = async () => {
  try {
    const res = await getLoginUserUsingGet();
    const res1 = await getPictureVoByUserIdUsingGet();
    console.log(res1.data);
    if (res.data.code === 0) {
      Object.assign(user, res.data.data);
    }
    if (res1.data.code === 0) {
      contributionData.value = res1.data.data;
      prepareTableData();
    }
  } catch (error) {
    message.error('获取用户信息失败');
  }
};

// 准备表格数据
const prepareTableData = () => {
  const newData = [];
  contributionData.value.forEach((gallery) => {
    gallery.categoryCounts.forEach((categoryCount) => {
      newData.push({
        galleryName: gallery.galleryName,
        category: categoryCount.category,
        count: categoryCount.count,
      });
    });
  });
  tableData.value = newData;
};

// 显示编辑弹窗
const showEditModal = () => {
  editForm.id = user.id;
  editForm.userName = user.userName;
  editForm.userAvatar = user.userAvatar || '';
  visible.value = true;
};

// 提交修改
const handleSubmit = async () => {
  try {
    const body = {
      id: editForm.id,
      userName: editForm.userName,
      userAvatar: editForm.userAvatar,
    };

    const res = await updateUserUsingPost(body);
    if (res.data.code === 0) {
      message.success('修改成功');
      await fetchUserInfo(); // 刷新用户信息
      visible.value = false;
    }
  } catch (error) {
    message.error('修改失败');
  }
};

// 取消修改
const handleCancel = () => {
  visible.value = false;
};

// 时间格式化
const formatDate = (dateString) => {
  return dayjs(dateString).format('YYYY-MM-DD HH:mm:ss');
};

// 处理图片加载失败
const handleImageError = (event) => {
  event.target.src = defaultAvatar.value;
};

// 绘制饼图
const drawPieChart = () => {
  if (!chartRef.value) return;
  const myChart = echarts.init(chartRef.value);

  // 处理数据，将所有图库的分类统计合并
  const allCategoryCounts = {};
  contributionData.value.forEach((gallery) => {
    gallery.categoryCounts.forEach((categoryCount) => {
      if (allCategoryCounts[categoryCount.category]) {
        allCategoryCounts[categoryCount.category] += categoryCount.count;
      } else {
        allCategoryCounts[categoryCount.category] = categoryCount.count;
      }
    });
  });

  const option = {
    title: {
      text: '个人贡献分类统计',
      left: 'center'
    },
    tooltip: {
      trigger: 'item'
    },
    series: [
      {
        name: '分类统计',
        type: 'pie',
        radius: '50%',
        data: Object.keys(allCategoryCounts).map((category) => ({
          value: allCategoryCounts[category],
          name: category
        }))
      }
    ]
  };

  myChart.setOption(option);
};

// 监听贡献数据变化，重新绘制图表
watchEffect(() => {
  drawPieChart();
  prepareTableData();
});
</script>

<style scoped>
/* 整体用户信息容器样式 */
.user-profile-container {
  padding: 20px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center; /* 水平居中卡片 */
  background-color: #f9f9f9; /* 背景色 */
}

/* 个人信息卡片和个人贡献卡片通用样式 */
.user-info-card,
.blank-card {
  border-radius: 8px;
  overflow: hidden;
  width: 600px;
  margin: 20px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  transition: box-shadow 0.3s ease; /* 添加过渡效果 */
  background-color: #fff; /* 卡片背景色 */
}

/* 鼠标悬停在卡片上时增加阴影效果 */
.user-info-card:hover,
.blank-card:hover {
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

/* 卡片标题样式 */
.user-info-card.ant-card-head,
.blank-card.ant-card-head {
  background-color: #fafafa;
  border-bottom: 1px solid #e8e8e8;
  padding: 12px 24px;
}

/* 卡片标题文字样式 */
.user-info-card.ant-card-head-title,
.blank-card.ant-card-head-title {
  font-size: 18px;
  font-weight: 600;
}

/* 用户角色和注册时间文字样式 */
.user-role,
.register-time {
  display: inline-block;
  margin-right: 20px;
  font-size: 14px;
  color: #666;
}

/* 编辑按钮样式 */
.edit-button {
  transition: background-color 0.3s ease;
}

/* 鼠标悬停在编辑按钮上时的样式 */
.edit-button:hover {
  background-color: #007bff;
  border-color: #007bff;
  color: #fff;
}

/* 编辑弹窗内容样式 */
.edit-modal.ant-modal-content {
  border-radius: 8px;
}

/* 编辑弹窗头部样式 */
.edit-modal.ant-modal-header {
  border-bottom: 1px solid #eee;
  padding: 16px 24px;
  background-color: #fafafa;
}

/* 编辑弹窗标题样式 */
.edit-modal.ant-modal-title {
  font-size: 18px;
  font-weight: 600;
}

/* 编辑弹窗主体内容样式 */
.edit-modal.ant-modal-body {
  padding: 24px;
}

/* 编辑弹窗底部样式 */
.edit-modal.ant-modal-footer {
  border-top: 1px solid #eee;
  padding: 10px 24px;
  text-align: right;
}

/* 用户头像样式 */
.user-avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
}

/* 个人贡献卡片列表头部样式 */
.blank-card.ant-list-header {
  padding: 12px 24px;
  background-color: #fafafa;
  border-bottom: 1px solid #e8e8e8;
  font-weight: 600;
}

/* 个人贡献卡片列表项样式 */
.blank-card.ant-list-item {
  padding: 12px 24px;
  border-bottom: 1px solid #e8e8e8;
}

/* 个人贡献卡片列表项最后一项去掉底部边框 */
.blank-card.ant-list-item:last-child {
  border-bottom: none;
}

/* 图库名称样式 */
.blank-card.ant-list-item div:first-child {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
}

/* 分类统计信息样式 */
.blank-card.ant-list-item.ant-list.ant-list-item {
  padding: 6px 0;
  font-size: 14px;
  color: #666;
}

/* 表格样式 */
.blank-card.ant-table {
  margin-top: 10px;
}

/* 个人贡献卡片滚动样式 */
.blank-card {
  max-height: 600px; /* 设置最大高度，可根据需求调整 */
  overflow-y: scroll;
}
</style>
