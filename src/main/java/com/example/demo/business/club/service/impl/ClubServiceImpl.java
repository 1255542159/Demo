package com.example.demo.business.club.service.impl;

import com.example.demo.base.BaseServiceImpl;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.club.entity.Club;
import com.example.demo.business.club.mapper.ClubMapper;
import com.example.demo.business.club.service.ClubService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:53
 */
@Service
public class ClubServiceImpl extends BaseServiceImpl<Club> implements ClubService {

    @Autowired
    private ClubMapper clubMapper;


    @Override
    public ResponseVo getClubList(int page, int size, String keyWords) {
        PageHelper.startPage(page,size);
        List<Club> all = clubMapper.findAll();
        PageInfo<Club> clubPageInfo = new PageInfo<>(all);
        return ResponseVo.SUCCESS().setData(clubPageInfo);
    }
}
