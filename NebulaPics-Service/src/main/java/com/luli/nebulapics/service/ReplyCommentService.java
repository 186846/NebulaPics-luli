package com.luli.nebulapics.service;

import com.luli.nebulapics.model.dto.replyComment.ReplyCommentAddRequest;
import com.luli.nebulapics.model.dto.replyComment.ReplyTOCommentAddRequest;
import com.luli.nebulapics.model.entity.ReplyComment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.ReplyCommentVo;
import com.luli.nebulapics.model.vo.ReplyVo;

import java.util.List;

/**
* @author liuleliule
* @description 针对表【reply_comment(评论回复)】的数据库操作Service
* @createDate 2025-02-19 12:48:17
*/
public interface ReplyCommentService extends IService<ReplyComment> {
    ReplyComment AddReplyComment(ReplyCommentAddRequest replyCommentAddRequest, User user);

    ReplyComment AddReplyToComment(ReplyTOCommentAddRequest replyTOCommentAddRequest, User user);

    List<ReplyCommentVo> getByReplyId(Long replyId, User user);

    Boolean upDateReplyComment(ReplyCommentVo replyCommentVo, User loginUser);

    Boolean deleteReplyComment(ReplyCommentVo replyCommentVo, User loginUser);
}
