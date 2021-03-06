package com.example.demo.business.user.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 13:41
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("/userInfo")
    public ResponseVo getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Object principal = authentication.getPrincipal();
        return ResponseVo.SUCCESS().setData(principal);
    }

    /**
     * 根据当前角色去获取对应菜单
     *
     * @return
     */
    @GetMapping("/getMenu")
    public ResponseVo getMenuList(HttpServletRequest request, HttpServletResponse response) {
        return userService.getMenuList(request, response);
    }

    /**
     * 图片上传
     *
     * @param file
     * @return
     */
    @PostMapping("/uploadImage")
    public ResponseVo uploadImage(@RequestParam("file") MultipartFile file) {
        return userService.uploadImage(file);
    }


}
