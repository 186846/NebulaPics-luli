package com.luli.nebulapics.utils;

import cn.hutool.core.util.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class MailSendCode {
    // 这个是 mail 依赖提供给我们的发送邮件的接口
    @Resource
    private JavaMailSender mailSender;

    // 获取发件人邮箱
    @Value("${spring.mail.username}")
    private String sender;

    // 获取发件人昵称
    @Value("${spring.mail.nickname}")
    private String nickname;

    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";

    public boolean SendMail(String email, String code){
        try {
            // 创建一个简单的邮件消息对象
            SimpleMailMessage message = new SimpleMailMessage();
            // 设置发件人，格式为 昵称 <邮箱地址>
            message.setFrom(nickname + "<" + sender + ">");
            // 设置收件人邮箱
            message.setTo(email);
            // 设置邮件主题
            message.setSubject("邮箱登录验证码");
            // 构建邮件内容，包含验证码和有效期信息
            String content = "【验证码】您的验证码为：" + code + " 。 验证码五分钟内有效，逾期作废。";
            // 设置邮件文本内容
            message.setText(content);
            // 发送邮件
            mailSender.send(message);
            // 邮件发送成功，返回 true
            return true;
        } catch (Exception e) {
            // 打印异常信息，方便调试
            e.printStackTrace();
            // 邮件发送失败，返回 false
            return false;
        }
    }

    public boolean isValidEmail(String email){
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        if(email == null) return false;

        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
