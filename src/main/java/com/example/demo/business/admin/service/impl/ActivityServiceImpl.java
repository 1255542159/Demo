package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;

import com.example.demo.business.admin.entity.Activity;
import com.example.demo.business.admin.mapper.ActivityMapper;
import com.example.demo.business.admin.service.ActivityService;
import com.example.demo.business.user.entity.User;
import com.example.demo.utils.Constants;
import com.example.demo.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/10 11:06
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    SnowflakeIdWorker idWorker;

    @Override
    public ResponseVo save(Activity entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        entity.setId(String.valueOf(idWorker.nextId()));
        entity.setSponsorId(user.getId());
        entity.setCreateTime(new Date());
        entity.setIsDelete(0);
        entity.setStatus(Constants.ActivityStatus.TO_AUDIT);
        int save = activityMapper.save(entity);
        if(save != 1){
            return ResponseVo.FAILURE();
        }
        return ResponseVo.SUCCESS().setMsg("发布成功");
    }

    @Override
    public ResponseVo remove(Integer id) {
        int remove = activityMapper.remove(id);
        if(remove != 1){
            return ResponseVo.FAILURE();
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo update(Activity entity) {
        return null;
    }

    @Override
    public ResponseVo getList(int page, int size, String keyWords) {
        return null;
    }
}
