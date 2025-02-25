package com.luli.nebulapics.model.dto.reply;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyAddRequest implements Serializable {
    //评论索引
    private Long id;

    //评论帖子id
    private Long postId;

    //帖子评论
    private String content;
}
