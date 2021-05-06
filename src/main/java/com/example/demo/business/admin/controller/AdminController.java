package com.example.demo.business.admin.controller;

import com.example.demo.base.ResponseVo;
import com.example.demo.business.admin.entity.Settings;
import com.example.demo.business.admin.service.ActivityService;
import com.example.demo.business.admin.service.impl.AdminServiceImpl;
import com.example.demo.business.user.entity.Image;
import com.example.demo.business.user.service.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author joy
 * @version 1.0
 * @date 2021/3/9 20:18
 */
@RestController
@Api(value = "管理员接口",tags = "管理员接口")
@RequestMapping("/admin")
public class AdminController{
    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private UserService userService;

    @Autowired
    private ActivityService activityService;
    /**
     * 获取注册用户数
     *
     * @return
     */
    @GetMapping("/web_site_info/register_count")
    public ResponseVo getUserCount() {
        return adminService.getUserCount();
    }

    /**
     * 获取社团总数
     *
     * @return
     */
    @GetMapping("/web_site_info/club_count")
    public ResponseVo getClubCount() {
        return adminService.getClubCount();
    }


    /**
     * 获取活动总数
     * @return
     */
    @GetMapping("/web_site_info/activity_count")
    public ResponseVo getActivityCount() {
        return adminService.getActivityCount();
    }

    /**
     * 获取登录日志
     * @return
     */
    @GetMapping("/web_site_info/loginLog")
    public ResponseVo loginLog() {
        return adminService.loginLog();
    }


    /**
     * 轮播图上传
     *
     * @param file
     * @return
     */
    @PostMapping("/upload/carousel")
    public ResponseVo uploadImage(@RequestParam("file") MultipartFile file,
                                  @RequestParam("original") String original) {
        return userService.uploadImage(file,original);
    }


    /**
     * 获取所有的角色
     * @return
     */
    @GetMapping("/role/list")
    public ResponseVo roleList(){
        return adminService.roleList();
    }

    /**
     * 获取所有的菜单权限
     * @param id
     * @return
     */
    @GetMapping("/power/list")
    public  ResponseVo powerList(@RequestParam(value = "id",required = false) String id){
        return adminService.powerList(id);
    }


    /**
     * 角色管理
     * @param roleId
     * @param idStr
     * @return
     */
    @PostMapping("/role/manage/{roleId}/{idStr}")
    public ResponseVo manageRole(@PathVariable(value = "roleId") String roleId,
                                @PathVariable(value = "idStr") String idStr){
        return adminService.manageRole(roleId,idStr);
    }

    @PostMapping("/role/issue/{userId}/{roleId}")
    public ResponseVo issueRole(@PathVariable(value = "roleId") String roleId,
                                @PathVariable(value = "userId") String userId){
        return adminService.issueRole(userId,roleId);
    }

    /**
     * 人员列表
     *
     * @return
     */
    @GetMapping("/personnelList/{page}/{size}")
    public ResponseVo personnelList(@PathVariable(value = "page", required = false) int page,
                                    @PathVariable(value = "size", required = false) int size,
                                    @RequestParam(value = "status",required = false) Integer status,
                                    @RequestParam(value = "keyWords", required = false) String keyWords) {
        return adminService.getList(page, size, status,keyWords);
    }


    /**
     *轮播图更新
     * @return
     */
    @PostMapping("/update/carousel")
    public ResponseVo updateCarousel(@RequestBody Image image) {
        return adminService.updateCarousel(image);
    }

    /**
     *轮播图删除
     * @return
     */
    @PostMapping("/delete/carousel/{id}")
    public ResponseVo deleteCarousel(@PathVariable String id) {
        return adminService.deleteCarousel(id);
    }

    /**
     *关于设置
     * @return
     */
    @PostMapping("/save/about")
    public ResponseVo saveAbout(@RequestBody Settings settings) {
        return adminService.saveAbout(settings);
    }

    /**
     *关于设置
     * @return
     */
    @GetMapping("/get/about")
    public ResponseVo getAbout() {
        return adminService.getAbout();
    }
}
