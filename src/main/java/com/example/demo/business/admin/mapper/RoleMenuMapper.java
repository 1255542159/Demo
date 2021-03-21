package com.example.demo.business.admin.mapper;

import com.example.demo.business.user.entity.RoleMenu;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/21 13:32
 */
@Mapper
@Repository
public interface RoleMenuMapper {

    int saveRoleMenu(@Param("roleMenuList")List<RoleMenu> roleMenuList);

    int delete(String roleId);
}
