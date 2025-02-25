package com.luli.nebulapics.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

@Getter
public enum UserActionEnum {
    disliked("点踩", 0),
    liked("点赞", 1);

    private final String text;

    private final int value;

    UserActionEnum(String text, int value) {
        this.text = text;
        this.value = value;
    }

    /**
     * 根据 value 获取枚举
     */
    public static UserActionEnum getEnumByValue(Integer value) {
        if (ObjUtil.isEmpty(value)) {
            return null;
        }
        for (UserActionEnum userActionEnum : UserActionEnum.values()) {
            if (userActionEnum.value == value) {
                return userActionEnum;
            }
        }
        return null;
    }

    /**
     * 根据枚举名称获取对应的 value
     */
    public static Integer getValueByName(String name) {
        for (UserActionEnum userActionEnum : UserActionEnum.values()) {
            if (userActionEnum.name().equals(name)) {
                return userActionEnum.value;
            }
        }
        return null;
    }
}
