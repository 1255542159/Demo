package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Activity;
import com.example.demo.business.admin.mapper.ActivityMapper;
import com.example.demo.business.admin.service.ActivityService;
import com.example.demo.business.user.entity.User;
import com.example.demo.utils.Constants;
import com.example.demo.utils.SnowflakeIdWorker;
import com.example.demo.utils.Tools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/10 11:06
 */
@Service
@Slf4j
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    SnowflakeIdWorker idWorker;

    @Override
    public ResponseVo save(Activity entity) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        entity.setId(String.valueOf(idWorker.nextId()));
        entity.setSponsorId(user.getId());
        entity.setCreateTime(new Date());
        entity.setIsDelete(0);
        entity.setStatus(Constants.ActivityStatus.TO_AUDIT);
        int save = activityMapper.save(entity);
        if(save != 1){
            return ResponseVo.FAILURE();
        }
        return ResponseVo.SUCCESS().setMsg("发布成功");
    }

    @Override
    public ResponseVo remove(Integer id) {
        int remove = activityMapper.remove(id);
        if(remove != 1){
            return ResponseVo.FAILURE();
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo update(Activity entity) {
        return null;
    }

    @Override
    public ResponseVo getList(int page, int size, String keyWords) {
        try {
            User currentUser = Tools.getCurrentUser();
            PageHelper.startPage(page, size);
            List<Activity> all = null;
            if (currentUser.getRoles().getRoleName().equals(Constants.Role.ROLE_ADMIN)){
                //如果是超级管理员，则查看所有
                all = activityMapper.getActivityList(null,null);
            }else if(currentUser.getRoles().getRoleName().equals(Constants.Role.ROLE_CLUB)){
                //如果是社团管理员 查看当前社团下的所有活动申请
                    all = activityMapper.getActivityList(currentUser.getClubId(),null);
            }else {
                //如果是普通用户 查看自己提交的活动
                all = activityMapper.getActivityList(null,currentUser.getId());
            }
            PageInfo<Activity> activityPageInfo = new PageInfo<>(all);
            return ResponseVo.SUCCESS().setData(activityPageInfo);
        }catch (Exception e){
            return ResponseVo.FAILURE().setMsg("获取失败");
        }
    }
}
