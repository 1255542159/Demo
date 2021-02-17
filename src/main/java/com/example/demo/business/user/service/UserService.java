package com.example.demo.business.user.service;

import com.example.demo.base.ResponseVo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 11:54
 */
public interface UserService extends UserDetailsService {


    /**
     * 通过手机号获取用户信息
     * @param phone
     * @return
     */
    @Override
    UserDetails loadUserByUsername(String phone);

    /**
     * 获取用户菜单
     * @param request
     * @param response
     * @return
     */
    ResponseVo getMenuList(HttpServletRequest request, HttpServletResponse response);
}
