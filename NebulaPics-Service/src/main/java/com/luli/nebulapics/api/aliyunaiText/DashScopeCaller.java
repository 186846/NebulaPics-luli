package com.luli.nebulapics.api.aliyunaiText;

import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;
import com.google.gson.Gson;
import com.luli.nebulapics.api.aliyunaiText.model.AIRequest;
import com.luli.nebulapics.api.aliyunaiText.model.AIResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Slf4j
@Component
public class DashScopeCaller {
    // 读取配置文件
    @Value("${aliYunAi.apiKey}")
    private String apiKey;

    // 静态方法，用于调用大模型并返回结果
    public AIResponse callDashScope(AIRequest userMessage) {
        try {
            // 创建 Generation 实例
            Generation gen = new Generation();
            // 构建系统消息
            Message systemMsg = Message.builder()
                    .role(Role.SYSTEM.getValue())
                    .content("You are a helpful assistant.")
                    .build();
            // 构建用户消息
            Message userMsg = Message.builder()
                    .role(Role.USER.getValue())
                    .content(userMessage.getUserMessage())
                    .build();
            // 构建 GenerationParam 参数
            GenerationParam param = GenerationParam.builder()
                    .apiKey(apiKey)
                    .model("qwen-max-2025-01-25")
                    .messages(Arrays.asList(systemMsg, userMsg))
                    .resultFormat(GenerationParam.ResultFormat.MESSAGE)
                    .build();
            // 调用大模型并获取结果
            GenerationResult result = gen.call(param);
            // 将结果转换为 JSON 字符串
            String jsonResult = JsonUtils.toJson(result);
            // 使用 Gson 将 JSON 字符串解析为 AIResponse 对象
            Gson gson = new Gson();
            return gson.fromJson(jsonResult, AIResponse.class);
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 打印异常信息
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
            return null;
        }
    }
}
