<template>
  <div class="picture-upload">
    <a-upload
      list-type="picture-card"
      :show-upload-list="false"
      :custom-request="handleUpload"
      :before-upload="beforeUpload"
    >
      <img v-if="picture?.url" :src="picture?.url" alt="avatar" />
      <div v-else>
        <loading-outlined v-if="loading"></loading-outlined>
        <plus-outlined v-else></plus-outlined>
        <div class="ant-upload-text">点击或拖拽上传图片</div>
      </div>
    </a-upload>
  </div>
</template>
<script lang="ts" setup>
import { ref } from 'vue'
import { LoadingOutlined, PlusOutlined } from '@ant-design/icons-vue'
import { message } from 'ant-design-vue'
import { uploadPictureUsingPost } from '@/api/pictureController.ts'
import type { AxiosResponse } from 'axios'
interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}
const props = defineProps<Props>()
/**
 * 上传图片
 * @param file
 */
interface UploadResponse {
  code: number;
  data: any;
  msg: string;
}

const adaptAxiosResponse = (response: AxiosResponse): UploadResponse => {
  const { data } = response;
  return {
    code: data.code,
    data: data.data,
    msg: data.msg
  };
};

const handleUpload = async ({ file }: any) => {
  loading.value = true;
  try {
    const params: API.PictureUploadRequest = props.picture ? { id: props.picture.id } : {}
    params.spaceId = props.spaceId;
    const res = await uploadPictureUsingPost(params, {}, file);
    const adaptedRes = adaptAxiosResponse(res);
    if (adaptedRes.code === 0 && adaptedRes.data) {
      message.success('图片上传成功');
      props.onSuccess?.(adaptedRes.data);
    } else {
      message.error('图片上传失败，' + adaptedRes.msg);
    }
  } catch (error) {
    console.error('图片上传失败', error);
    message.error('图片上传失败，' + (error as Error).message);
  }
  loading.value = false;
}

const loading = ref<boolean>(false)
/**
 * 上传前的校验
 * @param file
 */
interface UploadFile {
  type: string;
  size: number;
}
const beforeUpload = (file: UploadFile) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  if (!isJpgOrPng) {
    message.error('不支持上传该格式的图片，推荐 jpg 或 png');
  }
  const isLt2M = file.size / 1024 / 1024 < 2;
  if (!isLt2M) {
    message.error('不能上传超过 2M 的图片');
  }
  return isJpgOrPng && isLt2M;
}

</script>
<style scoped>
.picture-upload :deep(.ant-upload) {
  width: 100% !important;
  height: 100% !important;
  min-width: 152px;
  min-height: 152px;
}
.picture-upload img {
  max-width: 100%;
  max-height: 480px;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}
.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
