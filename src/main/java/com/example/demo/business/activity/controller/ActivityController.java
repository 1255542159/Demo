package com.example.demo.business.activity.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.activity.entity.Activity;
import com.example.demo.business.activity.service.ActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/10 10:40
 */
@RestController
@Api(value = "活动管理中心",tags = "活动管理中心")
@RequestMapping("/user")
public class ActivityController{
    
    @Autowired
    private ActivityService activityService;
    
    @PostMapping("/post/activity")
    public ResponseVo postActivity(@RequestBody Activity activity){
        return activityService.save(activity);
    }
}
