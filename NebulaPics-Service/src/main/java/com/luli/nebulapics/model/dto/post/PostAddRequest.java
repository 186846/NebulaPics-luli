package com.luli.nebulapics.model.dto.post;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class PostAddRequest implements Serializable {
    // 话题标题
    private String title;
    // 图片内容列表，对应前端的 topicForm.content
    private List<String> content;
    // 话题分类
    private List<String> category;
}
