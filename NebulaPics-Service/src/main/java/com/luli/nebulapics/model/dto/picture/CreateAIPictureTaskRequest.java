package com.luli.nebulapics.model.dto.picture;

import cn.hutool.core.annotation.Alias;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.luli.nebulapics.api.aliyuncreatepicture.model.CreatePictureTaskRequest;
import lombok.Data;

import java.io.Serializable;

@Data
public class CreateAIPictureTaskRequest implements Serializable {
    /**
     * 扩图描述
     */
    private String prompt;
    /**
     * 扩图参数
     */
    private Parameters parameters;

    @Data
    public static class Parameters extends CreatePictureTaskRequest.Parameters implements Serializable {
        /**
         * 可选，图像风格
         * <auto>：默认值，由模型随机输出图像风格。
         * <photography>：摄影。
         * <portrait>：人像写真。
         * <3d cartoon>：3D卡通。
         * <anime>：动画。
         * <oil painting>：油画。
         * <watercolor>：水彩。
         * <sketch>：素描。
         * <chinese painting>：中国画。
         * <flat illustration>：扁平插画。
         */
        @Alias("style")
        private String style;
        /**
         * 可选，输出图像的分辨率
         * 可选值：1024*1024：默认值。
         * 720*1280
         * 768*1152
         * 1280*720
         */
        @Alias("size")
        private String size;
        /**
         * 可选，生成图片的图片数量 1-4
         */
        @Alias("n")
        private Integer n=1;

        /**
         * 可选，随机数种子，用于控制模型生成内容的随机性。
         */
        @Alias("seed")
        private Integer seed;

        /**
         * 可选，控制输出图像与垫图（参考图）的相似度。
         * 取值范围为[0.0, 1.0]。取值越大，代表生成的图像与参考图越相似。
         */
        @Alias("ref_strength")
        @JsonProperty("ref_strength")
        private Float refStrength;

        /**
         * 可选，基于垫图（参考图）生成图像的模式。
         */
        @Alias("ref_mode")
        private String refMode;
    }
    private static final long serialVersionUID = 1L;
}
