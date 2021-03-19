package com.example.demo.business.portal.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Activity;
import com.example.demo.business.admin.mapper.ActivityMapper;
import com.example.demo.business.portal.mapper.PortalMapper;
import com.example.demo.business.portal.service.PortalService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/19 13:24
 */
@Service
public class PortalServiceImpl implements PortalService {
    @Autowired
    private PortalMapper portalMapper;
    @Override
    public ResponseVo listActivity(int page, int size) {
        PageHelper.startPage(page, size);
        List<HashMap<String, Object>> activityList = portalMapper.getActivityList(null, null, 1);
        PageInfo<Activity> activityPageInfo = new PageInfo(activityList);
        return ResponseVo.SUCCESS().setData(activityPageInfo);
    }

    @Override
    public ResponseVo hotActivity(int page, int size) {

        return null;
    }

    @Override
    public ResponseVo getDetails(String id) {
        try {
            HashMap<String,Object> data = portalMapper.getDetails(id);
            return ResponseVo.SUCCESS().setData(data);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseVo.FAILURE();
        }

    }
}
