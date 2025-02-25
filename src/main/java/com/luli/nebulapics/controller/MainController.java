package com.luli.nebulapics.controller;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import com.luli.nebulapics.common.BaseResponse;
import com.luli.nebulapics.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

import static cn.dev33.satoken.SaManager.log;
import static cn.hutool.http.ContentType.JSON;

@RestController
@RequestMapping("/")
public class MainController {
    /**
     * 健康检查
     */
    @GetMapping("/health")
    public BaseResponse<String> health() {
        return ResultUtils.success("OK");
    }
}
