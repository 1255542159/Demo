package com.example.demo.business.admin.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.service.impl.AdminServiceImpl;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/9 20:18
 */
@RestController
@Api(value = "管理员接口",tags = "管理员接口")
@RequestMapping("/admin")
public class AdminController{
    @Autowired
    private AdminServiceImpl adminService;
    /**
     * 获取注册用户数
     *
     * @return
     */
    @GetMapping("/web_site_info/register_count")
    public ResponseVo getUserCount() {
        return adminService.getUserCount();
    }

    /**
     * 获取社团总数
     *
     * @return
     */
    @GetMapping("/web_site_info/club_count")
    public ResponseVo getClubCount() {

        return adminService.getClubCount();
    }

}
