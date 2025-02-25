package com.luli.nebulapics.model.dto.replyComment;

import lombok.Data;

import java.io.Serializable;

@Data
public class ReplyTOCommentAddRequest implements Serializable {
    private Long replyId;
    private Long receiveUserId;
    private String content;
}
