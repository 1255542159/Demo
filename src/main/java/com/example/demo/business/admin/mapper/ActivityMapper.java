package com.example.demo.business.admin.mapper;


import com.example.demo.base.BaseMapper;
import com.example.demo.business.admin.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

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
    int remove(String id);

    /**
     * 更新活动
     * @param entity
     * @return
     */
    @Override
    int update(Activity entity);

    /**
     * 获取所有的活动
     * @return
     */
    List<Activity> getActivityList(String clubId,String sponsorId,int status);
}
