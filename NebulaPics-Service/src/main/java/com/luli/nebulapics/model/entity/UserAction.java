package com.luli.nebulapics.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 用户行为记录表
 * @TableName user_action
 */
@TableName(value ="user_action")
@Data
public class UserAction implements Serializable {
    /**
     * 操作记录ID
     */
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 帖子id
     */
    private Long postId;

    /**
     * 回复ID
     */
    private Long replyId;

    /**
     * 评论讨论id
     */
    private Long replyCommentId;

    /**
     * 操作类型
     */
    private Integer action;

    /**
     * 帖子 用户是否点赞
     */
    private Integer postAction;

    /**
     * 操作时间
     */
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}