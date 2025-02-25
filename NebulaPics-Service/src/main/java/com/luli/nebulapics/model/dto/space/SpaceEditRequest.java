package com.luli.nebulapics.model.dto.space;

import lombok.Data;
import java.io.Serializable;
/**
 * 编辑空间请求
 * 目前只允许编辑空间名称
 */
@Data
public class SpaceEditRequest implements Serializable {
    /**
     * 空间 id
     */
    private Long id;
    /**
     * 空间名称
     */
    private String spaceName;

    /**
     * 空间级别：0-普通版 1-专业版 2-旗舰版
     */
    private Integer spaceLevel;

    private static final long serialVersionUID = 1L;
}