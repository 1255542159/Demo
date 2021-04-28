package com.example.demo.business.admin.mapper;


import com.example.demo.base.BaseMapper;
import com.example.demo.business.admin.entity.Activity;
import com.example.demo.business.admin.entity.Club;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/3 14:47
 */
@Repository
@Mapper
public interface ClubMapper extends BaseMapper<Club> {
    /**
     * 保存社团
     * @param entity
     * @return
     */
    @Override
    int save(Club entity);


    /**
     * 删除社团
     * @param id
     * @return
     */
    @Override
    int remove(String id);

    /**
     * 更新社团
     * @param entity
     * @return
     */
    @Override
    int update(@Param("club") Club entity);

    /**
     * 通过社团id查找社团
     * @param clubId
     * @return
     */
    Club findClubById(Integer clubId);

    /**
     * 查询所有社团
     * @return
     */
    List<Club> getClubList(String leaderId,int status);

    Club findClubByName(String clubName);


    @Override
    List<Club> findAll();
}
