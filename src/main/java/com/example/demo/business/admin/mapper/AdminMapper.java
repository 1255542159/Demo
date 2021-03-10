package com.example.demo.business.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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

}
