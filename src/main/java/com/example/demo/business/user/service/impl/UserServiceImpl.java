package com.example.demo.business.user.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.club.entity.Club;
import com.example.demo.business.user.entity.UserVo;
import com.example.demo.business.user.service.UserService;
import com.example.demo.business.user.entity.Menu;
import com.example.demo.business.user.entity.User;
import com.example.demo.business.user.mapper.UserMapper;
import com.example.demo.utils.Constants;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
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
            if (menu.getParentId() == new Long(0)) {
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
    public ResponseVo uploadImage(MultipartFile file) {
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
                    //将图片地址返回
                    return ResponseVo.SUCCESS().setMsg("上传成功").setData("http://" + QINIU_IMAGE_DOMAIN + "/" + key);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {

            return ResponseVo.FAILURE().setMsg("请上传【jpg,png,gif,psd】类型的图片！");
        }
        return null;
    }

    private List<Menu> getChild(long id, List<Menu> menuList) {
        //子菜单
        List<Menu> childMenu = new ArrayList<>();
        for (Menu menu : menuList) {
            if (menu.getParentId() == id) {
                childMenu.add(menu);
            }
        }
        return childMenu;
    }


    @Override
    public ResponseVo save(User entity) {
        return null;
    }

    @Override
    public ResponseVo remove(Integer id) {
        return null;
    }

    @Override
    public ResponseVo update(User entity) {
        entity.setUpdateTime(new Date());
            int update = userMapper.update(entity);
            if(update != 1) {
                return ResponseVo.FAILURE();
            }
        return ResponseVo.SUCCESS();
    }

    @Override
    public ResponseVo getList(int page, int size, String keyWords) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User principal = (User) authentication.getPrincipal();
        PageHelper.startPage(page, size);
        List<UserVo> all = userMapper.getListByClubId(principal.getId());
        Iterator<UserVo> iterator = all.iterator();
        while (iterator.hasNext()) {
            UserVo next = iterator.next();
            if (next.getId().equals(principal.getId())) {
                iterator.remove();
            }
        }
        PageInfo<UserVo> userPageInfo = new PageInfo<>(all);
        return ResponseVo.SUCCESS().setData(userPageInfo);
    }
}
