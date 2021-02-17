package com.example.demo.business.user.mapper;

import com.example.demo.business.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 14:07
 */
@Mapper
public interface RoleMapper {

    List<Role> findRolesByUserId(String userId);
}
