package com.luli.nebulapics.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 回复
 * @TableName reply
 */
@TableName(value ="reply")
@Data
public class Reply implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 所属帖子 ID
     */
    private Long postId;

    /**
     * 回复用户 ID
     */
    private Long userId;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 点赞个数
     */
    private Long likeCount;

    /**
     * 点踩个数
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