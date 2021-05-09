package com.example.demo.business.admin.service.impl;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.admin.mapper.ClubMapper;
import com.example.demo.business.admin.service.ClubService;
import com.example.demo.business.user.entity.Audit;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.mapper.AuditMapper;
import com.example.demo.business.user.mapper.UserMapper;
import com.example.demo.business.user.mapper.UserRoleMapper;
import com.example.demo.utils.Constants;
import com.example.demo.utils.SnowflakeIdWorker;
import com.example.demo.utils.Tools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.tools.Tool;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author joy
 * @version 1.0
 * @date 2021/1/2 16:53
 */
@Slf4j
@Service
public class ClubServiceImpl implements ClubService {

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private SnowflakeIdWorker idWorker;

    @Autowired
    private AuditMapper auditMapper;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private UserRoleMapper userRoleMapper;

    @Override
    public ResponseVo getList(int page, int size, int status, String keyWords) {
        try {
            User currentUser = Tools.getCurrentUser();
            PageHelper.startPage(page, size);
            List<Club> all = null;
            if (currentUser.getRoles().getRoleName().equals(Constants.Role.ROLE_ADMIN)) {
                //如果是超级管理员，则查看所有
                all = clubMapper.getClubList(null,  status);
            } else{
                //(currentUser.getRoles().getRoleName().equals(Constants.Role.ROLE_CLUB))
                //如果是社团管理员 查看当前社团下的所有活动申请
                all = clubMapper.getClubList(currentUser.getId(), status);
            }
            PageInfo<Club> activityPageInfo = new PageInfo<>(all);
            return ResponseVo.SUCCESS().setData(activityPageInfo);
        } catch (Exception e) {
            return ResponseVo.FAILURE().setMsg("获取失败");
        }
    }

    @Override
    public ResponseVo save(Club entity) {
        User currentUser = Tools.getCurrentUser();
        //判断该用户是否已经提交了创建
        Club cl = clubMapper.getClubByUserId(currentUser.getId());
        if(!Objects.isNull(cl)){
            return ResponseVo.FAILURE().setMsg("你已提交申请");
        }
        //判断当前社团是否已存在
        Club club = clubMapper.findClubByName(entity.getClubName());
        if(entity.getId() == null){
            entity.setId("");
        }
        if(!entity.getId().equals("")){
            entity.setUpdateTime(new Date());
            entity.setStatus(Constants.ActivityStatus.TO_AUDIT);
           boolean success  = clubMapper.update(entity) == 1;
            if (!success) {
                log.error("clubUpdate: update club failed data={}",entity);
                return ResponseVo.FAILURE().setMsg("更新失败");
            }
            return ResponseVo.SUCCESS().setMsg("更新成功");
        } else {
            if(!Objects.isNull(club)){
                return ResponseVo.FAILURE().setMsg("该社团已存在");
            }

            entity.setLeaderId(currentUser.getId());
            entity.setClubCreator(currentUser.getName());
            entity.setId(String.valueOf(idWorker.nextId()));
            entity.setStatus(Constants.ActivityStatus.TO_AUDIT);
            entity.setIsDelete(0);
            entity.setCreateTime(new Date());
            boolean success = clubMapper.save(entity) == 1;
            if (!success) {
                log.error("clubSave: save club failed data={}",entity);
                return ResponseVo.FAILURE().setMsg("保存失败");
            }
        }
        return ResponseVo.SUCCESS().setMsg("保存成功");
    }

    @Override
    public ResponseVo remove(String id) {
        boolean success = clubMapper.remove(id) == 1;
        if (!success) {
            log.error("clubRemove: remove club failed data={}",id);
            return ResponseVo.FAILURE().setMsg("删除失败");
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo update(Club entity) {
        User user = new User();
        user.setId(entity.getLeaderId());
        user.setClubId(entity.getId());
        boolean success;
        success = clubMapper.update(entity) == 1;
        //如果审核通过将用户的角色变更为社团管理员
        if(entity.getStatus() == 1){
            userRoleMapper.updateUserRole(entity.getLeaderId(),Constants.RoleId.ROLE_CLUB);
        }
        success = success && userMapper.update(user) == 1;
        if (!success) {
            log.error("clubUpdate: update club failed data={}",entity);
            return ResponseVo.FAILURE().setMsg("更新失败");
        }
        return ResponseVo.SUCCESS().setMsg("更新成功");
    }

    @Override
    public ResponseVo list() {
        List<Club> all = clubMapper.findAll();
        return ResponseVo.SUCCESS().setData(all);
    }

    @Override
    public ResponseVo getAuditList(int page, int size, String type) {
        try {
            User currentUser = Tools.getCurrentUser();
            PageHelper.startPage(page, size);
            List<Audit> all = null;
            if (currentUser.getRoles().getRoleName().equals(Constants.Role.ROLE_ADMIN)) {
                //如果是超级管理员，则查看所有
                all = auditMapper.getAuditList(null,  type);
            } else{
                //(currentUser.getRoles().getRoleName().equals(Constants.Role.ROLE_CLUB))
                //如果是社团管理员 查看当前社团下的所有申请
                all = auditMapper.getAuditList(currentUser.getId(), type);
            }
            PageInfo<Audit> activityPageInfo = new PageInfo<>(all);
            return ResponseVo.SUCCESS().setData(activityPageInfo);
        } catch (Exception e) {
            return ResponseVo.FAILURE().setMsg("获取失败");
        }
    }

    @Override
    public ResponseVo agreeAudit(Audit audit) {
        //如果是入团和退团申请，额外的需要更新用户信息
        if(audit.getType().equals("quit")){
            User user = new User();
            user.setId(audit.getUserId());
            user.setClubId("");
            userMapper.update(user);
        }else if (audit.getType().equals("join")){
            User user = new User();
            user.setId(audit.getUserId());
            user.setClubId(audit.getClubId());
            userMapper.update(user);
        }
        boolean success = auditMapper.update(audit) == 1;
        if (!success) {
            log.error("auditUpdate: update audit failed data={}",audit);
            return ResponseVo.FAILURE().setMsg("审核失败");
        }
        return ResponseVo.SUCCESS().setMsg("审核成功");
    }
}
