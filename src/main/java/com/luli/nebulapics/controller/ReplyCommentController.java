package com.luli.nebulapics.controller;

import com.luli.nebulapics.common.BaseResponse;
import com.luli.nebulapics.common.ResultUtils;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.model.dto.replyComment.ReplyCommentAddRequest;
import com.luli.nebulapics.model.dto.replyComment.ReplyTOCommentAddRequest;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.ReplyCommentVo;
import com.luli.nebulapics.service.ReplyCommentService;
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
@RequestMapping("/ReplyComment")
public class ReplyCommentController {
    @Resource
    private ReplyCommentService replyCommentService;

    @Resource
    private UserService userService;

    @PostMapping("/addReplyComment")
    public BaseResponse<Boolean> addReplyComment(@RequestBody ReplyCommentAddRequest replyCommentAddRequest, HttpServletRequest request){
        if (replyCommentAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        System.out.println(replyCommentAddRequest);
        replyCommentService.AddReplyComment(replyCommentAddRequest, loginUser);
        return ResultUtils.success(true);
    }

    @PostMapping("/addReplyTOComment")
    public BaseResponse<Boolean> addReplyTOComment(@RequestBody ReplyTOCommentAddRequest replyTOCommentAddRequest, HttpServletRequest request){
        if (replyTOCommentAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        System.out.println(replyTOCommentAddRequest);
        replyCommentService.AddReplyToComment(replyTOCommentAddRequest, loginUser);
        return ResultUtils.success(true);
    }

    @PostMapping("/updateReplyComment")
    public BaseResponse<Boolean> updateReplyComment(@RequestBody ReplyCommentVo replyCommentVo, HttpServletRequest request){
        System.out.println(replyCommentVo);
        User loginUser = userService.getLoginUser(request);
        replyCommentService.upDateReplyComment(replyCommentVo, loginUser);
        return ResultUtils.success(true);
    }

    @PostMapping("/deleteReply")
    public BaseResponse<Boolean> deleteReplyComment(@RequestBody ReplyCommentVo replyCommentVo, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        replyCommentService.deleteReplyComment(replyCommentVo, loginUser);
        return ResultUtils.success(true);
    }
}
