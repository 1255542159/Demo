package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.mapper.AdminMapper;
import com.example.demo.business.admin.mapper.RoleMenuMapper;
import com.example.demo.business.admin.service.AdminService;
import com.example.demo.business.user.entity.*;
import com.example.demo.business.user.mapper.UserMapper;
import com.example.demo.business.user.mapper.UserRoleMapper;
import com.example.demo.utils.SnowflakeIdWorker;
import com.example.demo.utils.Tools;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.tools.Tool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2020/10/3 16:56
 */
@Slf4j
@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private RoleMenuMapper roleMenuMapper;

    @Autowired
    private SnowflakeIdWorker idWorker;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public ResponseVo getUserCount() {
        int userCount = adminMapper.getUserCount();
        return ResponseVo.SUCCESS().setData(userCount);
    }

    @Override
    public ResponseVo getClubCount() {
        int clubCount = adminMapper.getClubCount();
        return ResponseVo.SUCCESS().setData(clubCount);
    }

    @Override
    public ResponseVo roleList() {
        List<Role> roleList = adminMapper.roleList();
        for (Role role : roleList) {
            List<Menu> menuList = userMapper.getMenuList(role.getId());
            List<Menu> data = Tools.getMenuList(menuList);
            if (data != null) {
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

    @Override
    @Transactional
    public ResponseVo manageRole(String roleId, String idStr) {
        //将权限id拆分
        String[] split = idStr.split(",");
        List<RoleMenu> roleMenus = new ArrayList<>();
        //补全数据
        for (int i = 0; i < split.length; i++) {
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setId(String.valueOf(idWorker.nextId()));
            roleMenu.setRoleId(roleId);
            roleMenu.setMenuId(split[i]);
            roleMenus.add(roleMenu);
        }
        //在创建前先将原来的清空
        boolean delete = roleMenuMapper.delete(roleId) == 1;
//        if (!delete) {
//            log.error("deleteRoleMenu: delete roleMenu failed data={}", roleId);
//            return ResponseVo.FAILURE().setMsg("删除失败");
//        }
        int success = roleMenuMapper.saveRoleMenu(roleMenus);
//        if (success != 1) {
//            log.error("saveRoleMenu: save roleMenu failed data={}", roleMenus);
//            return ResponseVo.FAILURE().setMsg("保存失败");
//        }

        return ResponseVo.SUCCESS().setMsg("分配成功");
    }

    @Override
    public ResponseVo getList(int page, int size, Integer status, String keyWords) {

        PageHelper.startPage(page, size);
        //查询所有的成员包括自己
        List<UserVo> userVoList = adminMapper.getList();
        PageInfo<UserVo> userPageInfo = new PageInfo<>(userVoList);
        return ResponseVo.SUCCESS().setData(userPageInfo);
    }

    @Override
    public ResponseVo issueRole(String userId, String roleId) {
        boolean success = userRoleMapper.updateUserRole(userId, roleId) == 1;
        if (!success) {
            log.error("updateCarousel: update carousel failed data={}", userId, roleId);
            return ResponseVo.FAILURE().setMsg("分配失败");
        }
        return ResponseVo.SUCCESS().setMsg("分配成功");
    }

    @Override
    public ResponseVo updateCarousel(Image image) {
        boolean success = adminMapper.updateCarousel(image) == 1;
        if (!success) {
            log.error("updateCarousel: update carousel failed data={}", image);
            return ResponseVo.FAILURE().setMsg("修改失败");
        }
        return ResponseVo.SUCCESS().setMsg("修改成功");
    }

    @Override
    public ResponseVo deleteCarousel(String id) {
        boolean success = adminMapper.deleteCarousel(id) == 1;
        if (!success) {
            log.error("deleteCarousel: delete carousel failed data={}", id);
            return ResponseVo.FAILURE().setMsg("修改失败");
        }
        return ResponseVo.SUCCESS().setMsg("修改成功");
    }

    @Override
    public ResponseVo getActivityCount() {
        int activityCount = adminMapper.getActivityCount();
        return ResponseVo.SUCCESS().setData(activityCount);
    }

    @Override
    public ResponseVo loginLog() {
        User currentUser = Tools.getCurrentUser();
        HashMap<String, Object> data = adminMapper.loginLog(currentUser.getId());
        return ResponseVo.SUCCESS().setData(data);
    }

}
