<template>
  <a-modal
    class="watermark-modal"
    v-model:visible="visible"
    title="添加水印"
    :footer="false"
    @cancel="closeModal"
  >
    <a-row gutter="16">
      <a-col span="12">
        <h4>原始图片</h4>
        <img :src="picture?.url" :alt="picture?.name" style="max-width: 100%" />
      </a-col>
      <a-col span="12">
        <h4>添加水印结果</h4>
        <img
          v-if="watermarkedImageUrl"
          :src="watermarkedImageUrl"
          :alt="picture?.name"
          style="max-width: 100%"
        />
      </a-col>
    </a-row>
    <div style="margin-bottom: 16px" />
    <!-- 输入水印内容 -->
    <a-input v-model:value="watermarkContent" placeholder="请输入水印内容" style="margin-bottom: 16px" />
    <a-flex justify="center" gap="16">
      <a-button type="primary" :loading="isGenerating" ghost @click="generateWatermarkedImage">生成水印图片</a-button>
      <a-button v-if="watermarkedImageUrl" type="primary" :loading="uploadLoading" @click="handleUpload">
        应用结果
      </a-button>
    </a-flex>
  </a-modal>
</template>

<script lang="ts" setup>
import { ref } from 'vue'
import { uploadPictureUsingPost } from '@/api/pictureController.ts'
import { message } from 'ant-design-vue'

interface Props {
  picture?: API.PictureVO
  spaceId?: number
  onSuccess?: (newPicture: API.PictureVO) => void
}

const props = defineProps<Props>()

// 是否可见
const visible = ref(false)

// 水印内容
const watermarkContent = ref('liuleliule')

// 添加水印后的图片 URL
const watermarkedImageUrl = ref<string>('')

// 是否正在生成水印图片
const isGenerating = ref(false)

// 是否正在上传
const uploadLoading = ref(false)

// 打开弹窗
const openModal = () => {
  visible.value = true
}

// 关闭弹窗
const closeModal = () => {
  visible.value = false
  watermarkContent.value = ''
  watermarkedImageUrl.value = ''
  isGenerating.value = false
  uploadLoading.value = false
}

// 暴露函数给父组件
defineExpose({
  openModal,
})

// 生成水印图片
const generateWatermarkedImage = async () => {
  const content = watermarkContent.value
  const height = 80 // 两个水印之间的垂直高度
  const width = 70 // 两个水印之间的水平高度
  const fontSize = 14 // 水印字体大小

  if (!props.picture?.url) {
    message.error('图片 URL 不存在')
    return
  }

  isGenerating.value = true

  const img = new Image()
  img.crossOrigin = 'anonymous' // 设置跨域属性
  img.src = props.picture.url

  console.log("imgok")
  img.onload = () => {
    const canvas = document.createElement('canvas')
    canvas.width = img.width
    canvas.height = img.height

    const ctx = canvas.getContext('2d')

    if (!ctx) {
      message.error('无法获取 canvas 2D 上下文')
      isGenerating.value = false
      return
    }

    ctx.drawImage(img, 0, 0, img.width, img.height)

    ctx.textAlign = 'left'
    ctx.textBaseline = 'top'
    ctx.font = `${fontSize}px Microsoft Yahei`
    ctx.fillStyle = 'rgba(255, 255, 255,0.5)'

    ctx.rotate(17 * Math.PI / 180)
    let i = 0
    let j = 0
    const waterMarkerWidth = content.split('').length * fontSize
    for (i = 0; i < img.width / waterMarkerWidth; i++) {
      for (j = 0; j < img.height / (height - 20); j++) {
        if (j === 0) {
          ctx.fillText(content, i * (waterMarkerWidth + width), -height, img.width)
        }
        ctx.fillText(content, i * (waterMarkerWidth + width), j * height, img.width)
      }
    }

    watermarkedImageUrl.value = canvas.toDataURL('image/png')
    console.log(watermarkedImageUrl.value)
    isGenerating.value = false
  }

  img.onerror = () => {
    message.error('图片加载失败，请检查图片 URL 或跨域设置。')
    isGenerating.value = false
  }
}

// 上传图片
const handleUpload = async () => {
  uploadLoading.value = true
  try {
    const response = await fetch(watermarkedImageUrl.value)
    const blob = await response.blob()
    const fileName = (props.picture?.name || 'image') + '_watermarked.png'
    const file = new File([blob], fileName, { type: blob.type })

    const params: API.PictureUploadRequest = {
      spaceId: props.spaceId,
    }

    if (props.picture) {
      params.id = props.picture.id
    }

    const res = await uploadPictureUsingPost(params, {}, file)
    if (res.data.code === 0 && res.data.data) {
      message.success('图片上传成功')
      // 将上传成功的图片信息传递给父组件
      props.onSuccess?.(res.data.data)
      // 关闭弹窗
      closeModal()
    } else {
      message.error('图片上传失败，' + res.data.msg)
    }
  } catch (error) {
    console.error('图片上传失败', error)
    message.error('图片上传失败，' + error.message)
  }
  uploadLoading.value = false
}
</script>

<style scoped>
.watermark-modal {
  text-align: center;
}
</style>
