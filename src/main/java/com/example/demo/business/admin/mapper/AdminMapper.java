package com.example.demo.business.admin.mapper;

import com.example.demo.business.admin.entity.Settings;
import com.example.demo.business.user.entity.Image;
import com.example.demo.business.user.entity.Role;
import com.example.demo.business.user.entity.RoleMenu;
import com.example.demo.business.user.entity.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2020/10/3 16:58
 */
@Repository
@Mapper
public interface AdminMapper {

    int getUserCount();

    int getClubCount();

    List<Role> roleList();

    List<UserVo> getList();

    int updateCarousel(Image image);

    int deleteCarousel(String id);

    int getActivityCount();

    HashMap<String, Object> loginLog(String id);


}
