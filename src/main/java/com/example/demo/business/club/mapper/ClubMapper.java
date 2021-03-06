package com.example.demo.business.club.mapper;

import com.example.demo.base.BaseMapper;
import com.example.demo.business.club.entity.Club;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

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
    int remove(Integer id);

    /**
     * 更新社团
     * @param entity
     * @return
     */
    @Override
    int update(Club entity);

    /**
     * 通过社团id查找社团
     * @param clubId
     * @return
     */
    Club findClubById(Integer clubId);
}
