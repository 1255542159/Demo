package com.example.demo.business.user.mapper;

import com.example.demo.base.BaseMapper;
import com.example.demo.business.club.entity.Club;
import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.entity.UserVo;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 13:33
 */
@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {

    /**
     * 通过手机号查询用户
     * @param s
     * @return
     */
    User findUserByPhone(String s);


    /**
     * 获取菜单
     * @param id
     * @return
     */
    List<Menu> getMenuList(long id);

    /**
     * 获取列表
     * @return
     */
    @Override
    List<User> findAll();


    /**
     * 通过用户id查询负责人下所有人
     * @param userId
     * @return
     */
    List<UserVo> getListByClubId(Long userId);

    /**
     * 更新用户信息
     * @param entity
     * @return
     */
    @Override
    int update(User entity);
}
