package com.example.demo.business.activity.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.activity.entity.Activity;
import com.example.demo.business.activity.mapper.ActivityMapper;
import com.example.demo.business.activity.service.ActivityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public ResponseVo save(Activity entity) {
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
