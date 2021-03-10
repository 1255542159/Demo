package com.example.demo.business.activity.mapper;

import com.example.demo.base.BaseMapper;
import com.example.demo.business.activity.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/10 11:05
 */
@Mapper
@Repository
public interface ActivityMapper extends BaseMapper<Activity> {

    /**
     * 发布活动
     * @param entity
     * @return
     */
    @Override
    int save(Activity entity);

    /**
     * 删除活动
     * @param id
     * @return
     */
    @Override
    int remove(Integer id);
}
