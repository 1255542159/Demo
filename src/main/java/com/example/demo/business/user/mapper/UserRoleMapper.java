package com.example.demo.business.user.mapper;

import com.example.demo.business.user.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/21 14:30
 */
@Mapper
@Repository
public interface UserRoleMapper {
    Role findRoleByUserId(String userId);

    int updateUserRole(String userId, String roleId);

}
