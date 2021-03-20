package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.mapper.AdminMapper;
import com.example.demo.business.admin.service.AdminService;
import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.Role;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.mapper.UserMapper;
import com.example.demo.utils.Tools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2020/10/3 16:56
 */
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Override
    public ResponseVo getUserCount() {
        try {
            int userCount = adminMapper.getUserCount();
            return ResponseVo.SUCCESS().setData(userCount);
        }catch (Exception e){
            return ResponseVo.FAILURE();
        }
    }

    @Override
    public ResponseVo getClubCount() {
        try {
            int clubCount = adminMapper.getClubCount();
            return ResponseVo.SUCCESS().setData(clubCount);
        }catch (Exception e){
            return ResponseVo.FAILURE();
        }
    }

    @Override
    public ResponseVo roleList() {
        List<Role> roleList = adminMapper.roleList();
        for (Role role:roleList) {
            List<Menu> menuList = userMapper.getMenuList(role.getId());
            List<Menu> data = Tools.getMenuList(menuList);
            if(data != null){
                role.setChildMenu(data);
            }
        }
        return ResponseVo.SUCCESS().setData(roleList);
    }

    @Override
    public ResponseVo powerList(String id) {
        //根据角色获取用户菜单
        List<Menu> menuList = userMapper.getMenuList(id);
        List<Menu> data = Tools.getMenuList(menuList);
        return ResponseVo.SUCCESS().setData(data);
    }
}
