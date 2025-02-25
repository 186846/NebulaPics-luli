package com.luli.nebulapics.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luli.nebulapics.common.BaseResponse;
import com.luli.nebulapics.common.ResultUtils;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.exception.ThrowUtils;
import com.luli.nebulapics.model.dto.post.PostAddRequest;
import com.luli.nebulapics.model.dto.post.PostQueryRequest;
import com.luli.nebulapics.model.entity.Post;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.PostVo;
import com.luli.nebulapics.service.PostService;
import com.luli.nebulapics.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/post")
public class PostController {
    @Resource
    private UserService userService;

    @Resource
    private PostService postService;

    /**
     * 添加贴子
     * @param postAddRequest
     * @param request
     * @return
     */
    @PostMapping("/addPost")
    public BaseResponse<Boolean> addPost(@RequestBody PostAddRequest postAddRequest, HttpServletRequest request){
        if (postAddRequest==null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        postService.AddPost(postAddRequest, loginUser);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取帖子列表（封装类）
     */
    @PostMapping("/list/page")
    public BaseResponse<Page<PostVo>> listPostVOByPage(@RequestBody PostQueryRequest postQueryRequest,
                                                       HttpServletRequest request) {
        long current = postQueryRequest.getCurrent();
        long size = postQueryRequest.getPageSize();
        // 限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);

        //查询数据库
        Page<Post> postPage = postService.page(new Page<>(current, size), postService.getQueryWrapper(postQueryRequest));
        Page<PostVo> postVoPage = postService.getPostVoPage(postPage, request);
        return ResultUtils.success(postVoPage);
    }

    /**
     * 获取帖子的详情信息
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/get/vo")
    public BaseResponse<PostVo> getPostVOById(long id, HttpServletRequest request) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        // 查询数据库
        Post post = postService.getById(id);
        ThrowUtils.throwIf(post == null, ErrorCode.NOT_FOUND_ERROR);
        PostVo postVo = postService.getPostVO(post,request);
        // 获取封装类
        return ResultUtils.success(postVo);
    }

    /**
     * 修改点赞数据
     * @param postVo
     * @param request
     * @return
     */
    @PostMapping("/update")
    public BaseResponse<Boolean> updatePost(@RequestBody PostVo postVo, HttpServletRequest request) {
        ThrowUtils.throwIf(postVo == null, ErrorCode.PARAMS_ERROR);
        //查询当前登录用户
        User loginUser = userService.getLoginUser(request);
        postService.updatePost(postVo,loginUser);
        return ResultUtils.success(true);
    }
}
