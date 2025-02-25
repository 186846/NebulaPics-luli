<template>
  <div>
    <h1>增加话题</h1>
    <el-form :model="topicForm" ref="formRef" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="topicForm.title" placeholder="请输入话题标题"></el-input>
      </el-form-item>
      <el-form-item label="图片">
        <el-upload
          :show-file-list="false"
          :before-upload="beforeUpload"
          accept="image/*"
        >
          <el-button type="primary">选择图片</el-button>
        </el-upload>
        <div v-if="list.length > 0" class="uploaded-images">
          <span
            v-for="(fileName, index) in list"
            :key="index"
            class="uploaded-image"
          >
            {{ fileName }}
            <el-icon @click="removeImage(index)" class="delete-icon">
              <Close />
            </el-icon>
          </span>
        </div>
      </el-form-item>
      <el-form-item label="分类（标签）">
        <el-input
          v-model="newCategoryInput"
          placeholder="输入分类（标签），用逗号分隔，按回车添加"
          @keyup.enter="addCategory"
        ></el-input>
        <div v-if="topicForm.category.length > 0" class="tag-list">
          <span
            v-for="(category, index) in topicForm.category"
            :key="index"
            class="tag-item"
          >
            {{ category }}
            <el-icon @click="removeCategory(index)" class="delete-icon">
              <Close />
            </el-icon>
          </span>
        </div>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="submitForm">提交</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import axios from 'axios';
import { ElMessage } from 'element-plus';
import { testUploadFileUsingPost } from '@/api/fileController.ts';
import { Close } from '@element-plus/icons-vue';
import { addPostUsingPost } from '@/api/postController.ts'

// 定义表单数据
const topicForm = ref({
  title: '',
  content: [] as string[],
  category: [] as string[], // category 现在是一个数组
});

// 表单引用
const formRef = ref(null);
// 图片文件名列表
const list = ref<string[]>([]);
// 临时存储用户输入的分类（标签）字符串
const newCategoryInput = ref('');

// 上传前处理
const beforeUpload = async (file: File) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    ElMessage.error('只能上传 JPG/PNG 格式的图片！');
    return false;
  }
  try {
    const res = await testUploadFileUsingPost({}, file);
    console.log(res);
    if (res.data.code === 0) {
      list.value.push(file.name);
      topicForm.value.content.push(res.data.data);
      console.log(topicForm.value.content);
      console.log(list);
      ElMessage.success('图片上传成功！');
    } else {
      ElMessage.error('图片上传失败！');
    }
  } catch (error) {
    console.error('图片上传出错：', error);
    ElMessage.error('图片上传失败，请稍后重试！');
  }
  return false; // 阻止 el-upload 默认的上传行为
};

// 移除图片
const removeImage = (index: number) => {
  topicForm.value.content.splice(index, 1);
  list.value.splice(index, 1);
  console.log(topicForm.value.content);
  console.log(list);
};

// 添加多个分类（标签）
const addCategory = () => {
  if (newCategoryInput.value.trim()) {
    const newCategories = newCategoryInput.value.split(',').map(category => category.trim());
    // 过滤掉空分类（标签）
    const validCategories = newCategories.filter(category => category.length > 0);
    topicForm.value.category.push(...validCategories);
    console.log(topicForm.value.category);
    newCategoryInput.value = ''; // 清空输入框
  }
};

// 移除分类（标签）
const removeCategory = (index: number) => {
  topicForm.value.category.splice(index, 1);
  console.log(topicForm.value.category);
};

// 提交表单
const submitForm = async () => {
  if (
    !topicForm.value.title ||
    topicForm.value.content.length === 0 ||
    topicForm.value.category.length === 0
  ) {
    ElMessage.error('请填写完整信息！');
    return;
  }
  try {
    // 这里模拟将话题数据提交到后端，你需要根据实际情况修改 URL
    const response = await addPostUsingPost(topicForm.value);
    if (response.data.code === 0) {
      ElMessage.success('话题添加成功！');
      // 清空表单
      topicForm.value = {
        title: '',
        content: [],
        category: [],
      };
      list.value = []; // 同时清空图片列表
      newCategoryInput.value = ''; // 清空分类（标签）输入框
    } else {
      ElMessage.error('话题添加失败，请稍后重试！');
    }
  } catch (error) {
    console.error('提交话题时出错：', error);
    ElMessage.error('话题添加失败，请稍后重试！');
  }
};
</script>

<style scoped>
/* 基础容器优化 */
.el-form {
  max-width: 800px;
  margin: 20px auto;
  padding: 30px;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 6px 18px rgba(0, 0, 0, 0.06);
}

/* 表单项增强 */
.el-form-item {
  margin-bottom: 28px;
  transition: all 0.3s ease;
}

.el-form-item:hover {
  transform: translateX(5px);
}

/* 图片上传区域优化 */
:deep(.el-upload) {
  width: 100%;
}

.uploaded-images {
  display: flex;
  flex-wrap: wrap;
  gap: 12px;
  margin-top: 15px;
}

.uploaded-image {
  position: relative;
  background: #f8f9fa;
  border: 1px solid #e9ecef;
  border-radius: 8px;
  padding: 8px 32px 8px 12px;
  font-size: 13px;
  color: #495057;
  transition: all 0.2s;
  cursor: pointer;
}

.uploaded-image:hover {
  background: #e7f5ff;
  border-color: #4dabf7;
  transform: translateY(-2px);
}

.delete-icon {
  position: absolute;
  right: 8px;
  top: 50%;
  transform: translateY(-50%);
  padding: 2px;
  background: rgba(255, 255, 255, 0.8);
  border-radius: 50%;
}

/* 标签样式升级 */
.tag-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 12px;
}

.tag-item {
  position: relative;
  background: #e7f5ff;
  color: #228be6;
  border-radius: 20px;
  padding: 6px 28px 6px 12px;
  font-size: 13px;
  font-weight: 500;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  transition: all 0.2s;
}

.tag-item:hover {
  background: #d0ebff;
  transform: scale(1.02);
}

/* 按钮布局优化 */
.el-form-item:last-child {
  margin-top: 32px;
  text-align: center;
}

.el-button--primary {
  width: 180px;
  height: 42px;
  border-radius: 8px;
  font-size: 15px;
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 输入框动效 */
.el-input {
  transition: box-shadow 0.3s ease;
}

.el-input:hover {
  box-shadow: 0 2px 8px rgba(66, 153, 225, 0.12);
}

/* 响应式处理 */
@media (max-width: 768px) {
  .el-form {
    padding: 20px;
    margin: 15px;
  }

  .el-form-item__label {
    font-size: 14px;
  }
}
</style>
