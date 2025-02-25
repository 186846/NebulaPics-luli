package com.luli.nebulapics.model.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.enums.UserActionEnum;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ReplyCommentVo implements Serializable {
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
    private User user;

    /**
     * 接收用户 ID
     */
    private User receiveUser;

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
     *用户当前是否点赞或点踩
     */
    private UserActionEnum action;

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
