package com.luli.nebulapics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.exception.ThrowUtils;
import com.luli.nebulapics.model.dto.reply.ReplyAddRequest;
import com.luli.nebulapics.model.dto.reply.ReplyQueryRequest;
import com.luli.nebulapics.model.entity.Post;
import com.luli.nebulapics.model.entity.Reply;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.entity.UserAction;
import com.luli.nebulapics.model.enums.UserActionEnum;
import com.luli.nebulapics.model.vo.ReplyCommentVo;
import com.luli.nebulapics.model.vo.ReplyVo;
import com.luli.nebulapics.model.vo.UserVO;
import com.luli.nebulapics.service.*;
import com.luli.nebulapics.mapper.ReplyMapper;
import net.bytebuddy.implementation.bytecode.Throw;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
* @author liuleliule
* @description 针对表【reply(回复)】的数据库操作Service实现
* @createDate 2025-02-17 09:28:52
*/
@Service
public class ReplyServiceImpl extends ServiceImpl<ReplyMapper, Reply>
    implements ReplyService{

    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    @Resource
    private UserActionService userActionService;

    @Resource
    @Lazy
    private ReplyCommentService replyCommentService;


    @Override
    public Reply AddReply(ReplyAddRequest replyAddRequest, User loginUser) {
        Long userID = loginUser.getId();
        Reply reply = new Reply();

        reply.setPostId(replyAddRequest.getPostId());
        reply.setContent(replyAddRequest.getContent());
        reply.setUserId(userID);

        this.save(reply);

        Post post = postService.getById(replyAddRequest.getPostId());
        post.setReplyCount(post.getReplyCount() + 1);
        postService.updateById(post);
        return reply;
    }

    /**
     * 转换查询数据的对象类型
     * @param replyPage
     * @param loginUser
     * @return
     */
    @Override
    public Page<ReplyVo> getReplyVoPage(Page<Reply> replyPage, User loginUser) {
        List<Reply> replyList = replyPage.getRecords();
        Page<ReplyVo> replyVoPage = new Page<>(replyPage.getCurrent(), replyPage.getSize(), replyPage.getTotal());
        if (CollUtil.isEmpty(replyList)) {
            return replyVoPage;
        }
        // 对象列表 => 封装对象列表
        List<ReplyVo> replyVoList = replyList.stream()
                .map(reply -> {
                    ReplyVo replyVo = ReplyVo.objToVo(reply);
                    User user = userService.getById(reply.getUserId());
                    UserAction userAction = userActionService.getUserActionById(loginUser.getId(),reply.getId());
                    if (userAction !=null){
                        replyVo.setAction(UserActionEnum.getEnumByValue(userAction.getAction()));
                    }
                    if (user != null) {
                        UserVO userVO = new UserVO();
                        try {
                            userVO.setId(user.getId());
                            userVO.setUserAccount(user.getUserAccount());
                            userVO.setUserName(user.getUserName());
                            userVO.setUserAvatar(user.getUserAvatar());
                            userVO.setUserProfile(user.getUserProfile());
                            userVO.setUserRole(user.getUserRole());
                            userVO.setCreateTime(user.getCreateTime());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        replyVo.setUser(userVO);
                    }
                    System.out.println(user);
                    List<ReplyCommentVo> replyCommentVoList = replyCommentService.getByReplyId(replyVo.getId(),loginUser);
                    replyVo.setReplyCommentVoList(replyCommentVoList);
                    return replyVo;
                })
                .collect(Collectors.toList());

        replyVoPage.setRecords(replyVoList);
        return replyVoPage;
    }

    /**
     * 分页获取回复
     * @param replyQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<Reply> getQueryWrapper(ReplyQueryRequest replyQueryRequest) {
        QueryWrapper<Reply> queryWrapper = new QueryWrapper<>();
        if (replyQueryRequest == null) {
            return queryWrapper;
        }
        Long id = replyQueryRequest.getId();
        Long postId = replyQueryRequest.getPostId();
        Long userId = replyQueryRequest.getUserId();
        String content = replyQueryRequest.getContent();
        Long likeCount = replyQueryRequest.getLikeCount();
        Long dislikeCount = replyQueryRequest.getDislikeCount();
        Date editTime = replyQueryRequest.getEditTime();
        Date createTime = replyQueryRequest.getCreateTime();
        Date updateTime = replyQueryRequest.getUpdateTime();

        String sortField = replyQueryRequest.getSortField();
        String sortOrder = replyQueryRequest.getSortOrder();

        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.eq(ObjUtil.isNotEmpty(postId), "postId", postId);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.like(ObjUtil.isNotEmpty(content), "content", content);
        queryWrapper.like(ObjUtil.isNotEmpty(likeCount), "likeCount", likeCount);
        queryWrapper.like(ObjUtil.isNotEmpty(dislikeCount), "dislikeCount", dislikeCount);
        queryWrapper.eq(ObjUtil.isNotEmpty(editTime), "editTime", editTime);
        queryWrapper.eq(ObjUtil.isNotEmpty(createTime), "createTime", createTime);
        queryWrapper.eq(ObjUtil.isNotEmpty(updateTime), "updateTime", updateTime);



        // 排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    /**
     * 修改点赞和点踩的数据
     */
    @Override
    public Boolean upDateReply(ReplyVo replyVo, User loginUser) {
        Reply reply = ReplyVo.voToObj(replyVo);
        reply.setUserId(replyVo.getUser().getId());
        UserAction userAction =new UserAction();
        userAction.setReplyId(reply.getId());
        userAction.setReplyCommentId(0L);
        userAction.setUserId(loginUser.getId());
        userAction.setAction(UserActionEnum.getValueByName(String.valueOf(replyVo.getAction())));
        if (userAction.getAction() == null){
            userAction.setAction(2);
        }
        UserAction userAction1 = userActionService.getUserActionById(loginUser.getId(),reply.getId());
        if (userAction1 != null) {
            userAction.setId(userAction1.getId());
            userActionService.updateById(userAction);
        }else {
            userActionService.save(userAction);
        }
        this.updateById(reply);
        return true;
    }

    @Override
    public Boolean deleteReply(ReplyVo replyVo, User loginUser) {
        ThrowUtils.throwIf(ObjUtil.isEmpty(replyVo), ErrorCode.PARAMS_ERROR);
        if ((!replyVo.getUser().getId().equals(loginUser.getId()))&&(!loginUser.getUserRole().equals("admin"))) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户没有权限");
        }
        this.lambdaUpdate().eq(Reply::getId,replyVo.getId()).remove();
        Post post = postService.getById(replyVo.getPostId());
        post.setReplyCount(post.getReplyCount()-1);
        postService.updateById(post);
        return true;
    }
}




