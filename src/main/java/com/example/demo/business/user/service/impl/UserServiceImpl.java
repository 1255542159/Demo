package com.example.demo.business.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Activity;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.admin.mapper.ActivityMapper;
import com.example.demo.business.admin.mapper.ClubMapper;
import com.example.demo.business.user.entity.*;
import com.example.demo.business.user.mapper.*;
import com.example.demo.business.user.service.UserService;
import com.example.demo.utils.Constants;
import com.example.demo.utils.SnowflakeIdWorker;
import com.example.demo.utils.Tools;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.tools.Tool;
import java.io.IOException;
import java.util.*;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 11:54
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
    private ClubMapper clubMapper;
    @Autowired
    private SnowflakeIdWorker idWorker;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private ActivityMapper activityMapper;

    @Autowired
    private UserActivityMapper userActivityMapper;
    @Autowired
    private AuditMapper auditMapper;

    /**
     * 设置好账号的ACCESS_KEY和SECRET_KEY
     */
    @Value("${qiniu.accessKey}")
    String ACCESS_KEY;
    @Value("${qiniu.secretKey}")
    String SECRET_KEY;
    @Value("${qiniu.buckName}")
    String bucketname;

    /**
     * 密钥配置
     *
     * @return
     */
    private Auth getAuth() {
        return Auth.create(ACCESS_KEY, SECRET_KEY);
    }

    /**
     * 构造一个带指定Zone对象的配置类,不同的七云牛存储区域调用不同的zone
     */
    Configuration cfg = new Configuration(Zone.zone2());

    /**
     * 其他参数参考类注释
     *
     * @return
     */
    private UploadManager getUploadManager() {
        return new UploadManager(cfg);
    }

    /**
     * 测试域名，只有30天有效期
     */
    @Value("${qiniu.qiniuDomin}")
    private String QINIU_IMAGE_DOMAIN;

    /**
     * 简单上传，使用默认策略，只需要设置上传的空间名就可以了
     */
    public String getUpToken() {
        return getAuth().uploadToken(bucketname);
    }

    @Override
    public UserDetails loadUserByUsername(String s){
        User user = userMapper.findUserByPhone(s);
        try{
            if (Objects.isNull(user)) {
                //捕获异常
                return null;
            }
        }catch (UsernameNotFoundException e){
            throw new UsernameNotFoundException("用户不存在");
        }
        return user;
    }

    @Override
    public ResponseVo getMenuList(HttpServletRequest request, HttpServletResponse response) {
        //根据角色获取用户菜单
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Menu> menuList = userMapper.getMenuList(user.getRoles().getId());
        //对菜单进行分离
        List<Menu> data = new ArrayList<>();
        for (Menu menu : menuList) {
            //找到父级id
            if (menu.getParentId().equals("0")) {
                data.add(menu);
            }
        }
        //找寻二级菜单
        for (Menu menu : data) {
            menu.setChildMenu(getChild(menu.getId(), menuList));
        }
        return ResponseVo.SUCCESS().setData(data);
    }

    @Override
    public ResponseVo uploadImage(MultipartFile file, String original) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        /**
         * 1.校验file是否存在
         * 2.校验图片格式 jpg,png,gif,psd
         * 3.文件上传
         * a:图片格式不变，命名
         * b:七牛云认证参数信息
         */
        if (file == null) {
            return ResponseVo.FAILURE().setMsg("图片文件不存在");
        }
        long size = file.getSize();
        //图片大小不能超过5MB
        if (size >= 2097152) {
            return ResponseVo.FAILURE().setMsg("图片过大");
        }
        //获取原始文件名
        String originalFilename = file.getOriginalFilename();
        //获取文件后缀
        String type = FilenameUtils.getExtension(originalFilename);
        if (StringUtils.isEmpty(type)) {
            return ResponseVo.FAILURE().setMsg("图片类型为空，请检查后重试！");
        }
        if ("JPG".equals(type.toUpperCase()) || "PNG".equals(type.toUpperCase())
                || "GIF".equals(type.toUpperCase()) || "PSD".equals(type.toUpperCase())) {
            //图片格式正确
            //对图片重命名
            String fileName = System.currentTimeMillis() + "." + type;
            try {
                //将图片上传到七牛云服务器
                Response response = getUploadManager().put(file.getBytes(), fileName, getUpToken());
                //判断状态
                if (response.isOK() && response.isJson()) {
                    //表示上传成功
                    //获取七牛云图片地址
                    Object key = JSONObject.parseObject(response.bodyString()).get("key");
                    String url = "http://" + QINIU_IMAGE_DOMAIN + "/" + key;
                    //存入数据库
                    //补全数据
                    Image image = new Image();
                    image.setOriginal(original);
                    image.setId(String.valueOf(idWorker.nextId()));
                    image.setUserId(principal.getId());
                    image.setUrl(url);
                    image.setStatus(0);
                    image.setCreateTime(new Date());
                    int save = imageMapper.save(image);
                    //将图片地址返回
                    return ResponseVo.SUCCESS().setMsg("上传成功").setData(url);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return ResponseVo.FAILURE().setMsg("请上传【jpg,png,gif,psd】类型的图片！");
    }

    @Override
    public ResponseVo getImageList(int page, int size, String original) {
        try {
            //获取全部
            if(page == 0){
                List<Image> imageList = userMapper.getImageList(null, original);
                return ResponseVo.SUCCESS().setData(imageList);
            }else {
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                User principal = (User) authentication.getPrincipal();
                PageHelper.startPage(page, size);
                List<Image> imageList = userMapper.getImageList(principal.getId(), original);
                PageInfo<Image> imagePageInfo = new PageInfo<>(imageList);
                return ResponseVo.SUCCESS().setData(imagePageInfo);
            }
        } catch (Exception e) {
            return ResponseVo.FAILURE().setMsg("获取失败");
        }
    }

    @Override
    public ResponseVo applyJoin(Audit audit) {

        User currentUser = Tools.getCurrentUser();
        audit.setUserId(currentUser.getId());
        //判断是更新还是保存
        if(audit.getId() != null){
            //更新
            audit.setStatus(Constants.ActivityStatus.TO_AUDIT);
            audit.setUpdateTime(new Date());
            boolean success = auditMapper.update(audit) == 1;
            if(!success){
                log.error("applyJoin: update audit failed data={}",audit);
                return ResponseVo.FAILURE().setMsg("更新失败");
            }
        }else {
            //先查询当前用户是否已存在社团，若存在则提示退出当前社团
            if(Objects.isNull(currentUser.getClubId())){
                return ResponseVo.FAILURE().setMsg("请先申请入社!");
            }else {
                audit.setId(String.valueOf(idWorker.nextId()));
                audit.setStatus(Constants.ActivityStatus.TO_AUDIT);
                audit.setCreateTime(new Date());
               boolean success = auditMapper.save(audit) == 1;
                if(!success){
                    log.error("applyJoin: save audit failed data={}",audit);
                    return ResponseVo.FAILURE().setMsg("申请失败");
                }
            }
        }
        return ResponseVo.SUCCESS().setMsg("更新成功");
    }

    @Override
    public ResponseVo applyQuit(Audit audit) {
        //退团申请，1查看当前用户是否存在社团，或退团id与自身社团id不符合
        User currentUser = Tools.getCurrentUser();
        //不存在社团
        if(currentUser.getClubId() == null || currentUser.getClubId().equals("0")){
            return ResponseVo.FAILURE().setMsg("你当前不在社团内!");
        }else if(!audit.getClubId().equals(currentUser.getClubId())){
            return ResponseVo.FAILURE().setMsg("你当前不在此社团内!");
        }else {
            audit.setUserId(currentUser.getId());
            audit.setId(String.valueOf(idWorker.nextId()));
            audit.setStatus(Constants.ActivityStatus.TO_AUDIT);
            audit.setCreateTime(new Date());
            boolean success = auditMapper.save(audit) == 1;
            if(!success){
                log.error("applyQuit: save audit failed data={}",audit);
                return ResponseVo.FAILURE().setMsg("申请失败");
            }
        }
        return ResponseVo.SUCCESS().setMsg("申请成功");
    }

    @Override
    public ResponseVo applyLeave(Audit audit) {
        User currentUser = Tools.getCurrentUser();
            audit.setUserId(currentUser.getId());
            audit.setId(String.valueOf(idWorker.nextId()));
            audit.setStatus(Constants.ActivityStatus.TO_AUDIT);
            audit.setCreateTime(new Date());
            boolean success = auditMapper.save(audit) == 1;
            if(!success){
                log.error("applyLeave: save audit failed data={}",audit);
                return ResponseVo.FAILURE().setMsg("申请失败");
            }
        return ResponseVo.SUCCESS().setMsg("申请成功");
    }

    @Override
    public ResponseVo applyList(int page, int size) {
        PageHelper.startPage(page,size);
        List<Audit> list = auditMapper.list(Tools.getCurrentUser().getId());
        PageInfo<Audit> auditPageInfo = new PageInfo<>(list);
        return ResponseVo.SUCCESS().setData(auditPageInfo);
    }

    @Override
    public ResponseVo applyDelete(String id) {
        boolean success = auditMapper.remove(id) == 1;
        if (!success) {
            log.error("applyDelete: remove apply failed data={}",id);
            return ResponseVo.FAILURE().setMsg("删除失败");
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo applyActivity(String activityId) {
        User currentUser = Tools.getCurrentUser();
        if(Objects.isNull(currentUser)){
            return ResponseVo.FAILURE().setMsg("请登陆后操作");
        }
        //是否已经加入了该活动
        UserActivity byId = userActivityMapper.findById(currentUser.getId(),activityId);
        if(Objects.isNull(byId)){
            return ResponseVo.FAILURE().setMsg("请登陆后操作");
        }
        UserActivity userActivity = new UserActivity();
        userActivity.setId(String.valueOf(idWorker.nextId()));
        userActivity.setUserId(currentUser.getId());
        userActivity.setActivityId(activityId);
        boolean success = userActivityMapper.save(userActivity) == 1;
        if (!success) {
            log.error("applyActivity: save userActivity failed data={}",userActivity);
            return ResponseVo.FAILURE().setMsg("加入失败");
        }
        return ResponseVo.SUCCESS().setMsg("加入成功");
    }

    private List<Menu> getChild(String id, List<Menu> menuList) {
        //子菜单
        List<Menu> childMenu = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId().equals(id)) {
                childMenu.add(menu);
            }
        }
        return childMenu;
    }

    @Transactional
    @Override
    public ResponseVo save(User entity) {
        //数据校验
        if(!Tools.isChinaPhoneLegal(entity.getPhone())){
            return ResponseVo.FAILURE().setMsg("手机号不合法");
        }
        if(entity.getSno().length() != 11){
            return ResponseVo.FAILURE().setMsg("学号不合法");
        }
        //检查学号是否已存在
        User sno = userMapper.isExistSno(entity.getSno());
        if(!Objects.isNull(sno)){
            return ResponseVo.FAILURE().setMsg("该学号已存在");
        }
        //先去查询当前是否存在该账户
        User userByPhone = userMapper.findUserByPhone(entity.getPhone());
        if (!Objects.isNull(userByPhone)){
            return ResponseVo.FAILURE().setMsg("该用户已存在");
        }else {
            if (entity.getAvatar() == null || entity.getAvatar().equals("")) {
                entity.setAvatar(Constants.DEFAULT.AVATAR);
            }
            entity.setCreateTime(new Date());
            entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
            entity.setId(String.valueOf(idWorker.nextId()));
            entity.setRegIp(request.getRemoteAddr());
            entity.setStatus(0);
            boolean success = userMapper.save(entity) == 1;
            if (!success){
                log.error("saveUser: save user failed data={}",entity);
                return ResponseVo.FAILURE().setMsg("添加失败");
            }
            //设置当前用户的角色为普通用户
            UserRole userRole = new UserRole();
            userRole.setId(String.valueOf(idWorker.nextId()));
            userRole.setUserId(entity.getId());
            userRole.setRoleId(Constants.RoleId.ROLE_USER);
            boolean res = userRoleMapper.save(userRole) == 1;
            if (!res){
                log.error("saveUserRole: save userRole failed data={}",entity);
                return ResponseVo.FAILURE().setMsg("添加失败");
            }
        }
        return ResponseVo.SUCCESS().setMsg("添加成功");
    }



    @Override
    public ResponseVo remove(String id) {
        boolean success = userMapper.remove(id) == 1;
        if (!success) {
            log.error("removeUser: remove user failed data={}",id);
            return ResponseVo.FAILURE().setMsg("删除失败");
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo update(User entity) {
        boolean update = userMapper.update(entity) == 1;
        if(!update){
            log.error("updateUser: update user failed data={}",entity);
            return ResponseVo.FAILURE().setMsg("更新失败");
        }
        return ResponseVo.SUCCESS().setMsg("更新成功");
    }

    @Override
    public ResponseVo getList(int page, int size, int status, String keyWords) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        PageHelper.startPage(page, size);
        String userId = principal.getId();
        List<UserVo> all;
        //如果是超级管理员，则可以查看全部的用户信息
        if (principal.getRoles().getRoleName().equals(Constants.Role.ROLE_ADMIN)) {
            all = userMapper.selectAll();
        }else {
            //查询当前用户创建的社团下的成员列表
           all = userMapper.getListByClubId(userId, principal.getClubId());
        }
        //如果社团为空
        Iterator<UserVo> iterator = all.iterator();
        while (iterator.hasNext()) {
            UserVo userVo = iterator.next();
            //如果用户无社团
            if (userVo.getClub() == null) {
                userVo.setClub(new Club());
            }
        }
        PageInfo<UserVo> userPageInfo = new PageInfo<>(all);
        return ResponseVo.SUCCESS().setData(userPageInfo);
    }

    @Override
    public ResponseVo getActivityInfo() {
        //获取当前用户
        User currentUser = Tools.getCurrentUser();
        List<Activity> byClubId = activityMapper.findByClubId(currentUser.getClubId());
        if(Objects.isNull(byClubId)){
            log.error("getActivityInfo: get Info failed data={}",currentUser.getClubId());
            return ResponseVo.FAILURE().setMsg("获取失败");
        }
        return ResponseVo.SUCCESS().setData(byClubId);
    }
}
