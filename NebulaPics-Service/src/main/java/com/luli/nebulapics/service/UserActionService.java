package com.luli.nebulapics.service;

import com.luli.nebulapics.model.entity.UserAction;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author liuleliule
* @description 针对表【user_action(用户行为记录表)】的数据库操作Service
* @createDate 2025-02-18 17:53:08
*/
public interface UserActionService extends IService<UserAction> {
    UserAction getUserActionById(Long userId,Long replyId);
    UserAction getUserActionById2(Long userId,Long replyCommtentId);

    UserAction getUserActionById3(Long userId, Long postId);

    boolean deleteByReplyId(Long replyId);

    boolean deleteByReplyCommentId( Long replyCommentId);
}
