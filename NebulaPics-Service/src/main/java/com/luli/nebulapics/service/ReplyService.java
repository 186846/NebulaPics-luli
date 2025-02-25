package com.luli.nebulapics.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luli.nebulapics.model.dto.reply.ReplyAddRequest;
import com.luli.nebulapics.model.dto.reply.ReplyQueryRequest;
import com.luli.nebulapics.model.entity.Reply;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.ReplyVo;

import javax.servlet.http.HttpServletRequest;

/**
* @author liuleliule
* @description 针对表【reply(回复)】的数据库操作Service
* @createDate 2025-02-17 09:28:52
*/
public interface ReplyService extends IService<Reply> {

    Reply AddReply(ReplyAddRequest replyAddRequest, User loginUser);

    Page<ReplyVo> getReplyVoPage(Page<Reply> replyPage, User loginUser);

    QueryWrapper<Reply> getQueryWrapper(ReplyQueryRequest replyQueryRequest);

    Boolean upDateReply(ReplyVo replyVo, User loginUser);

    Boolean deleteReply(ReplyVo replyVo, User loginUser);
}
