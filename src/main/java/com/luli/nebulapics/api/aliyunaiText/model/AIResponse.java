package com.luli.nebulapics.api.aliyunaiText.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * AI 响应类
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    /**
     * 请求唯一标识
     */
    private String requestId;
    /**
     * 使用信息
     */
    private Usage usage;
    /**
     * 输出信息
     */
    private Output output;
    /**
     * 表示使用信息
     */
    @Data
    public static class Usage {
        /**
         * 输入的 token 数量
         */
        private Integer input_tokens;
        /**
         * 输出的 token 数量
         */
        private Integer output_tokens;
        /**
         * 总的 token 数量
         */
        private Integer total_tokens;
    }
    /**
     * 表示输出信息
     */
    @Data
    public static class Output {
        /**
         * 选择列表
         */
        private java.util.List<Choice> choices;
    }
    /**
     * 表示选择信息
     */
    @Data
    public static class Choice {
        /**
         * 结束原因
         */
        private String finish_reason;
        /**
         * 消息信息
         */
        private Message message;
    }
    /**
     * 表示消息信息
     */
    @Data
    public static class Message {
        /**
         * 角色
         */
        private String role;
        /**
         * 内容
         */
        private String content;
    }
}