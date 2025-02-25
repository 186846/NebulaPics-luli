package com.luli.nebulapics.model.dto.post;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luli.nebulapics.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class PostQueryRequest extends PageRequest implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
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
    private Long userId;

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

    /**
     * 创建时间
     */
    private Date createTime;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}
