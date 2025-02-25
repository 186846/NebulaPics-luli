// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addReply POST /api/reply/addReply */
export async function addReplyUsingPost(
  body: API.ReplyAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/reply/addReply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteReply POST /api/reply/deleteReply */
export async function deleteReplyUsingPost(body: API.ReplyVo, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/reply/deleteReply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** listReplyVOByPage POST /api/reply/list/page */
export async function listReplyVoByPageUsingPost(
  body: API.ReplyQueryRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponsePageReplyVo_>('/api/reply/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateReply POST /api/reply/updateReply */
export async function updateReplyUsingPost(body: API.ReplyVo, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean_>('/api/reply/updateReply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
