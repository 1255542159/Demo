package com.example.demo.utils;

import com.example.demo.business.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/13 13:21
 */
public class Tools {

    public static User getCurrentUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }
}
