package com.luli.nebulapics.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.exception.ThrowUtils;
import com.luli.nebulapics.model.dto.post.PostAddRequest;
import com.luli.nebulapics.model.dto.post.PostQueryRequest;
import com.luli.nebulapics.model.entity.Post;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.entity.UserAction;
import com.luli.nebulapics.model.vo.PostVo;
import com.luli.nebulapics.model.vo.UserVO;
import com.luli.nebulapics.service.PostService;
import com.luli.nebulapics.mapper.PostMapper;
import com.luli.nebulapics.service.UserActionService;
import com.luli.nebulapics.service.UserService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author liuleliule
 * @description 针对表【post(帖子)】的数据库操作Service实现
 * @createDate 2025-02-14 10:25:43
 */
@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post>
        implements PostService {

    @Resource
    private UserService userService;

    @Resource
    private UserActionService userActionService;

    @Override
    public void validPost(Post post) {
        ThrowUtils.throwIf(post == null, ErrorCode.PARAMS_ERROR);

        //从对象中获取数据
        String title = post.getTitle();
        String content = post.getContent();
        String category = post.getCategory();
        if (StrUtil.isNotBlank(title)) {
            ThrowUtils.throwIf(title.length() > 15, ErrorCode.PARAMS_ERROR);
        }
        if (StrUtil.isNotBlank(content)) {
            System.out.println(content);
            List<String> contentList = JSONUtil.toList(content, String.class);
            System.out.println(contentList.size());
            ThrowUtils.throwIf(contentList.size() > 15, ErrorCode.PARAMS_ERROR);
        }
        if (StrUtil.isNotBlank(category)) {
            List<String> categoryList = JSONUtil.toList(category, String.class);
            ThrowUtils.throwIf(categoryList.size() > 5, ErrorCode.PARAMS_ERROR);
        }
    }

    @Override
    public Post AddPost(PostAddRequest postAddRequest, User loginUser) {
        Post post = new Post();
        post.setTitle(postAddRequest.getTitle());
        post.setCategory(JSONUtil.toJsonStr(postAddRequest.getCategory()));
        post.setContent(JSONUtil.toJsonStr(postAddRequest.getContent()));
        post.setUserId(loginUser.getId());
        this.validPost(post);
        this.save(post);
        return post;
    }

    @Override
    public Page<PostVo> getPostVoPage(Page<Post> postPage, HttpServletRequest request) {
        List<Post> postList = postPage.getRecords();
        Page<PostVo> postVoPage = new Page<>(postPage.getCurrent(), postPage.getSize(), postPage.getTotal());
        if (CollUtil.isEmpty(postList)) {
            return postVoPage;
        }

        //对象列表 =》 封装对象列表
        List<PostVo> postVoList = postList.stream()
                .map(post -> {
                    PostVo postVo = PostVo.objToVo(post);
                    // 根据 userId 查询用户信息
                    User user = userService.getById(post.getUserId());
                    // 获取登录的用户
                    User loginUser = userService.getLoginUser(request);
                    UserAction userAction = userActionService.getUserActionById3(loginUser.getId(), post.getId());
                    if (userAction != null && userAction.getPostAction() ==1)  {
                        postVo.setAction("liked");
                    }else {
                        postVo.setAction(null);
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
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        postVo.setUser(userVO);
                    }
                    return postVo;
                })
                .collect(Collectors.toList());

        postVoPage.setRecords(postVoList);
        return postVoPage;
    }

    @Override
    public QueryWrapper<Post> getQueryWrapper(PostQueryRequest postQueryRequest) {
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        if (postQueryRequest == null) {
            return queryWrapper;
        }
        Long id = postQueryRequest.getId();
        String title = postQueryRequest.getTitle();
        List<String> content = postQueryRequest.getContent();
        List<String> category = postQueryRequest.getCategory();
        Long userId = postQueryRequest.getUserId();
        Integer viewCount = postQueryRequest.getViewCount();
        Integer replyCount = postQueryRequest.getReplyCount();
        Integer heartCount = postQueryRequest.getHeartCount();


        String sortField = postQueryRequest.getSortField();
        String sortOrder = postQueryRequest.getSortOrder();

        queryWrapper.eq(ObjUtil.isNotEmpty(id), "id", id);
        queryWrapper.like(ObjUtil.isNotEmpty(title), "title", title);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "userId", userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(viewCount), "viewCount", viewCount);
        queryWrapper.eq(ObjUtil.isNotEmpty(replyCount), "replyCount", replyCount);
        queryWrapper.eq(ObjUtil.isNotEmpty(heartCount), "heartCount", heartCount);
        if (CollUtil.isNotEmpty(category)) {
            for (String categoryStr : category) {
                queryWrapper.like("category", "\"" + categoryStr + "\"");
            }
        }
        if (CollUtil.isNotEmpty(content)) {
            for (String contentStr : content) {
                queryWrapper.like("content", "\"" + contentStr + "\"");
            }
        }

        //排序
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public PostVo getPostVO(Post post, HttpServletRequest request) {
        // 先查询已存在的 post 对象
        Post existingPost = this.getById(post.getId());
        if (existingPost != null) {
            // 更新 viewCount
            existingPost.setViewCount(existingPost.getViewCount() + 1);
            this.updateById(existingPost);
            post = existingPost;
        } else {
            // 若不存在，则插入新记录
            post.setViewCount(post.getViewCount() + 1);
            this.save(post);
        }

        PostVo postVo = PostVo.objToVo(post);
        // 关联查询用户信息
        Long userId = post.getUserId();
        if (userId != null && userId > 0) {
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            postVo.setUser(userVO);
        }
        return postVo;
    }

    @Override
    public Boolean updatePost(PostVo postVo, User loginUser) {
        Post post = PostVo.voToObj(postVo);
        post.setUserId(postVo.getUser().getId());
        UserAction userAction = new UserAction();
        userAction.setUserId(loginUser.getId());
        userAction.setReplyId(0L);
        userAction.setReplyCommentId(0L);
        userAction.setAction(2);
        userAction.setPostAction(1);
        userAction.setPostId(post.getId());
        if (Objects.equals(postVo.getAction(), "liked")){
            userAction.setPostAction(1);
        }else {
            userAction.setPostAction(0);
        }
        //查询是否存在用户对帖子的点赞信息
        UserAction userAction1 = userActionService.getUserActionById3(loginUser.getId(), post.getId());
        if (userAction1 != null)  {
            userAction.setId(userAction1.getId());
            userActionService.updateById(userAction);
        }else {
            userActionService.save(userAction);
        }
        this.updateById(post);
        return true;
    }
}