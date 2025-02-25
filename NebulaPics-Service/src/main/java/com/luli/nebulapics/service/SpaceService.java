package com.luli.nebulapics.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.luli.nebulapics.model.dto.space.SpaceAddRequest;
import com.luli.nebulapics.model.dto.space.SpaceQueryRequest;
import com.luli.nebulapics.model.entity.Space;
import com.baomidou.mybatisplus.extension.service.IService;
import com.luli.nebulapics.model.entity.User;
import com.luli.nebulapics.model.vo.SpaceVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author liuleliule
* @description 针对表【space(空间)】的数据库操作Service
* @createDate 2025-01-24 10:18:38
*/
public interface SpaceService extends IService<Space> {


    /**
     * 添加空间
     * @param spaceAddRequest
     * @param loginUser
     * @return
     */
    long addSpace(SpaceAddRequest spaceAddRequest, User loginUser);

    /**
     * 校验空间数据
     * @param space
     * @param add
     */
    void validSpace(Space space, boolean add);

    SpaceVO getSpaceVO(Space space, HttpServletRequest request);

    Page<SpaceVO> getSpaceVOPage(Page<Space> spacePage, HttpServletRequest request);

    QueryWrapper<Space> getQueryWrapper(SpaceQueryRequest spaceQueryRequest);

    /**
     * 填充空间数据
     * @param space
     */
    void fillSpaceBySpaceLevel(Space space);

    /**
     * 校验空间权限
     *
     * @param loginUser
     * @param space
     */
    void checkSpaceAuth(User loginUser, Space space);



}
