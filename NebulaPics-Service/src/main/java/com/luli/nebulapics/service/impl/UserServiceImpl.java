package com.luli.nebulapics.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.luli.nebulapics.constant.UserConstant;
import com.luli.nebulapics.manager.auth.StpKit;
import com.luli.nebulapics.model.dto.user.UserQueryRequest;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.exception.BusinessException;
import com.luli.nebulapics.exception.ErrorCode;
import com.luli.nebulapics.model.enums.UserRoleEnum;
import com.luli.nebulapics.model.vo.LoginUserVO;
import com.luli.nebulapics.model.vo.UserVO;
import com.luli.nebulapics.service.UserService;
import com.luli.nebulapics.mapper.UserMapper;
import com.luli.nebulapics.utils.RedisVerificationCodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.luli.nebulapics.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author liuleliule
* @description 针对表【user(用户)】的数据库操作Service实现
* @createDate 2025-01-02 20:56:24
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService{

    @Resource
    private RedisVerificationCodeService redisVerificationCodeService;

    /**
     * 用户注册
     * @param userAccount   用户账户
     * @param userPassword  用户密码
     * @param checkPassword 校验密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String code,String userPassword, String checkPassword) {
        // 1. 校验
        if (StrUtil.hasBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (code.length() != 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"code长度不为6");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        if (!Objects.equals(redisVerificationCodeService.getVerificationCode(userAccount), code)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"验证码错误");
        }
        // 2. 检查是否重复
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        long count = this.baseMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
        }
        redisVerificationCodeService.deleteVerificationCode(userAccount);
        // 3. 加密
        String encryptPassword = getEncryptPassword(userPassword);
        // 4. 插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName("用户");
        user.setUserAvatar("https://tse4-mm.cn.bing.net/th/id/OIP-C.MqovI15z6O3xqrbcjHUm4gAAAA?rs=1&pid=ImgDetMain");
        user.setUserRole(UserRoleEnum.USER.getValue());
        System.out.println(user);
        boolean saveResult = this.save(user);
        if (!saveResult) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
        }
        return user.getId();
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, String captcha, HttpServletRequest request) {
        String loginType = " ";
        // 1. 通用校验
        if (StrUtil.hasBlank(userAccount)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号不能为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (StrUtil.hasBlank(captcha)) {
            loginType = "password";
        }
        if (StrUtil.hasBlank(userPassword)) {
            loginType = "captcha";
        }

        User user = null;
        if ("password".equals(loginType)) {
            // 密码登录校验
            if (StrUtil.hasBlank(userPassword)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码不能为空");
            }
            if (userPassword.length() < 8) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
            }
            // 加密
            String encryptPassword = getEncryptPassword(userPassword);
            // 查询用户是否存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            queryWrapper.eq("userPassword", encryptPassword);
            user = this.baseMapper.selectOne(queryWrapper);

            // 用户不存在
            if (user == null) {
                log.info("user login failed, userAccount cannot match userPassword");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
            }
        } else if ("captcha".equals(loginType)) {
            // 邮箱验证码登录校验
            if (StrUtil.hasBlank(captcha)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码不能为空");
            }
            if (!Objects.equals(redisVerificationCodeService.getVerificationCode(userAccount), captcha)) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "验证码错误");
            }
            // 查询用户是否存在
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            user = this.baseMapper.selectOne(queryWrapper);

            // 用户不存在
            if (user == null) {
                log.info("user login failed, userAccount not found");
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
            }
        } else {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "不支持的登录方式");
        }

        // 3. 记录用户的登录态
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATE, user);
        // 记录用户登录态到 Sa-token，便于空间鉴权时使用，注意保证该用户信息与 SpringSession 中的信息过期时间一致
        StpKit.SPACE.login(user.getId());
        StpKit.SPACE.getSession().set(UserConstant.USER_LOGIN_STATE, user);

        System.out.println("user:" + request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE));
        return this.getLoginUserVO(user);
    }

    /**
     * 获取加密后的密码
     *
     * @param userPassword 用户密码
     * @return 加密后的密码
     */
    @Override
    public String getEncryptPassword(String userPassword) {
        // 加盐，混淆密码
        final String SALT = "luli";
        return DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        // 从数据库中查询（追求性能的话可以注释，直接返回上述结果）
        Long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return currentUser;
    }

    /**
     * 获取脱敏类的用户信息
     *
     * @param user 用户
     * @return 脱敏后的用户信息
     */
    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }


    /**
     * 用户注销
     * @param request
     * @return
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 判断是否已经登录
        Object userObj = request.getSession().getAttribute(UserConstant.USER_LOGIN_STATE);
        if (userObj == null) {
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    /**
     * 获得脱敏后的用户信息
     *
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        // 将user转换为userVo
        BeanUtil.copyProperties(user, userVO);
        // 这里可以进行脱敏操作
//        if (userVO.getUserAccount()!= null && userVO.getUserAccount().length() >= 7) {
//            String phone = userVO.getUserAccount();
//            String desensitizedPhone = phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
//            userVO.setUserAccount(desensitizedPhone);
//        }
        return userVO;
    }

    /**
     * 获取脱敏后的用户列表
     *
     * @param userList
     * @return
     */
    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream()
                .map(this::getUserVO)
                .collect(Collectors.toList());
    }


    /**
     * 用户分页查询
     * @param userQueryRequest
     * @return
     */
    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Long id = userQueryRequest.getId();
        String userName = userQueryRequest.getUserName();
        String userAccount = userQueryRequest.getUserAccount();
        String userProfile = userQueryRequest.getUserProfile();
        String userRole = userQueryRequest.getUserRole();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(ObjUtil.isNotNull(id), "id", id);
        queryWrapper.eq(StrUtil.isNotBlank(userRole), "userRole", userRole);
        queryWrapper.like(StrUtil.isNotBlank(userAccount), "userAccount", userAccount);
        queryWrapper.like(StrUtil.isNotBlank(userName), "userName", userName);
        queryWrapper.like(StrUtil.isNotBlank(userProfile), "userProfile", userProfile);
        queryWrapper.orderBy(StrUtil.isNotEmpty(sortField), sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getUserRole());
    }
}




