package com.luli.nebulapics.controller;

import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luli.nebulapics.annotation.AuthCheck;
import com.luli.nebulapics.api.aliyunaiText.DashScopeCaller;
import com.luli.nebulapics.api.aliyunaiText.model.AIRequest;
import com.luli.nebulapics.api.aliyunaiText.model.AIResponse;
import com.luli.nebulapics.common.BaseResponse;
import com.luli.nebulapics.common.DeleteRequest;
import com.luli.nebulapics.common.ResultUtils;
import com.luli.nebulapics.constant.UserConstant;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.exception.ThrowUtils;
import com.luli.nebulapics.model.dto.user.*;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.LoginUserVO;
import com.luli.nebulapics.model.vo.UserVO;
import com.luli.nebulapics.service.UserService;
import com.luli.nebulapics.utils.MailSendCode;
import com.luli.nebulapics.utils.RedisVerificationCodeService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @Resource
    private DashScopeCaller dashScopeCaller;

    @Resource
    private RedisVerificationCodeService redisVerificationCodeService;

    @Resource
    private MailSendCode mailSendCode;

    private final Lock lock = new ReentrantLock();


    /**
     * 生成验证码
     */
    // 生成验证码并保存到 Redis
    @PostMapping("/sendMailCode")
    public BaseResponse<Boolean> sendMailCode(@RequestBody SendMailCodeRequest sendMailCodeRequest) {
        System.out.println(sendMailCodeRequest);
        ThrowUtils.throwIf(!mailSendCode.isValidEmail(sendMailCodeRequest.getEmail()),ErrorCode.PARAMS_ERROR);
        // 使用 hutool-all 生成 6 位随机数验证码
        String code = RandomUtil.randomNumbers(6);
        // 验证码有效期为 5 分钟
        redisVerificationCodeService.saveVerificationCode(sendMailCodeRequest.getEmail(), code, 5, TimeUnit.MINUTES);
        boolean result = mailSendCode.SendMail(sendMailCodeRequest.getEmail(),code);
        return ResultUtils.success(result);
    }

    /**
     * 调用大模型服务
     */
    @PostMapping("/callDashScope")
    public BaseResponse<AIResponse> callDashScope(@RequestBody AIRequest request) {
        lock.lock(); // 获取锁
        try {
            // 检查请求体是否为空
            ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
            String userMessage = request.getUserMessage();
            // 检查用户消息是否为空
            ThrowUtils.throwIf(userMessage == null || userMessage.isEmpty(), ErrorCode.PARAMS_ERROR);
            // 调用大模型服务
            AIResponse aiResponse = dashScopeCaller.callDashScope(request);
            // 检查调用结果是否为空
            ThrowUtils.throwIf(aiResponse == null, ErrorCode.OPERATION_ERROR, "调用大模型服务时发生错误");
            System.out.println(aiResponse);
            // 返回成功响应
            return ResultUtils.success(aiResponse);
        } finally {
            lock.unlock(); // 释放锁
        }
    }


    /**
     * 用户注册
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(!mailSendCode.isValidEmail(userRegisterRequest.getUserAccount()),ErrorCode.PARAMS_ERROR);
//        ThrowUtils.throwIf(userRegisterRequest == null, ErrorCode.PARAMS_ERROR);
        System.out.println(userRegisterRequest);
        String userAccount = userRegisterRequest.getUserAccount();
        String code = userRegisterRequest.getCode();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        long result = userService.userRegister(userAccount,code, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 登录
     * @param userLoginRequest
     * @param request
     * @return
     */

    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(userLoginRequest == null, ErrorCode.PARAMS_ERROR);
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        String captcha = userLoginRequest.getCaptcha();
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, captcha, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 获取当前登录用户
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        return ResultUtils.success(userService.getLoginUserVO(loginUser));
    }

    /**
     * 注销
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 创建用户
     */
    @PostMapping("/add")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest) {
        ThrowUtils.throwIf(userAddRequest == null, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        // 默认密码 12345678
        final String DEFAULT_PASSWORD = "12345678";
        String encryptPassword = userService.getEncryptPassword(DEFAULT_PASSWORD);
        user.setUserPassword(encryptPassword);
        boolean result = userService.save(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(user.getId());
    }

    /**
     * 根据 id 获取用户（仅管理员）
     */
    @GetMapping("/get")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<User> getUserById(long id) {
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 根据 id 获取包装类
     */
    @GetMapping("/get/vo")
    public BaseResponse<UserVO> getUserVOById(long id) {
        BaseResponse<User> response = getUserById(id);
        User user = response.getData();
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest) {
        if (deleteRequest == null || deleteRequest.getId() <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        System.out.println(deleteRequest);
        boolean b = userService.removeById(deleteRequest.getId());
        System.out.println(b);
        return ResultUtils.success(b);
    }

    /**
     * 更新用户
     */
    @PostMapping("/update")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest) {
        if (userUpdateRequest == null || userUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = new User();
        BeanUtils.copyProperties(userUpdateRequest, user);
        boolean result = userService.updateById(user);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(true);
    }

    /**
     * 分页获取用户封装列表（仅管理员）
     *
     * @param userQueryRequest 查询请求参数
     */
    @PostMapping("/list/page/vo")
    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest) {
        ThrowUtils.throwIf(userQueryRequest == null, ErrorCode.PARAMS_ERROR);
        long current = userQueryRequest.getCurrent();
        long pageSize = userQueryRequest.getPageSize();
        Page<User> userPage = userService.page(new Page<>(current, pageSize), userService.getQueryWrapper(userQueryRequest));

        // 打印查询到的 User 实体数据
        System.out.println("查询到的 User 实体数据: ");
        userPage.getRecords().forEach(System.out::println);

        Page<UserVO> userVOPage = new Page<>(current, pageSize, userPage.getTotal());
        List<UserVO> userVOList = userService.getUserVOList(userPage.getRecords());
        userVOPage.setRecords(userVOList);
        return ResultUtils.success(userVOPage);
    }
}
