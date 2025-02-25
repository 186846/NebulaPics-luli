<template>
  <div id="spaceAnalyzePage">
    <h2>
      空间图库分析 -
      <span v-if="queryAll">全部空间</span>
      <span v-else-if="queryPublic">公共图库</span>
      <span v-else>
        <a :href="`/space/${spaceId}`" target="_blank">空间 id：{{ spaceId }}</a>
      </span>
    </h2>
    <div style="margin-bottom: 16px" />
    <a-row :gutter="[16, 16]">
      <!-- 空间使用分析 -->
      <a-col :xs="24" :md="12">
        <SpaceUsageAnalyze :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 空间分类分析 -->
      <a-col :xs="24" :md="12">
        <SpaceCategoryAnalyze :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 标签分析 -->
      <a-col :xs="24" :md="12">
        <SpaceTagAnalyze :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 图片大小分段分析 -->
      <a-col :xs="24" :md="12">
        <SpaceSizeAnalyze :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 用户上传行为分析 -->
      <a-col :xs="24" :md="12">
        <SpaceUserAnalyze :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
      <!-- 空间使用排行分析 -->
      <a-col :xs="24" :md="12">
        <SpaceRankAnalyze v-if="isAdmin" :spaceId="spaceId" :queryAll="queryAll" :queryPublic="queryPublic" />
      </a-col>
    </a-row>
  </div>
</template>
<script setup lang="ts">

import { useRoute } from 'vue-router'
import { computed } from 'vue'
import { useLoginUserStore } from '@/stores/useLoginUserStore.ts'
import SpaceUsageAnalyze from '@/components/SpaceUsageAnalyze.vue'
import SpaceCategoryAnalyze from '@/components/SpaceCategoryAnalyze.vue'
import SpaceTagAnalyze from '@/components/SpaceTagAnalyze.vue'
import SpaceUserAnalyze from '@/components/SpaceUserAnalyze.vue'
import SpaceRankAnalyze from '@/components/SpaceRankAnalyze.vue'
import SpaceSizeAnalyze from '@/components/SpaceSizeAnalyze.vue'
const route = useRoute()
// 空间 id
const spaceId = computed(() => {
  return route.query?.spaceId as string
})
// 是否查询所有空间
const queryAll = computed(() => {
  return !!route.query?.queryAll
})
// 是否查询公共空间
const queryPublic = computed(() => {
  return !!route.query?.queryPublic
})
// 判断用户是否为管理员
const loginUserStore = useLoginUserStore()
const loginUser = loginUserStore.loginUser
const isAdmin = computed(() => {
  return loginUser.userRole === 'admin'
})
</script>
<style scoped>
#spaceAnalyzePage {
  margin-bottom: 16px;
}
</style>
