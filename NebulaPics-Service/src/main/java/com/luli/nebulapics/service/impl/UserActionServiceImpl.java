package com.luli.nebulapics.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luli.nebulapics.model.entity.UserAction;
import com.luli.nebulapics.service.UserActionService;
import com.luli.nebulapics.mapper.UserActionMapper;
import org.springframework.stereotype.Service;

/**
* @author liuleliule
* @description 针对表【user_action(用户行为记录表)】的数据库操作Service实现
* @createDate 2025-02-18 17:53:08
*/
@Service
public class UserActionServiceImpl extends ServiceImpl<UserActionMapper, UserAction>
    implements UserActionService{

    @Override
    public UserAction getUserActionById(Long userId, Long replyId) {
        return lambdaQuery()
                .eq(UserAction::getUserId, userId)
                .eq(UserAction::getReplyId, replyId)
                .one();
    }

    @Override
    public UserAction getUserActionById2(Long userId, Long replyCommtentId) {
        return lambdaQuery()
                .eq(UserAction::getUserId, userId)
                .eq(UserAction::getReplyCommentId, replyCommtentId)
                .one();
    }

    @Override
    public  UserAction getUserActionById3(Long userId, Long postId){
        return lambdaQuery()
                .eq(UserAction::getUserId,userId)
                .eq(UserAction::getPostId,postId)
                .one();
    }

    @Override
    public boolean deleteByReplyId( Long replyId) {
        return this.lambdaUpdate()
                .eq(UserAction::getReplyId, replyId)
                .remove();
    }

    @Override
    public boolean deleteByReplyCommentId(Long replyCommentId) {
        return this.lambdaUpdate()
                .eq(UserAction::getReplyCommentId, replyCommentId)
                .remove();
    }
}




