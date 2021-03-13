package com.example.demo.business.admin.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Activity;
import com.example.demo.business.admin.service.ActivityService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/10 10:40
 */
@RestController
@Api(value = "活动管理中心",tags = "活动管理中心")
@RequestMapping("/user")
public class ActivityController {
    
    @Autowired
    private ActivityService activityService;
    
    @PostMapping("/post/activity")
    public ResponseVo postActivity(@RequestBody Activity activity){
        return activityService.save(activity);
    }


    @GetMapping("/list/activity")
    public ResponseVo listActivity(@RequestParam("page") int page,
                                   @RequestParam("size") int size,
                                   @RequestParam(value = "keyWords", required = false) String keyWords){
        return activityService.getList(page,size,keyWords);
    }
}
