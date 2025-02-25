package com.luli.nebulapics.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum PostSortTypeEnum {
    LATEST("最新",1),
    HOT("最热",2);

    private final String text;
    private final int value;

    PostSortTypeEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据value获取数据
     */
    public static PostSortTypeEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (PostSortTypeEnum postSortTypeEnum : PostSortTypeEnum.values()) {
            if (postSortTypeEnum.value == value) {
                return postSortTypeEnum;
            }
        }
        return null;
    }
}
