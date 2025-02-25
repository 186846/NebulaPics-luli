package com.luli.nebulapics.model.vo;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class GalleryStatisticsVo {
    private String galleryName;
    private List<CategoryCountVo> categoryCounts;

    // 添加分类统计信息
    public void addCategoryCount(CategoryCountVo categoryCount) {
        if (categoryCounts == null) {
            categoryCounts = new ArrayList<>(); // 若列表为 null，则进行初始化
        }
        this.categoryCounts.add(categoryCount);
    }
}
