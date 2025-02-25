package com.luli.nebulapics.model.dto.replyComment;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyCommentAddRequest implements Serializable {
    //评论索引
    private Long id;

    private Long ReplyId;

    private String Content;
}
