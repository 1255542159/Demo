package com.example.demo.business.portal.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.admin.service.ActivityService;
import com.example.demo.business.admin.service.ClubService;
import com.example.demo.business.portal.service.PortalService;
import com.example.demo.business.user.service.UserService;
import com.example.demo.utils.Constants;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/19 0:20
 */
@RestController
@Api(value = "门户接口",tags = "门户接口")
@RequestMapping("/portal")
public class PortalController {

    @Autowired
    private UserService userService;

    @Autowired
    private PortalService portalService;


    /**
     *轮播图获取
     * @return
     */
    @GetMapping("/list/carousel")
    public ResponseVo list() {
        return userService.getImageList(0,0,"carousel");
    }
    @GetMapping("/list/activity/{page}/{size}")
    public ResponseVo listActivity(@PathVariable(value = "page") int page,
                                   @PathVariable(value = "size") int size){
        return portalService.listActivity(page,size);
    }

    @GetMapping("/hot/activity")
    public ResponseVo hotActivity(){
        return portalService.hotActivity(1,10);
    }

    @GetMapping("/activity/{id}")
    public ResponseVo getActivityDetails(@PathVariable(value = "id") String id){
        return portalService.getDetails(id);
    }
}
