package com.example.demo.business.user.mapper;

import com.example.demo.base.BaseEntity;
import com.example.demo.base.BaseMapper;
import com.example.demo.business.user.entity.UserActivity;
import lombok.Data;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * @author Joy
 */
@Mapper
@Repository
public interface UserActivityMapper extends BaseMapper<UserActivity> {

    @Override
    int save(UserActivity userActivity);

    UserActivity findById(String userId,String activityId);
}
