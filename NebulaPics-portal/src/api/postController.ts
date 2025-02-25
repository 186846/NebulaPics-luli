// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addPost POST /api/post/addPost */
export async function addPostUsingPost(body: API.PostAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/post/addPost', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** getPostVOById GET /api/post/get/vo */
export async function getPostVoByIdUsingGet(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getPostVOByIdUsingGETParams,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePostVo_>('/api/post/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  })
}

/** listPostVOByPage POST /api/post/list/page */
export async function listPostVoByPageUsingPost(
  body: API.PostQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePagePostVo_>('/api/post/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updatePost POST /api/post/update */
export async function updatePostUsingPost(body: API.PostVo, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/post/update', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
