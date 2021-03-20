package com.example.demo.utils;
import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import java.util.ArrayList;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/13 13:21
 */
public class Tools {
    //获取当前用户
    public static User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        return user;
    }

    //返回菜单权限
    public static List<Menu> getMenuList(List<Menu> menuList){
        //对菜单进行分离
        List<Menu> data = new ArrayList<>();
        for (Menu menu : menuList) {
            //找到父级id
            if (menu.getParentId().equals("0")) {
                data.add(menu);
            }
        }
        //找寻二级菜单
        for (Menu menu : data) {
            menu.setChildMenu(getChild(menu.getId(), menuList));
        }
        return data;
    }

    private static List<Menu> getChild(String id, List<Menu> menuList) {
        //子菜单
        List<Menu> childMenu = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId().equals(id)) {
                childMenu.add(menu);
            }
        }
        return childMenu;
    }
}
