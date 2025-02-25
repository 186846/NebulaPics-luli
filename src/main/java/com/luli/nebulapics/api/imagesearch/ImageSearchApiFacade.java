package com.luli.nebulapics.api.imagesearch;


import com.luli.nebulapics.api.imagesearch.model.ImageSearchResult;
import com.luli.nebulapics.api.imagesearch.sub.GetImageFirstUrlApi;
import com.luli.nebulapics.api.imagesearch.sub.GetImageListApi;
import com.luli.nebulapics.api.imagesearch.sub.GetImagePageUrlApi;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
@Slf4j
public class ImageSearchApiFacade {
    /**
     * 搜索图片
     * @param imageUrl
     * @return
     */
    public static List<ImageSearchResult> searchImage(String imageUrl) {
        String imagePageUrl = GetImagePageUrlApi.getImagePageUrl(imageUrl);
        String imageFirstUrl = GetImageFirstUrlApi.getImageFirstUrl(imagePageUrl);
        List<ImageSearchResult> imageList = GetImageListApi.getImageList(imageFirstUrl);
        return imageList;
    }
    public static void main(String[] args) {
        List<ImageSearchResult> imageList = searchImage("https://luli-1319967679.cos.ap-guangzhou.myqcloud.com//public/1881995682265395201/2025-01-23_JYeMTjDmcwtAfxLJ.png");
        System.out.println("结果列表" + imageList);
    }
}