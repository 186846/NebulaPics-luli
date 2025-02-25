package com.luli.nebulapics.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.TableField;
import com.luli.nebulapics.model.entity.Post;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class PostVo implements Serializable {
    /**
     * id
     */
    private Long id;

    /**
     * 帖子标题
     */
    private String title;

    /**
     * 帖子内容
     */
    private List<String> content;

    /**
     * 帖子分类
     */
    private List<String> category;

    /**
     * 发帖用户 ID
     */
    private UserVO user;

    /**
     * 浏览次数
     */
    private Integer viewCount;

    /**
     * 回复次数
     */
    private Integer replyCount;

    /**
     * 点赞数
     */
    private Integer heartCount;

    private String action;

    /**
     * 编辑时间
     */
    private Date editTime;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    /**
     * 封装类转对象
     */
    public static Post voToObj(PostVo postVo) {
        if (postVo == null) {
            return null;
        }
        Post post = new Post();
        BeanUtils.copyProperties(postVo, post);
        // 类型不同，需要转换
        post.setContent(JSONUtil.toJsonStr(postVo.getContent()));
        post.setCategory(JSONUtil.toJsonStr(postVo.getCategory()));
        return post;
    }

    /**
     * 对象转封装类
     */
    public static PostVo objToVo(Post post) {
        if (post == null) {
            return null;
        }
        PostVo postVo = new PostVo();
        BeanUtils.copyProperties(post, postVo);
        // 类型不同，需要转换
        postVo.setContent(JSONUtil.toList(post.getContent(), String.class));
        postVo.setCategory(JSONUtil.toList(post.getCategory(), String.class));
//        pictureVO.setTags(JSONUtil.toList(picture.getTags(), String.class));
        return postVo;
    }
}
