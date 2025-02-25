package com.luli.nebulapics.api.aliyuncreatepicture;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import com.luli.nebulapics.api.aliyuncreatepicture.model.CreatePictureTaskRequest;
import com.luli.nebulapics.api.aliyuncreatepicture.model.CreatePictureTaskResponse;
import com.luli.nebulapics.api.aliyuncreatepicture.model.GetPictureTaskResponse;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AliYunAiPictureApi {
    // 读取配置文件
    @Value("${aliYunAi.apiKey}")
    private String apiKey;
    // 创建任务地址
    public static final String CREATE_OUT_PAINTING_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/services/aigc/text2image/image-synthesis";
    // 查询任务状态
    public static final String GET_OUT_PAINTING_TASK_URL = "https://dashscope.aliyuncs.com/api/v1/tasks/%s";
    /**
     * 创建任务
     *
     * @param createPictureTaskRequest
     * @return
     */
    public CreatePictureTaskResponse createPictureTask(CreatePictureTaskRequest createPictureTaskRequest) {
        if (createPictureTaskRequest == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "扩图参数为空");
        }
        // 发送请求
        HttpRequest httpRequest = HttpRequest.post(CREATE_OUT_PAINTING_TASK_URL)
                .header("Authorization", "Bearer " + apiKey)
                // 必须开启异步处理
                .header("X-DashScope-Async", "enable")
                .header("Content-Type", "application/json")
                .body(JSONUtil.toJsonStr(createPictureTaskRequest));
        // 处理响应
        try (HttpResponse httpResponse = httpRequest.execute()) {
            if (!httpResponse.isOk()) {
                log.error("请求异常：{}", httpResponse.body());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 创图失败");
            }
            CreatePictureTaskResponse createPictureTaskResponse = JSONUtil.toBean(httpResponse.body(), CreatePictureTaskResponse.class);
            if (createPictureTaskResponse.getCode() != null) {
                String errorMessage = createPictureTaskResponse.getMessage();
                log.error("请求异常：{}", errorMessage);
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "AI 创图失败，" + errorMessage);
            }
            return createPictureTaskResponse;
        }
    }
    /**
     * 查询创建的任务结果
     *
     * @param taskId
     * @return
     */
    public GetPictureTaskResponse getPictureTask(String taskId) {
        if (StrUtil.isBlank(taskId)) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "任务 ID 不能为空");
        }
        // 处理响应
        String url = String.format(GET_OUT_PAINTING_TASK_URL, taskId);
        try (HttpResponse httpResponse = HttpRequest.get(url)
                .header("Authorization", "Bearer " + apiKey)
                .execute()) {
            if (!httpResponse.isOk()) {
                log.error("请求异常：{}", httpResponse.body());
                throw new BusinessException(ErrorCode.OPERATION_ERROR, "获取任务结果失败");
            }
            return JSONUtil.toBean(httpResponse.body(), GetPictureTaskResponse.class);
        }
    }
}