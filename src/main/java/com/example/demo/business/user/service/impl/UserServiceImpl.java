package com.example.demo.business.user.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.user.service.UserService;
import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 11:54
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.findUserByPhone(s);
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    @Override
    public ResponseVo getMenuList(HttpServletRequest request, HttpServletResponse response) {
        //根据角色获取用户菜单
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Menu> menuList = userMapper.getMenuList(user.getRoles().getId());
        //对菜单进行分离
        List<Menu> data = new ArrayList<>();
        for (Menu menu : menuList) {
            //找到父级id
            if (menu.getParentId() == new Long(0)) {
                data.add(menu);
            }
        }
        //找寻二级菜单
        for (Menu menu : data) {
            menu.setChildMenu(getChild(menu.getId(), menuList));
        }
        return ResponseVo.SUCCESS().setData(data);
    }

    private List<Menu> getChild(long id, List<Menu> menuList) {
        //子菜单
        List<Menu> childMenu = new ArrayList<>();
        for (Menu menu : menuList) {
                if (menu.getParentId() == id) {
                    childMenu.add(menu);
                }
        }
        return childMenu;
    }
}
