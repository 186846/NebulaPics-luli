package com.luli.nebulapics.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论回复
 * @TableName reply_comment
 */
@TableName(value ="reply_comment")
@Data
public class ReplyComment implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属评论 ID
     */
    private Long replyId;

    /**
     * 发送用户 ID
     */
    private Long userId;

    /**
     * 接收用户 ID
     */
    private Long receiveUserId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 点赞个数
     */
    private Long likeCount;

    /**
     * 下踩个数
     */
    private Long dislikeCount;

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
}