package com.example.demo.business.user.service;

import com.example.demo.base.BaseService;
import com.example.demo.base.ResponseVo;
import com.example.demo.business.user.entity.Audit;
import com.example.demo.business.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author joy
 * @version 1.0
 * @date 2020/12/28 11:54
 */
public interface UserService extends UserDetailsService, BaseService<User> {


    /**
     * 通过手机号获取用户信息
     * @param phone
     * @return
     */
    @Override
    UserDetails loadUserByUsername(String phone);

    /**
     * 获取用户菜单
     * @param request
     * @param response
     * @return
     */
    ResponseVo getMenuList(HttpServletRequest request, HttpServletResponse response);

    /**
     *上传图片
     * @param file
     * @return
     */
    ResponseVo uploadImage(MultipartFile file, String original);

    /**
     * 分页获取图片列表
     * @param page
     * @param size
     * @param original
     * @return
     */
    ResponseVo getImageList(int page, int size, String original);

    /**
     * 入团申请
     * @param audit
     * @return
     */
    ResponseVo applyJoin(Audit audit);

    /**
     * 退团申请
     * @param audit
     * @return
     */
    ResponseVo applyQuit(Audit audit);

    /**
     * 请假申请
     * @param audit
     * @return
     */
    ResponseVo applyLeave(Audit audit);

    /**
     * 我的申请列表
     * @param page
     * @param size
     * @return
     */
    ResponseVo applyList(int page, int size);

    /**
     * 根据id删除申请
    * @param id
     * @return
     */
    ResponseVo applyDelete(String id);
}
