package com.example.demo.business.user.mapper;

import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.User;
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
public interface UserMapper {

    User findUserByPhone(String s);


    List<Menu> getMenuList(long id);
}
