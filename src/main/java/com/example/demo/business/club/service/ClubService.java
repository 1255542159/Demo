package com.example.demo.business.club.service;


import com.example.demo.base.BaseService;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.club.entity.Club;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:42
 */
public interface ClubService extends BaseService<Club> {
    /**
     * 分页查询
     * @param page
     * @param size
     * @param keyWords
     * @return
     */
    ResponseVo getClubList(int page, int size, String keyWords);
}
