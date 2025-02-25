package com.luli.nebulapics.model.vo;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.luli.nebulapics.model.entity.Reply;
import com.luli.nebulapics.model.enums.UserActionEnum;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
public class ReplyVo implements Serializable {
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
    private UserVO user;

    /**
     * 回复内容
     */
    private String content;

    /**
     * 点赞数
     */
    private Long likeCount;

    /**
     * 点踩数
     */
    private Long dislikeCount;

    /**
     * 当前用户对该评论的评价
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
     * 评论讨论数据
     */
    private List<ReplyCommentVo> replyCommentVoList;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    /**
     * 封装类转对象
     */
    public static Reply voToObj(ReplyVo replyVo) {
        if (replyVo == null) {
            return null;
        }
        Reply reply = new Reply();
        BeanUtils.copyProperties(replyVo, reply);
        // 类型不同，需要转换

        return reply;
    }

    /**
     * 对象转封装类
     */
    public static ReplyVo objToVo(Reply reply) {
        if (reply == null) {
            return null;
        }
        ReplyVo replyVo = new ReplyVo();
        BeanUtils.copyProperties(reply, replyVo);
        // 类型不同，需要转换

        return replyVo;
    }
}
