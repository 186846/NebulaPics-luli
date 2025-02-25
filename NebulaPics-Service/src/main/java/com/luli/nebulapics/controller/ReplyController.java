package com.luli.nebulapics.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luli.nebulapics.common.BaseResponse;
import com.luli.nebulapics.common.ResultUtils;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.exception.ThrowUtils;
import com.luli.nebulapics.model.dto.reply.ReplyAddRequest;
import com.luli.nebulapics.model.dto.reply.ReplyQueryRequest;
import com.luli.nebulapics.model.entity.Reply;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.ReplyVo;
import com.luli.nebulapics.service.ReplyService;
import com.luli.nebulapics.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/reply")
public class ReplyController {
    @Resource
    private ReplyService replyService;

    @Resource
    private UserService userService;

    @PostMapping("/addReply")
    public BaseResponse<Boolean> addReply(@RequestBody ReplyAddRequest replyAddRequest, HttpServletRequest request){
        if (replyAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        replyService.AddReply(replyAddRequest, loginUser);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取帖子评论列表（封装类）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<ReplyVo>> listReplyVOByPage(@RequestBody ReplyQueryRequest replyQueryRequest,
                                                        HttpServletRequest request) {
        long current = replyQueryRequest.getCurrent();
        long size = replyQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        QueryWrapper<Reply> queryWrapper = replyService.getQueryWrapper(replyQueryRequest);
        Page<Reply> replyPage = replyService.page(new Page<>(current, size), queryWrapper);
        return ResultUtils.success(replyService.getReplyVoPage(replyPage, loginUser));
    }

    @PostMapping("/updateReply")
    public BaseResponse<Boolean> updateReply(@RequestBody ReplyVo replyVo, HttpServletRequest request){
        System.out.println(replyVo);
        User loginUser = userService.getLoginUser(request);
        replyService.upDateReply(replyVo, loginUser);
        return ResultUtils.success(true);
    }

    @PostMapping("/deleteReply")
    public BaseResponse<Boolean> deleteReply(@RequestBody ReplyVo replyVo, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        replyService.deleteReply(replyVo, loginUser);
        return ResultUtils.success(true);
    }
}
