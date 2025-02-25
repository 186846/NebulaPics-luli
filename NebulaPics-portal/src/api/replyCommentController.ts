// @ts-ignore
/* eslint-disable */
import request from '@/request'

/** addReplyComment POST /api/ReplyComment/addReplyComment */
export async function addReplyCommentUsingPost(
  body: API.ReplyCommentAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/ReplyComment/addReplyComment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** addReplyTOComment POST /api/ReplyComment/addReplyTOComment */
export async function addReplyToCommentUsingPost(
  body: API.ReplyTOCommentAddRequest,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/ReplyComment/addReplyTOComment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** deleteReplyComment POST /api/ReplyComment/deleteReply */
export async function deleteReplyCommentUsingPost(
  body: API.ReplyCommentVo,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/ReplyComment/deleteReply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}

/** updateReplyComment POST /api/ReplyComment/updateReplyComment */
export async function updateReplyCommentUsingPost(
  body: API.ReplyCommentVo,
  options?: { [key: string]: any }
) {
  return request<API.BaseResponseBoolean_>('/api/ReplyComment/updateReplyComment', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  })
}
