package com.luli.nebulapics.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luli.nebulapics.model.dto.post.PostAddRequest;
import com.luli.nebulapics.model.dto.post.PostQueryRequest;
import com.luli.nebulapics.model.entity.Post;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.PostVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author liuleliule
* @description 针对表【post(帖子)】的数据库操作Service
* @createDate 2025-02-14 10:25:43
*/
public interface PostService extends IService<Post> {
    void validPost(Post post);

    Post AddPost(PostAddRequest postAddRequest, User loginUser);

    Page<PostVo> getPostVoPage(Page<Post> postPage, HttpServletRequest request);

    QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest);

    PostVo getPostVO(Post post, HttpServletRequest request);

    Boolean updatePost(PostVo postVo, User loginUser);
}
