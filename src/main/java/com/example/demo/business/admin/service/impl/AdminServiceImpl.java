package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.mapper.AdminMapper;
import com.example.demo.business.admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author joy
 * @version 1.0
 * @date 2020/10/3 16:56
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Override
    public ResponseVo getUserCount() {
        try {
            int userCount = adminMapper.getUserCount();
            return ResponseVo.SUCCESS().setData(userCount);
        }catch (Exception e){
            return ResponseVo.FAILURE();
        }
    }

    @Override
    public ResponseVo getClubCount() {
        try {
            int clubCount = adminMapper.getClubCount();
            return ResponseVo.SUCCESS().setData(clubCount);
        }catch (Exception e){
            return ResponseVo.FAILURE();
        }
    }
}
