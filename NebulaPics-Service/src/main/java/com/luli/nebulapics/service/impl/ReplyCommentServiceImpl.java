package com.luli.nebulapics.service.impl;

import cn.hutool.core.util.ObjUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.exception.ThrowUtils;
import com.luli.nebulapics.model.dto.replyComment.ReplyCommentAddRequest;
import com.luli.nebulapics.model.dto.replyComment.ReplyTOCommentAddRequest;
import com.luli.nebulapics.model.entity.Reply;
import com.luli.nebulapics.model.entity.ReplyComment;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.entity.UserAction;
import com.luli.nebulapics.model.enums.UserActionEnum;
import com.luli.nebulapics.model.vo.ReplyCommentVo;
import com.luli.nebulapics.service.ReplyCommentService;
import com.luli.nebulapics.mapper.ReplyCommentMapper;
import com.luli.nebulapics.service.ReplyService;
import com.luli.nebulapics.service.UserActionService;
import com.luli.nebulapics.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author liuleliule
* @description 针对表【reply_comment(评论回复)】的数据库操作Service实现
* @createDate 2025-02-19 12:48:17
*/
@Service
public class ReplyCommentServiceImpl extends ServiceImpl<ReplyCommentMapper, ReplyComment>
    implements ReplyCommentService{
    @Resource
    @Lazy
    private ReplyService replyService;

    @Resource
    @Lazy
    private UserActionService userActionService;

    @Resource
    private UserService userService;

    @Override
    public ReplyComment AddReplyComment(ReplyCommentAddRequest replyCommentAddRequest, User user) {
        Reply reply = replyService.getById(replyCommentAddRequest.getReplyId());
        ReplyComment replyComment = new ReplyComment();
        replyComment.setReplyId(reply.getId());
        replyComment.setUserId(user.getId());
        replyComment.setReceiveUserId(reply.getUserId());
        replyComment.setContent(replyCommentAddRequest.getContent());
        this.save(replyComment);
        return replyComment;
    }

    @Override
    public ReplyComment AddReplyToComment(ReplyTOCommentAddRequest replyTOCommentAddRequest, User user){
        ReplyComment replyComment = new ReplyComment();
        replyComment.setReplyId(replyTOCommentAddRequest.getReplyId());
        replyComment.setUserId(user.getId());
        replyComment.setReceiveUserId(replyTOCommentAddRequest.getReceiveUserId());
        replyComment.setContent(replyTOCommentAddRequest.getContent());
        this.save(replyComment);
        return replyComment;
    }

    @Override
    public List<ReplyCommentVo> getByReplyId(Long replyId,User user) {
        // 使用 MyBatis-Plus 的 LambdaQueryWrapper 进行条件查询
        List<ReplyComment> replyCommentList = lambdaQuery()
                .eq(ReplyComment::getReplyId, replyId)
                .list();
        // 将实体类列表转换为视图对象列表
        return replyCommentList.stream()
                .map(replyComment -> {
                    ReplyCommentVo replyCommentVo = new ReplyCommentVo();
                    BeanUtils.copyProperties(replyComment, replyCommentVo);
                    UserAction userAction = userActionService.getUserActionById2(user.getId(),replyCommentVo.getId());
                    if (userAction !=null){
                        replyCommentVo.setAction(UserActionEnum.getEnumByValue(userAction.getAction()));
                    }
                    replyCommentVo.setUser(userService.getById(replyComment.getUserId()));
                    replyCommentVo.setReceiveUser(userService.getById(replyComment.getReceiveUserId()));
                    return replyCommentVo;
                })
                .collect(Collectors.toList());
    }

    @Override
    public Boolean upDateReplyComment(ReplyCommentVo replyCommentVo, User loginUser) {
        ReplyComment replyComment = new ReplyComment();
        UserAction userAction =new UserAction();
        userAction.setReplyCommentId(replyCommentVo.getId());
        userAction.setReplyId(0L);
        userAction.setUserId(loginUser.getId());
        userAction.setAction(UserActionEnum.getValueByName(String.valueOf(replyCommentVo.getAction())));
        if (userAction.getAction() == null){
            userAction.setAction(2);
        }
        UserAction userAction1 = userActionService.getUserActionById2(loginUser.getId(),replyCommentVo.getId());
        if (userAction1 != null) {
            userAction.setId(userAction1.getId());
            userActionService.updateById(userAction);
        }else {
            userActionService.save(userAction);
        }
        BeanUtils.copyProperties(replyCommentVo, replyComment);
        replyComment.setUserId(replyCommentVo.getUser().getId());
        replyComment.setReceiveUserId(replyCommentVo.getReceiveUser().getId());
        this.updateById(replyComment);
        return true;
    }

    @Override
    public Boolean deleteReplyComment(ReplyCommentVo replyCommentVo, User loginUser) {
        ThrowUtils.throwIf(ObjUtil.isEmpty(replyCommentVo), ErrorCode.PARAMS_ERROR);
        if ((!replyCommentVo.getUser().getId().equals(loginUser.getId()))&&(!loginUser.getUserRole().equals("admin"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户没有权限");
        }
        this.lambdaUpdate().eq(ReplyComment::getId,replyCommentVo.getId()).remove();
        return true;
    }
}




