package com.luli.nebulapics.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * Serializable接口就是Java提供用来进行高效率的异地共享实例对象的机制，实现这个接口即可。
 */
@Data
public class UserAddRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    /**
     * 用户角色: user, admin
     */
    private String userRole;

    private static final long serialVersionUID = 1L;
}
