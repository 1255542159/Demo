package com.example.demo.business.portal.service;

import com.example.demo.base.BaseService;
import com.example.demo.base.ResponseVo;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/19 13:22
 */
public interface PortalService{
    /**
     * 分页获取活动列表
     * @param page
     * @param size
     * @return
     */
    ResponseVo listActivity(int page, int size);

    /**
     * 热门活动
     * @param page
     * @param size
     * @return
     */
    ResponseVo hotActivity(int page, int size);

    /**
     * 查询活动详细信息
     * @param id
     * @return
     */
    ResponseVo getDetails(String id);
}
