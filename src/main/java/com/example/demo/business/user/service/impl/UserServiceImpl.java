package com.example.demo.business.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Club;
import com.example.demo.business.user.entity.Image;
import com.example.demo.business.user.entity.UserVo;
import com.example.demo.business.user.mapper.ImageMapper;
import com.example.demo.business.user.service.UserService;
import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.mapper.UserMapper;
import com.example.demo.utils.Constants;
import com.example.demo.utils.SnowflakeIdWorker;
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
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
    private SnowflakeIdWorker idWorker;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private ImageMapper imageMapper;
    @Autowired
    private HttpServletRequest request;

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
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        User user = userMapper.findUserByPhone(s);
        if (user == null) {
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
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            User principal = (User) authentication.getPrincipal();
            PageHelper.startPage(page, size);
            List<Image> imageList = userMapper.getImageList(principal.getId(), original);
            PageInfo<Image> imagePageInfo = new PageInfo<>(imageList);
            return ResponseVo.SUCCESS().setData(imagePageInfo);
        } catch (Exception e) {
            return ResponseVo.FAILURE().setMsg("获取失败");
        }

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

    @Override
    public ResponseVo save(User entity) {
        if (entity.getAvatar() == null) {
            entity.setAvatar(Constants.DEFAULT.AVATAR);
        }
        entity.setCreateTime(new Date());
        entity.setPassword(bCryptPasswordEncoder.encode(entity.getPassword()));
        entity.setId(String.valueOf(idWorker.nextId()));
        entity.setRegIp(request.getRemoteAddr());
        entity.setStatus(0);
        int save = userMapper.save(entity);
        if (save != 1) {
            return ResponseVo.FAILURE().setMsg("添加失败");
        }
        return ResponseVo.SUCCESS().setMsg("添加成功");
    }

    @Override
    public ResponseVo remove(String id) {
        int remove = userMapper.remove(id);
        if (remove != 1) {
            return ResponseVo.FAILURE();
        }
        return ResponseVo.SUCCESS().setMsg("删除成功");
    }

    @Override
    public ResponseVo update(User entity) {
        entity.setUpdateTime(new Date());
        int update = userMapper.update(entity);
        if (update != 1) {
            return ResponseVo.FAILURE();
        }
        return ResponseVo.SUCCESS().setMsg("更新成功");
    }

    @Override
    public ResponseVo getList(int page, int size, int status, String keyWords) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        PageHelper.startPage(page, size);
        String userId = principal.getId();
        Long clubId = Long.valueOf(principal.getClubId());
        //如果是超级管理员，则可以查看全部的用户信息
        if (principal.getRoles().getRoleName().equals(Constants.Role.ROLE_ADMIN)) {
            clubId = null;
        }
        //查询当前用户创建的社团下的成员列表
        List<UserVo> all = userMapper.getListByClubId(userId, clubId);
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
}
